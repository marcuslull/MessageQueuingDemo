package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import com.marcuslull.momdemo.view.ViewModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void initializeExecutor() throws NoSuchFieldException, IllegalAccessException {

        // Create a real ThreadPoolExecutor with a single thread pool size
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

        // Check the number of active threads and terminate before calling initializeExecutor()
        int poolSizeBefore = threadPoolExecutor.getCorePoolSize();
        threadPoolExecutor.shutdownNow();

        // Use reflection to set the ThreadPoolExecutor in assemblerService
        Field field = AssemblerServiceImpl.class.getDeclaredField("threadPoolExecutor");
        field.setAccessible(true);
        field.set(assemblerService, threadPoolExecutor);

        // This should trigger the creation of a new ThreadPoolExecutor because the current one is terminated
        assemblerService.initializeExecutor();

        // Check the number of active threads after calling initializeExecutor() - should be 10
        ThreadPoolExecutor newThreadPoolExecutor = (ThreadPoolExecutor) field.get(assemblerService);
        int poolSizeAfter = newThreadPoolExecutor.getCorePoolSize();

        // Verify that a new ThreadPoolExecutor was created/
        assertEquals(1, poolSizeBefore);
        assertEquals(10, poolSizeAfter);
    }
}