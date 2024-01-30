package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class AssemblerService {
    private final ConsumerService consumerService;
    private final ProducerService producerService;
    private final RecordService recordService;

    public AssemblerService(ConsumerService consumerService, ProducerService producerService, RecordService recordService) {
        this.consumerService = consumerService;
        this.producerService = producerService;
        this.recordService = recordService;
    }
    public void assemble(Resource output, int amount) {
        Map<String, Integer> requirements = output.getRequirements();
        for (int i = 0; i < amount; i++) {
            List<Future> listOfFutures = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
                Resource resource = new Resource(recordService.getRecord(entry.getKey()));
                listOfFutures.add(consumerService.consume(resource, entry.getValue()));
            }
            for (Future future : listOfFutures) {
                try {
                    List<Resource> listOfResources = (List<Resource>) future.get();
                } catch (Exception e) {
                    System.out.println("Assembler: " + Thread.currentThread().getName() + " interrupted.");
                }
            }
            producerService.produce(output, 1);
        }
    }
}
