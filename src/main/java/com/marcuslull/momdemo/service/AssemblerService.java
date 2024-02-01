package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;

public interface AssemblerService {
    void assemble(Resource output, int amount);
    void updateViewModel();
    void initializeExecutor();
}
