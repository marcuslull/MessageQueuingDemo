package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;

import java.util.List;
import java.util.concurrent.Future;

public interface ConsumerService {
    Future<List<Resource>> consume(Resource resource, int amount);
}
