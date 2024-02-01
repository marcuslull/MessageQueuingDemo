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
public class CountServiceImpl implements CountService {
    private final RabbitTemplate rabbitTemplate;
    private final ExecutorTrackingService executorTrackingService;
    private final ViewModel viewModel;
    private final List<String> resources = List.of("Water", "Food", "Work", "Education", "Stone", "Wood", "Energy");
    private Long water;
    private Long food;
    private Long work;
    private Long education;
    private Long stone;
    private Long wood;
    private Long energy;

    public CountServiceImpl(RabbitTemplate rabbitTemplate, ExecutorTrackingService executorTrackingService, ViewModel viewModel) {
        this.rabbitTemplate = rabbitTemplate;
        this.executorTrackingService = executorTrackingService;
        this.viewModel = viewModel;
    }
    @Override
    public Count getCount() {
        // we need to update the UI with the current counts of each resource
        for (String resource : resources) {
            // check each queue for the number of messages
            Long count = rabbitTemplate.execute(channel -> channel.messageCount(resource));
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
    @Override
    public void monitorCount() {
        // we need to monitor the count of each resource and update the UI with the current counts
        int EXECUTOR_CORE_POOL_SIZE = 1;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
        Runnable runnable = () -> {
            viewModel.setCounts(getCount());
        };
        scheduledExecutorService.scheduleWithFixedDelay(runnable, 0, 1, TimeUnit.SECONDS);
        executorTrackingService.register(scheduledExecutorService); // keep track of the executor service
    }
}
