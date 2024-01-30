package com.marcuslull.momdemo.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ExecutorTrackingService {
    private final List<ScheduledExecutorService> scheduledExecutorServiceList;

    public ExecutorTrackingService() {
        this.scheduledExecutorServiceList = new ArrayList<>();
    }
    public void register(ScheduledExecutorService scheduledExecutorService) {
        scheduledExecutorServiceList.add(scheduledExecutorService);
    }
    public void shutdownAll() {
        scheduledExecutorServiceList.forEach(scheduledExecutorService -> {
            System.out.println("Shutting down " + scheduledExecutorService + " in ExecutorsTrackingService.");
            scheduledExecutorService.shutdown();
            try {
                if(!scheduledExecutorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    scheduledExecutorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                // TODO: Handle this properly
                System.out.println("Producer: " + Thread.currentThread().getName() + " interrupted.");
            }
        });
        scheduledExecutorServiceList.clear();
    }
}
