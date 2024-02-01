package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import com.marcuslull.momdemo.view.ViewModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssemblerServiceImplTest {
    private ConsumerService consumerService = mock(ConsumerService.class);
    private ProducerService producerService = mock(ProducerService.class);
    private RecordService recordService = mock(RecordService.class);
    private ViewModel viewModel = mock(ViewModel.class);
    private ExecutorTrackingService executorTrackingService = mock(ExecutorTrackingService.class);
    private AssemblerService assemblerService = new AssemblerServiceImpl(consumerService, producerService,
            recordService, viewModel, executorTrackingService);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void assemble() throws ExecutionException, InterruptedException {
        Resource output = mock(Resource.class);
        Future<List<Resource>> futureListResources = mock(Future.class);
        Future<List<Resource>> future = mock(Future.class);
        Map<String, Integer> requirements = new HashMap<>();
        requirements.put("water", 1);
        ResourceRecord resourceRecord = new ResourceRecord("Food", "description", null,
                null, null, null, requirements);

        when(recordService.getRecord(any())).thenReturn(resourceRecord);
        when(consumerService.consume(any(), anyInt())).thenReturn(futureListResources);
        when(future.get()).thenReturn(new ArrayList<>());
        when(output.getProductionTime()).thenReturn(0);

        assemblerService.assemble(output, 1);
        await().atLeast(50, TimeUnit.MILLISECONDS); // Need a blocking pause of the next test will run and fail this one
        verify(producerService, atLeastOnce()).produce(any(), anyInt());
        verify(executorTrackingService, atLeastOnce()).register(any());
    }

    @Test
    void updateViewModel() {
        assemblerService.updateViewModel();
        verify(viewModel, atLeastOnce()).setFocus(anyInt());
    }

    @Test
    void initializeExecutor() {
        ExecutorService threadPoolExecutor = mock(ExecutorService.class);
        when(threadPoolExecutor.isTerminated()).thenReturn(true);
        assemblerService.initializeExecutor();
        verify(threadPoolExecutor, atLeastOnce());
    }
}