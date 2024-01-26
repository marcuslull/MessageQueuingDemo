package com.marcuslull.momdemo.model;

import com.marcuslull.momdemo.model.records.ResourceRecord;

// Description provided by: https://bard.google.com/
public class Water extends Resource{
    private final String name = "Water";
    private final String description = "Mirror of the sky, cradle of life, water whispers from every spring. " +
            "Primitive hands cup its coolness, quench thirst, whisper prayers to unseen spirits. In its flow, " +
            "a society sees its own, ever-changing, reflecting the moon's dance and the sun's kiss. ";
    private int productionTime;
    public Water(Simulation simulation, ResourceRecord record) {
        this.rarity = record.rarity();
        this.difficulty = record.difficulty();
        this.production = record.production();
        this.techLevel = record.techLevel();
        this.productionTime = (this.difficulty.ordinal() * this.rarity.ordinal() * this.production.ordinal() *
                this.techLevel.ordinal()) / simulation.getCurrentTechLevel().ordinal();

        System.out.println(this.description);
        System.out.println(this.name + " production time: " + this.productionTime);
    }

    public Water() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(int productionTime) {
        this.productionTime = productionTime;
    }
}
