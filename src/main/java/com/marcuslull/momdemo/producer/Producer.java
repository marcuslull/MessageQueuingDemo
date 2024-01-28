package com.marcuslull.momdemo.producer;

import com.marcuslull.momdemo.model.Resource;

public interface Producer {
    void autoProduce(Resource resource);
    void produce(Resource resource, int amount);
    void stopProduction();
}
