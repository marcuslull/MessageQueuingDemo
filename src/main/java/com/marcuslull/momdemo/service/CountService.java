package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Count;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountService {
    private final RabbitTemplate rabbitTemplate;
    private List<String> resources = List.of("Water", "Food", "Work", "Education", "Stone", "Wood", "Energy");
    private long water;
    private long food;
    private long work;
    private long education;
    private long stone;
    private long wood;
    private long energy;

    public CountService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Count getCount() {
        for (String resource : resources) {
            long count = rabbitTemplate.execute(channel -> channel.messageCount(resource));
            switch (resource) {
                case "Water" -> water = count;
                case "Food" -> food = count;
                case "Work" -> work = count;
                case "Education" -> education = count;
                case "Stone" -> stone = count;
                case "Wood" -> wood = count;
                case "Energy" -> energy = count;
            }
        }
        return new Count(String.valueOf(water), String.valueOf(food), String.valueOf(work),
                String.valueOf(education), String.valueOf(stone), String.valueOf(wood), String.valueOf(energy));
    }
}
