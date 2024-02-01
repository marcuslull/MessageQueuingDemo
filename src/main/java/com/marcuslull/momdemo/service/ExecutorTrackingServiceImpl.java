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
        // keep track of all the executors, so we can shut them down later if needed
        if (executor instanceof ThreadPoolExecutor) {
            threadPoolExecutorList.add((ThreadPoolExecutor) executor);
        }
    }
    @Override
    public void shutdownAll() {
        // shut down all the executors gracefully when the stop button is pressed
        threadPoolExecutorList.forEach(scheduledExecutorService -> {
            scheduledExecutorService.shutdown();
            try {
                // kill it if it doesn't shut down gracefully
                if(!scheduledExecutorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    scheduledExecutorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                // TODO: Handle this properly
                System.out.println("Producer: " + Thread.currentThread().getName() + " interrupted.");
            }
        });
        threadPoolExecutorList.clear(); // clear the list, so we have an empty list for the next run
    }
}
