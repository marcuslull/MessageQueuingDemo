package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.consumer.Consumer;
import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.producer.Producer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class AssemblerService {
    private final Consumer consumer;
    private final Producer producer;
    private final RecordService recordService;

    public AssemblerService(Consumer consumer, Producer producer, RecordService recordService) {
        this.consumer = consumer;
        this.producer = producer;
        this.recordService = recordService;
    }
    public void assemble(Resource output, int amount) {
        Map<String, Integer> requirements = output.getRequirements();
        for (int i = 0; i < amount; i++) {
            List<Future> listOfFutures = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
                Resource resource = new Resource(recordService.getRecord(entry.getKey()));
                listOfFutures.add(consumer.consume(resource, entry.getValue()));
            }
            for (Future future : listOfFutures) {
                try {
                    List<Resource> listOfResources = (List<Resource>) future.get();
                } catch (Exception e) {
                    e.printStackTrace(); // TODO: Handle this exception
                }
            }
            producer.produce(output, 1);
        }
    }
}
