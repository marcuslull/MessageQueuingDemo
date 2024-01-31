package com.marcuslull.momdemo.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ExecutorTrackingService {
    private final List<ThreadPoolExecutor> threadPoolExecutorList;

    public ExecutorTrackingService() {
        this.threadPoolExecutorList = new ArrayList<>();
    }
    public void register(Executor executor) {
        if (executor instanceof ThreadPoolExecutor) {
            threadPoolExecutorList.add((ThreadPoolExecutor) executor);
        }
    }
    public void shutdownAll() {
        threadPoolExecutorList.forEach(scheduledExecutorService -> {
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
        threadPoolExecutorList.clear();
    }
}
