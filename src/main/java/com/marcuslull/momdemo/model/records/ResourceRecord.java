package com.marcuslull.momdemo.model.records;

import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.Production;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;

import java.util.Map;

public record ResourceRecord(
        String name,
        String description,
        TechLevel techLevel,
        Rarity rarity,
        Production production,
        Difficulty difficulty,
        Map<String, Integer> requirements) {
}
