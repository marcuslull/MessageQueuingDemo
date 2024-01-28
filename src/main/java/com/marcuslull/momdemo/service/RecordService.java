package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.Production;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    private final ResourceRecord water;
    private final ResourceRecord food;
    private final ResourceRecord work;
    private final ResourceRecord education;
    private final ResourceRecord stone;
    private final ResourceRecord wood;
    private final ResourceRecord energy;

    public RecordService() {
        this.water = new ResourceRecord("Water", TechLevel.TECH_LEVEL_1, Rarity.COMMON, Production.FAST, Difficulty.EASY);
        this.food = new ResourceRecord("Food", TechLevel.TECH_LEVEL_1, Rarity.UNCOMMON, Production.MEDIUM, Difficulty.MEDIUM);
        this.work = new ResourceRecord("Work", TechLevel.TECH_LEVEL_1, Rarity.COMMON, Production.FAST, Difficulty.MEDIUM);
        this.education = new ResourceRecord("Education", TechLevel.TECH_LEVEL_1, Rarity.RARE, Production.SLOW, Difficulty.HARD);
        this.stone = new ResourceRecord("Stone", TechLevel.TECH_LEVEL_1, Rarity.COMMON, Production.MEDIUM, Difficulty.EASY);
        this.wood = new ResourceRecord("Wood", TechLevel.TECH_LEVEL_1, Rarity.COMMON, Production.FAST, Difficulty.MEDIUM);
        this.energy = new ResourceRecord("Energy", TechLevel.TECH_LEVEL_1, Rarity.RARE, Production.FAST, Difficulty.EASY);
    }

    public ResourceRecord getWater() {
        return water;
    }

    public ResourceRecord getFood() {
        return food;
    }

    public ResourceRecord getWork() {
        return work;
    }

    public ResourceRecord getEducation() {
        return education;
    }

    public ResourceRecord getStone() {
        return stone;
    }

    public ResourceRecord getWood() {
        return wood;
    }

    public ResourceRecord getEnergy() {
        return energy;
    }
}
