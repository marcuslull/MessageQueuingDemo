package com.marcuslull.momdemo;

import com.marcuslull.momdemo.controller.Simulation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrap implements CommandLineRunner {
    private final Simulation simulation;

    public BootStrap(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public void run(String... args) {
        simulation.start();
    }
}
