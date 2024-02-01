package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.view.ViewModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@Service
public class AssemblerServiceImpl implements AssemblerService {
    private final ConsumerService consumerService;
    private final ProducerService producerService;
    private final RecordService recordService;
    private final ViewModel viewModel;
    private ThreadPoolExecutor threadPoolExecutor;
    private final SynchronousQueue<Runnable> synchronousQueue;
    public static final int EXECUTOR_CORE_POOL_SIZE = 10;
    private final ExecutorTrackingService executorTrackingService;

    public AssemblerServiceImpl(ConsumerService consumerService, ProducerService producerService, RecordService recordService, ViewModel viewModel, ExecutorTrackingService executorTrackingService) {
        this.consumerService = consumerService;
        this.producerService = producerService;
        this.recordService = recordService;
        this.viewModel = viewModel;
        this.executorTrackingService = executorTrackingService;
        this.synchronousQueue = new SynchronousQueue<>(); // need synchronous to preserve order and focus (pool size) cap - no caching the pool
        this.threadPoolExecutor = new ThreadPoolExecutor(EXECUTOR_CORE_POOL_SIZE, EXECUTOR_CORE_POOL_SIZE,
                1L, TimeUnit.MILLISECONDS, synchronousQueue); // keep alive must be non-zero for .allowCoreThreadTimeOut(true) to work
    }
    @Override
    public void assemble(Resource output, int amount) {
        initializeExecutor(); // make sure we have a valid executor
        Runnable runnable = (() -> {
            Map<String, Integer> requirements = output.getRequirements(); // K: resource name, V: amount
            for (int i = 0; i < amount; i++) {
                // secure the resources needed for the output
                List<Future<List<Resource>>> listOfFutures = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
                    Resource resource = new Resource(recordService.getRecord(entry.getKey()));
                    listOfFutures.add(consumerService.consume(resource, entry.getValue())); // fetching the resources
                }
                // wait for the resources to be secured and then discard them simulating use
                for (Future<List<Resource>> future : listOfFutures) {
                    try {
                        List<Resource> listOfResources = future.get();
                        listOfResources.clear(); // discard the resources
                    } catch (Exception e) {
                        System.out.println("Assembler: " + Thread.currentThread().getName() + " interrupted.");
                    }
                }
                try {
                    // block the thread for the production time and then produce the output
                    sleep(output.getProductionTime() * 1000L);
                    producerService.produce(output, 1); // send the output to the appropriate message queue
                } catch (InterruptedException e) {
                    System.out.println("Assembler: " + Thread.currentThread().getName() + " interrupted.");
                }
            }
        });
        threadPoolExecutor.allowCoreThreadTimeOut(true); // timeout idle threads otherwise it messes up the focus count
        threadPoolExecutor.submit(runnable);
        executorTrackingService.register(threadPoolExecutor); // register the executor for tracking and proper shutdown
    }
    @Override
    public void updateViewModel() {
        // gives an accurate count of available threads which are called focus in the view model and UI
        // This is why idle threads need to be timed out asap
        viewModel.setFocus(threadPoolExecutor.getCorePoolSize() - threadPoolExecutor.getActiveCount());
    }
    @Override
    public void initializeExecutor() {
        // if the stop button is pressed and the constructor initialized executor is terminated, create a new one
        if (this.threadPoolExecutor.isTerminated()) {
            this.threadPoolExecutor = new ThreadPoolExecutor(EXECUTOR_CORE_POOL_SIZE, EXECUTOR_CORE_POOL_SIZE,
                    1L, TimeUnit.MILLISECONDS, synchronousQueue);
        }
    }
}
