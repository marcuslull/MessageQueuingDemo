package com.marcuslull.momdemo.producer;

import com.marcuslull.momdemo.model.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ProducerImpl implements Producer{
    private final RabbitTemplate rabbitTemplate;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Long MILLIS_PER_SECOND = 1000L;
    private final int EXECUTOR_CORE_POOL_SIZE = 1;

    public ProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
    }
    @Override
    public void autoProduce(Resource resource) {
        Runnable runnable = () -> {
            rabbitTemplate.convertAndSend(resource.getName(), resource);
        };
        scheduledExecutorService.scheduleWithFixedDelay(runnable, resource.getProductionTime() * MILLIS_PER_SECOND,
                resource.getProductionTime() * MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
    }

    @Override
    public void produce(Resource resource, int amount) {
        Runnable runnable = () -> {
            rabbitTemplate.convertAndSend(resource.getName(), resource);
        };
        for (int i = 0; i < amount; i++) {
            scheduledExecutorService.schedule(runnable, resource.getProductionTime() * MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void stopProduction() {
        scheduledExecutorService.shutdown();
        try {
            long EXECUTOR_SHUTDOWN_TIMEOUT = 3L;
            if(scheduledExecutorService.awaitTermination(EXECUTOR_SHUTDOWN_TIMEOUT, TimeUnit.SECONDS)) {
                System.out.println("Graceful shutdown of production successful.");
            }
        } catch (InterruptedException e) {
            // TODO: Handle this properly
            System.out.println("Producer: " + Thread.currentThread().getName() + " interrupted.");
        }
    }
}
