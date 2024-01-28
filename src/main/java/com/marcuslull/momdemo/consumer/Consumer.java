package com.marcuslull.momdemo.consumer;

import com.marcuslull.momdemo.model.Resource;

import java.util.List;
import java.util.concurrent.Future;

public interface Consumer {
    Future<List<Resource>> consume(Resource resource, int amount);
}
