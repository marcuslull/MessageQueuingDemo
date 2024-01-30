package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Count;
import com.marcuslull.momdemo.view.ViewModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CountService {
    private final RabbitTemplate rabbitTemplate;
    private final ScheduledExecutorService scheduledExecutorService;
    private final ViewModel viewModel;
    private final int EXECUTOR_CORE_POOL_SIZE = 1;
    private List<String> resources = List.of("Water", "Food", "Work", "Education", "Stone", "Wood", "Energy");
    private long water;
    private long food;
    private long work;
    private long education;
    private long stone;
    private long wood;
    private long energy;

    public CountService(RabbitTemplate rabbitTemplate, ViewModel viewModel) {
        this.rabbitTemplate = rabbitTemplate;
        this.viewModel = viewModel;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
    }

    private Count getCount() {
        for (String resource : resources) {
            long count = rabbitTemplate.execute(channel -> channel.messageCount(resource));
            switch (resource) {
                case "Water" -> water = count;
                case "Food" -> food = count;
                case "Work" -> work = count;
                case "Education" -> education = count;
                case "Stone" -> stone = count;
                case "Wood" -> wood = count;
                case "Energy" -> energy = count;
            }
        }
        return new Count(String.valueOf(water), String.valueOf(food), String.valueOf(work),
                String.valueOf(education), String.valueOf(stone), String.valueOf(wood), String.valueOf(energy));
    }

    public void monitorCount() {
        Runnable runnable = () -> {
            viewModel.setCounts(getCount());
        };
        scheduledExecutorService.scheduleWithFixedDelay(runnable, 0, 1, TimeUnit.SECONDS);
    }
}
