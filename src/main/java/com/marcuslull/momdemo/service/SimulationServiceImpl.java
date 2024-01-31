package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import com.marcuslull.momdemo.view.ViewModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class SimulationServiceImpl implements SimulationService {
    private final RabbitTemplate rabbitTemplate;
    private final ViewModel viewModel;
    private final RecordService recordService;
    private final AssemblerService assemblerService;
    private final CountService countService;
    private final ProducerService producerService;
    private TechLevel currentTechLevel;
    private Resource water;
    private Resource food;
    private Resource work;
    private Resource education;
    private Resource stone;
    private Resource wood;
    private Resource energy;

    public SimulationServiceImpl(RabbitTemplate rabbitTemplate, ViewModel viewModel, RecordService recordService, AssemblerService assemblerService, CountService countService, ProducerService producerService) {
        this.rabbitTemplate = rabbitTemplate;
        this.viewModel = viewModel;
        this.recordService = recordService;
        this.assemblerService = assemblerService;
        this.countService = countService;
        this.producerService = producerService;
    }
    @Override
    public void init() {
        ResourceRecord waterRecord = recordService.getRecord("Water");
        water = new Resource(waterRecord);
        viewModel.setResources(water);
        ResourceRecord foodRecord = recordService.getRecord("Food");
        food = new Resource(foodRecord);
        viewModel.setResources(food);
        ResourceRecord workRecord = recordService.getRecord("Work");
        work = new Resource(workRecord);
        viewModel.setResources(work);
        ResourceRecord educationRecord = recordService.getRecord("Education");
        education = new Resource(educationRecord);
        viewModel.setResources(education);
        ResourceRecord stoneRecord = recordService.getRecord("Stone");
        stone = new Resource(stoneRecord);
        viewModel.setResources(stone);
        ResourceRecord woodRecord = recordService.getRecord("Wood");
        wood = new Resource(woodRecord);
        viewModel.setResources(wood);
        ResourceRecord energyRecord = recordService.getRecord("Energy");
        energy = new Resource(energyRecord);
        viewModel.setResources(energy);
    }
    @Override
    public void start() throws ExecutionException, InterruptedException {
        countService.monitorCount();
        assemblerService.updateViewModel();
        setCurrentTechLevel(TechLevel.TECH_LEVEL_1);
        producerService.autoProduce(water);

        //assemblerService.assemble(food, 10);
    }
    @Override
    public void reset() {
        rabbitTemplate.execute(channel -> {
            channel.queuePurge("Water");
            channel.queuePurge("Food");
            channel.queuePurge("Work");
            channel.queuePurge("Education");
            channel.queuePurge("Stone");
            channel.queuePurge("Wood");
            channel.queuePurge("Energy");
            return null;
        });
        setCurrentTechLevel(TechLevel.TECH_LEVEL_1);
    }
    @Override
    public void setCurrentTechLevel(TechLevel newTechLevel) {
        this.currentTechLevel = newTechLevel;
        switch (newTechLevel) {
            case TECH_LEVEL_1 -> viewModel.setTechLabel("1");
            case TECH_LEVEL_2 -> viewModel.setTechLabel("2");
            case TECH_LEVEL_3 -> viewModel.setTechLabel("3");
            default -> viewModel.setTechLabel("Simulation complete!");
        }
    }
    @Override
    public void advanceTechLevel() { setCurrentTechLevel(TechLevel.values()[currentTechLevel.ordinal() + 1]); }
}
