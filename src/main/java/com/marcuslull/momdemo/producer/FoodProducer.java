package com.marcuslull.momdemo.producer;

import com.marcuslull.momdemo.model.Food;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import com.marcuslull.momdemo.model.service.RecordService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class FoodProducer {
    private final RabbitTemplate rabbitTemplate;
    private final ScheduledExecutorService scheduledExecutorService;
    private final ResourceRecord foodRR;
    private final Long MILLIS_PER_SECOND = 1000L;

    public FoodProducer(RecordService recordService, RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.foodRR = recordService.getFood();
        int EXECUTOR_CORE_POOL_SIZE = 1;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
    }
    public void autoProduce() {
        Food food = new Food(foodRR);
        Runnable runnable = () -> {
            rabbitTemplate.convertAndSend("Food", food);
            System.out.println("Food Producer: " + Thread.currentThread().getName() + " Sending food...");
        };
        scheduledExecutorService.scheduleWithFixedDelay(runnable, food.getProductionTime() * MILLIS_PER_SECOND,
                food.getProductionTime() * MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
    }
    public void produce(int amount) {
        Food food = new Food(foodRR);
        Runnable runnable = () -> {
            rabbitTemplate.convertAndSend("Food", food);
            System.out.println("Food Producer: " + Thread.currentThread().getName() + " Sending food...");
        };
        for (int i = 0; i < amount; i++) {
            scheduledExecutorService.schedule(runnable, food.getProductionTime() * MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
        }
    }
    public void stopProduction() {
        scheduledExecutorService.shutdown();
        try {
            long EXECUTOR_SHUTDOWN_TIMEOUT = 3L;
            if(scheduledExecutorService.awaitTermination(EXECUTOR_SHUTDOWN_TIMEOUT, TimeUnit.SECONDS)) {
                System.out.println("Graceful shutdown of food production successful.");
            }
        } catch (InterruptedException e) {
            // TODO: Handle this properly
            System.out.println("Food Producer: " + Thread.currentThread().getName() + " interrupted.");
        }
    }
}
