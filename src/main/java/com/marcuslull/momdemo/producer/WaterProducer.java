package com.marcuslull.momdemo.producer;

import com.marcuslull.momdemo.model.Water;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import com.marcuslull.momdemo.model.service.RecordService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WaterProducer {
    private final RabbitTemplate rabbitTemplate;
    private final ScheduledExecutorService scheduledExecutorService;
    private final ResourceRecord waterRR;
    private final Long MILLIS_PER_SECOND = 1000L;

    public WaterProducer(RecordService recordService, RabbitTemplate rabbitTemplate) {
        this.waterRR = recordService.getWater();
        this.rabbitTemplate = rabbitTemplate;
        int EXECUTOR_CORE_POOL_SIZE = 1;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
    }
    public void autoProduce() {
        Water water = new Water(waterRR);
        Runnable runnable = () -> {
            rabbitTemplate.convertAndSend("Water", water);
            System.out.println("Water Producer: " + Thread.currentThread().getName() + " Sending water...");
        };
        scheduledExecutorService.scheduleWithFixedDelay(runnable, water.getProductionTime() * MILLIS_PER_SECOND,
                water.getProductionTime() * MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
    }
    public void produce(int amount) {
        Water water = new Water(waterRR);
        Runnable runnable = () -> {
            rabbitTemplate.convertAndSend("Water", water);
            System.out.println("Water Producer: " + Thread.currentThread().getName() + " Sending water...");
        };
        for (int i = 0; i < amount; i++) {
            scheduledExecutorService.schedule(runnable, water.getProductionTime() * MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
        }
    }
    public void stopProduction() {
        scheduledExecutorService.shutdown();
        try {
            long EXECUTOR_SHUTDOWN_TIMEOUT = 3L;
            if(scheduledExecutorService.awaitTermination(EXECUTOR_SHUTDOWN_TIMEOUT, TimeUnit.SECONDS)) {
                System.out.println("Graceful shutdown successful.");
            }
        } catch (InterruptedException e) {
            // TODO: Handle this properly
            System.out.println("Water Producer: " + Thread.currentThread().getName() + " interrupted.");
        }
    }
}
