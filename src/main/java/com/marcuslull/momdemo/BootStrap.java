package com.marcuslull.momdemo;

import com.marcuslull.momdemo.producer.CeramicProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrap implements CommandLineRunner {

    private final CeramicProducer ceramicProducer;

    public BootStrap(CeramicProducer ceramicProducer) {
        this.ceramicProducer = ceramicProducer;
    }

    @Override
    public void run(String... args) {

//        ceramicProducer.produce(3);
//        ceramicProducer.produce(5);
    }
}
