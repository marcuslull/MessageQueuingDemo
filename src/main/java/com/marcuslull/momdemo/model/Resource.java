package com.marcuslull.momdemo.model;

import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.ProductionTime;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;

import java.io.Serializable;

public abstract class Resource implements Serializable {

    private Rarity rarity;
    private Difficulty difficulty;
    private ProductionTime productionTime;
    private TechLevel techLevel;
}
