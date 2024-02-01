package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class ConsumerServiceImpl implements ConsumerService {
    private final RabbitTemplate rabbitTemplate;
    private final long RESOURCE_CONSUMPTION_ATTEMPT_INTERVAL = 500;

    public ConsumerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Future<List<Resource>> consume(Resource resource, int amount) {
        // Gather resources from the message queue and return them as a list for the assembler to use
        return CompletableFuture.supplyAsync(() -> {
            List<Resource> resources = new ArrayList<>();
            while (resources.size() < amount) {
                // grab the message from the given queue
                Message message = rabbitTemplate.receive(resource.getName());
                if (message != null) { // make sure it is an actual message and convert it to a resource
                    resources.add((Resource) rabbitTemplate.getMessageConverter().fromMessage(message));
                } else {
                    // if there is no message, wait a bit and try again
                    try {
                        Thread.sleep(RESOURCE_CONSUMPTION_ATTEMPT_INTERVAL); // blocking is intentional to preserve realism
                    } catch (InterruptedException e) {
                        // swallowing - the only interruption we expect is from the stop button which would be intentional
                    }
                }
            }
            return resources;
        });
    }
}
