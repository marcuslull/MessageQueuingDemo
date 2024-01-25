package com.marcuslull.momdemo.model;

import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.Production;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;

import java.io.Serializable;

public abstract class Resource implements Serializable {

    protected Rarity rarity;
    protected Difficulty difficulty;
    protected Production production;
    protected TechLevel techLevel;
}
