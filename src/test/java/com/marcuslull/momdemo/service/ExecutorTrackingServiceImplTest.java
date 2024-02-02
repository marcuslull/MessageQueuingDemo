package com.marcuslull.momdemo.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ExecutorTrackingServiceImplTest {
    private ExecutorTrackingService executorTrackingService = new ExecutorTrackingServiceImpl();
    private ThreadPoolExecutor executor = mock(ThreadPoolExecutor.class);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void register() throws NoSuchFieldException, IllegalAccessException {
        executorTrackingService.register(executor);
        // reflection for the list
        Field field = ExecutorTrackingServiceImpl.class.getDeclaredField("threadPoolExecutorList");
        field.setAccessible(true);
        List<ThreadPoolExecutor> threadPoolExecutorList = (List<ThreadPoolExecutor>) field.get(executorTrackingService);

        assertEquals(1, threadPoolExecutorList.size());
    }

    @Test
    void shutdownAll() throws NoSuchFieldException, IllegalAccessException {
        // register an executor to shut down
        executorTrackingService.register(executor);
        // reflection for the list
        Field field = ExecutorTrackingServiceImpl.class.getDeclaredField("threadPoolExecutorList");
        field.setAccessible(true);
        List<ThreadPoolExecutor> threadPoolExecutorList = (List<ThreadPoolExecutor>) field.get(executorTrackingService);
        boolean firstAssertion = threadPoolExecutorList.size() == 1;
        // stop and clear the list
        executorTrackingService.shutdownAll();
        verify(executor, atLeastOnce()).shutdown();
        // check the list now
        threadPoolExecutorList = (List<ThreadPoolExecutor>) field.get(executorTrackingService);
        boolean secondAssertion = threadPoolExecutorList.isEmpty();
        assertTrue(firstAssertion && secondAssertion);
    }
}