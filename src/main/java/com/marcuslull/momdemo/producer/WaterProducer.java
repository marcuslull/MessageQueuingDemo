package com.marcuslull.momdemo.producer;

import com.marcuslull.momdemo.model.Simulation;
import com.marcuslull.momdemo.model.Water;
import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.Production;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WaterProducer {
    private final RabbitTemplate rabbitTemplate;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Simulation simulation;
    private final ResourceRecord waterRR;
    private final Long MILLIS_PER_SECOND = 1000L;
    private final int CORE_POOL_SIZE = 1;
    private final Long TIMEOUT = 3L;

    public WaterProducer(RabbitTemplate rabbitTemplate, Simulation simulation) {
        this.rabbitTemplate = rabbitTemplate;
        this.simulation = simulation;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        this.waterRR = new ResourceRecord("Water", TechLevel.TECH_LEVEL_1, Rarity.COMMON, Production.FAST, Difficulty.EASY);
    }
    public void autoProduce() {
        Water water = new Water(simulation, waterRR);
        Runnable runnable = () -> {
            rabbitTemplate.convertAndSend("Water", water);
            System.out.println("Water Producer: " + Thread.currentThread().getName() + " Sending water...");
        };
        scheduledExecutorService.scheduleWithFixedDelay(runnable, water.getProductionTime() * MILLIS_PER_SECOND,
                water.getProductionTime() * MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
    }
    public void produce(int amount) {
        Water water = new Water(simulation, waterRR);
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
            if(scheduledExecutorService.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                System.out.println("Graceful shutdown successful.");
            }
        } catch (InterruptedException e) {
            // TODO: Handle this properly
            System.out.println("Water Producer: " + Thread.currentThread().getName() + " interrupted.");
        }
    }
}
