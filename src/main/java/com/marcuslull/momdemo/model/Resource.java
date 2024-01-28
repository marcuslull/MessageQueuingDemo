package com.marcuslull.momdemo.model;

import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.Production;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;

import java.io.Serializable;

public class Resource implements Serializable {
    private final String name;
    private final String description;
    private final Rarity rarity;
    private final Difficulty difficulty;
    private final Production production;
    private final TechLevel techLevel;
    private int productionTime;

    public Resource(ResourceRecord record) {
        this.name = record.name();
        this.description = record.description();
        this.rarity = record.rarity();
        this.difficulty = record.difficulty();
        this.production = record.production();
        this.techLevel = record.techLevel();
        this.productionTime = (this.difficulty.ordinal() * this.rarity.ordinal() * this.production.ordinal() *
                this.techLevel.ordinal());

        System.out.println(this.description);
        System.out.println(this.name + " production time: " + this.productionTime);
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Rarity getRarity() {
        return rarity;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }
    public Production getProduction() {
        return production;
    }
    public TechLevel getTechLevel() {
        return techLevel;
    }
    public int getProductionTime() {
        return productionTime;
    }
    public void setProductionTime(int productionTime) {
        this.productionTime = productionTime;
    }
}
