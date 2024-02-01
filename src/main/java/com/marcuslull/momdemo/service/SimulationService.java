package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.enums.TechLevel;

import java.util.concurrent.ExecutionException;

public interface SimulationService {
    void init();
    void start() throws ExecutionException, InterruptedException;
    void reset();
    void setCurrentTechLevel(TechLevel newTechLevel);
    void advanceTechLevel();
}
