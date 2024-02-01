package com.marcuslull.momdemo.service;

import java.util.concurrent.Executor;

public interface ExecutorTrackingService {
    void register(Executor executor);
    void shutdownAll();
}
