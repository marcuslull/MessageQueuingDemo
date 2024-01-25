package com.marcuslull.momdemo.model;

import com.marcuslull.momdemo.model.enums.TechLevel;
import org.springframework.stereotype.Component;

@Component
public class Simulation {
    private TechLevel currentTechLevel;
    private String descriptionTL1 = "Tech Level 1";
    private String descriptionTL2 = "Tech Level 2";
    private String descriptionTL3 = "Tech Level 3";
    private String descriptionEnd = "Simulation ended.";

    public Simulation() {
    }

    public void start() {
        setCurrentTechLevel(TechLevel.TECH_LEVEL_1);
        System.out.println("Simulation started..."); // TODO: replace with logger
    }

    public TechLevel getCurrentTechLevel() {
        return currentTechLevel;
    }

    public void setCurrentTechLevel(TechLevel currentTechLevel) {
        this.currentTechLevel = currentTechLevel;
        switch (currentTechLevel) {
            case TECH_LEVEL_1 -> System.out.println(descriptionTL1); // TODO: replace with logger
            case TECH_LEVEL_2 -> System.out.println(descriptionTL2); // TODO: replace with logger
            case TECH_LEVEL_3 -> System.out.println(descriptionTL3); // TODO: replace with logger
            default -> System.out.println(descriptionEnd); // TODO: replace with logger
        }
    }
}
