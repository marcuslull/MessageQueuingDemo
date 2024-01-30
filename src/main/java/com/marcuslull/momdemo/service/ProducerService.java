package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;

public interface ProducerService {
    void autoProduce(Resource resource);
    void produce(Resource resource, int amount);
    void stopProduction();
}
