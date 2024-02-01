package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ProducerServiceImpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final ExecutorTrackingService executorTrackingService;

    public ProducerServiceImpl(RabbitTemplate rabbitTemplate, ExecutorTrackingService executorTrackingService) {
        this.rabbitTemplate = rabbitTemplate;
        this.executorTrackingService = executorTrackingService;
    }
    @Override
    public void autoProduce(Resource resource) {
        // This is for water - it is freely produced in its own thread that does not count against focus (thread pool)
        int EXECUTOR_CORE_POOL_SIZE = 1;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
        Runnable runnable = () -> {
            rabbitTemplate.convertAndSend(resource.getName(), resource);
        };
        long MILLIS_PER_SECOND = 1000L;
        scheduledExecutorService.scheduleWithFixedDelay(runnable, resource.getProductionTime() * MILLIS_PER_SECOND,
                resource.getProductionTime() * MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
        executorTrackingService.register(scheduledExecutorService); // keep track of the executor
    }

    @Override
    public void produce(Resource resource, int amount) {
        // all other resources use this method to produce. Called by assembler and runs in the focus thread pool
        for (int i = 0; i < amount; i++) {
            rabbitTemplate.convertAndSend(resource.getName(), resource);
        }
    }
}
