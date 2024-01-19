package com.marcuslull.momdemo.producer;

import com.marcuslull.momdemo.model.Ceramic;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CeramicProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ScheduledExecutorService scheduledExecutorService;

    public CeramicProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(10);
    }

    public void produce(int interval) {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            Thread thread = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " Sending ceramic...");
                rabbitTemplate.convertAndSend("Ceramic", new Ceramic("Ceramic"));
            });
            thread.start();
        }, 0, interval * 1000L, TimeUnit.MILLISECONDS);
    }
}
