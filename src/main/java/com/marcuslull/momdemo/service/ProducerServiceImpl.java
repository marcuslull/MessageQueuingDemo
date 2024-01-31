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
    private ScheduledExecutorService scheduledExecutorService;
    private final ExecutorTrackingService executorTrackingService;
    private final Long MILLIS_PER_SECOND = 1000L;
    private final int EXECUTOR_CORE_POOL_SIZE = 1;

    public ProducerServiceImpl(RabbitTemplate rabbitTemplate, ExecutorTrackingService executorTrackingService) {
        this.rabbitTemplate = rabbitTemplate;
        this.executorTrackingService = executorTrackingService;
    }
    @Override
    public void autoProduce(Resource resource) {
        scheduledExecutorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
        Runnable runnable = () -> {
            rabbitTemplate.convertAndSend(resource.getName(), resource);
        };
        scheduledExecutorService.scheduleWithFixedDelay(runnable, resource.getProductionTime() * MILLIS_PER_SECOND,
                resource.getProductionTime() * MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
        executorTrackingService.register(scheduledExecutorService);
    }

    @Override
    public void produce(Resource resource, int amount) {
        for (int i = 0; i < amount; i++) {
            rabbitTemplate.convertAndSend(resource.getName(), resource);
        }
    }
}
