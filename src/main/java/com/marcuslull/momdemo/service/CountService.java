package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Count;

public interface CountService {
    Count getCount();
    void monitorCount();
}
