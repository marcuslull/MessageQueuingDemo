package com.marcuslull.momdemo.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ExecutorTrackingServiceImpl implements ExecutorTrackingService {
    private final List<ThreadPoolExecutor> threadPoolExecutorList;

    public ExecutorTrackingServiceImpl() {
        this.threadPoolExecutorList = new ArrayList<>();
    }
    @Override
    public void register(Executor executor) {
        if (executor instanceof ThreadPoolExecutor) {
            threadPoolExecutorList.add((ThreadPoolExecutor) executor);
        }
    }
    @Override
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
