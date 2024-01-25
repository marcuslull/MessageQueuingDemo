package com.marcuslull.momdemo.model.records;

import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.Production;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;

public record ResourceRecord(
        String name,
        TechLevel techLevel,
        Rarity rarity,
        Production production,
        Difficulty difficulty) {
}
