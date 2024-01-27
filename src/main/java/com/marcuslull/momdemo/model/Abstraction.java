package com.marcuslull.momdemo.model;

import com.marcuslull.momdemo.model.controller.Simulation;
import com.marcuslull.momdemo.model.records.ResourceRecord;

// Description provided by: https://bard.google.com/
public class Abstraction extends Resource {
    private final Simulation simulation;
    private final String name = "Abstraction";
    private final String description = "Abstraction layers civilization like a skyscraper, " +
            "lifting us from the ground of basic needs to soar among the clouds of innovation, " +
            "each level built on the invisible foundations of knowledge and trust.";
    private int productionTime; // difficulty * rarity * production * tech level / current tech level

    public Abstraction(Simulation simulation, ResourceRecord record) {
        this.simulation = simulation;
        this.rarity = record.rarity();
        this.difficulty = record.difficulty();
        this.production = record.production();
        this.techLevel = record.techLevel();
//        this.productionTime = (this.difficulty.ordinal() * this.rarity.ordinal() * this.production.ordinal() *
//                this.techLevel.ordinal()) / simulation.getCurrentTechLevel().ordinal();
    }
}
