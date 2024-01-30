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
        // TODO: Implement cleanup and escrow to implement a transactional system
        return CompletableFuture.supplyAsync(() -> {
            List<Resource> resources = new ArrayList<>();
            while (resources.size() < amount) {
                Message message = rabbitTemplate.receive(resource.getName());
                if (message != null) {
                    resources.add((Resource) rabbitTemplate.getMessageConverter().fromMessage(message));
                } else {
                    try {
                        Thread.sleep(RESOURCE_CONSUMPTION_ATTEMPT_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // TODO: Handle this exception
                    }
                }
            }
            return resources;
        });
    }
}
