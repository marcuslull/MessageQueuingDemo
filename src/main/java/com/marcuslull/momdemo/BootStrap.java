package com.marcuslull.momdemo;

import com.marcuslull.momdemo.model.Simulation;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.producer.WaterProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrap implements CommandLineRunner {
    private final Simulation simulation;
    private final WaterProducer waterProducer;

    public BootStrap(Simulation simulation, WaterProducer waterProducer) {
        this.simulation = simulation;
        this.waterProducer = waterProducer;
    }

    @Override
    public void run(String... args) {

        // game start
        simulation.setCurrentTechLevel(TechLevel.TECH_LEVEL_1);

        // water clicked
        waterProducer.autoProduce();
        waterProducer.produce(10);
        waterProducer.stopProduction();
    }
}
