package com.marcuslull.momdemo.model.controller;

import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.producer.FoodProducer;
import com.marcuslull.momdemo.producer.WaterProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Simulation {
    private WaterProducer waterProducer;
    private FoodProducer foodProducer;
    private TechLevel currentTechLevel;
    public Simulation() {}
    @Autowired
    public void setWaterProducer(WaterProducer waterProducer) { this.waterProducer = waterProducer; }
    @Autowired
    public void setFoodProducer(FoodProducer foodProducer) { this.foodProducer = foodProducer; }
    public void start() {
        setCurrentTechLevel(TechLevel.TECH_LEVEL_1);
        System.out.println("Simulation started..."); // TODO: replace with logger
        waterProducer.autoProduce();
        foodProducer.autoProduce();
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
