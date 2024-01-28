package com.marcuslull.momdemo.controller;

import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import com.marcuslull.momdemo.producer.Producer;
import com.marcuslull.momdemo.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Simulation {
    private RecordService recordService;
    private Producer producer;
    private TechLevel currentTechLevel;
    public Simulation() {}
    @Autowired
    public void setRecordService(RecordService recordService) { this.recordService = recordService; }
    @Autowired
    public void setWaterProducer(Producer producer) { this.producer = producer; }
    public void start() {
        setCurrentTechLevel(TechLevel.TECH_LEVEL_1);
        System.out.println("Simulation started..."); // TODO: replace with logger
        ResourceRecord waterRecord = recordService.getWaterRecord();
        Resource water = new Resource(waterRecord);
        producer.autoProduce(water);
    }
    private void setCurrentTechLevel(TechLevel newTechLevel) {
        this.currentTechLevel = newTechLevel;
        switch (newTechLevel) {
            case TECH_LEVEL_1 -> System.out.println("Tech Level 1 achieved!"); // TODO: replace with logger
            case TECH_LEVEL_2 -> System.out.println("Tech Level 2 achieved!"); // TODO: replace with logger
            case TECH_LEVEL_3 -> System.out.println("Tech Level 3 achieved!"); // TODO: replace with logger
            default -> System.out.println("Simulation ended."); // TODO: replace with logger
        }
    }
    public void advanceTechLevel() { setCurrentTechLevel(TechLevel.values()[currentTechLevel.ordinal() + 1]); }
}
