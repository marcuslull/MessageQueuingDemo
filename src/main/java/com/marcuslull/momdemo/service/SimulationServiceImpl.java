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

    public SimulationServiceImpl(RabbitTemplate rabbitTemplate, ViewModel viewModel, RecordService recordService,
                                 AssemblerService assemblerService, CountService countService, ProducerService producerService) {
        this.rabbitTemplate = rabbitTemplate;
        this.viewModel = viewModel;
        this.recordService = recordService;
        this.assemblerService = assemblerService;
        this.countService = countService;
        this.producerService = producerService;
    }
    @Override
    public void init() {
        // creates the base resources and sends them to the view model using authoritative records
        ResourceRecord waterRecord = recordService.getRecord("Water");
        water = new Resource(waterRecord);
        viewModel.setResources(water);

        ResourceRecord foodRecord = recordService.getRecord("Food");
        Resource food = new Resource(foodRecord);
        viewModel.setResources(food);

        ResourceRecord workRecord = recordService.getRecord("Work");
        Resource work = new Resource(workRecord);
        viewModel.setResources(work);

        ResourceRecord educationRecord = recordService.getRecord("Education");
        Resource education = new Resource(educationRecord);
        viewModel.setResources(education);

        ResourceRecord stoneRecord = recordService.getRecord("Stone");
        Resource stone = new Resource(stoneRecord);
        viewModel.setResources(stone);

        ResourceRecord woodRecord = recordService.getRecord("Wood");
        Resource wood = new Resource(woodRecord);
        viewModel.setResources(wood);

        ResourceRecord energyRecord = recordService.getRecord("Energy");
        Resource energy = new Resource(energyRecord);
        viewModel.setResources(energy);
    }
    @Override
    public void start() throws ExecutionException, InterruptedException {
        countService.monitorCount(); // starting count monitor which checks resource counts every 1 second and updates the view model
        assemblerService.updateViewModel(); // get the initial thread count (as focus) and updates the view model
        setCurrentTechLevel(TechLevel.TECH_LEVEL_1); // set the initial tech level and update the view model
        producerService.autoProduce(water); // start producing water which is the base free resource
    }
    @Override
    public void reset() {
        // purges all the queues and resets the tech level
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
        // Updates the view model with the new tech level
        this.currentTechLevel = newTechLevel;
        switch (newTechLevel) {
            case TECH_LEVEL_1 -> viewModel.setTechLabel("1");
            case TECH_LEVEL_2 -> viewModel.setTechLabel("2");
            case TECH_LEVEL_3 -> viewModel.setTechLabel("3");
            default -> viewModel.setTechLabel("Simulation complete!");
        }
    }
    @Override
    public void advanceTechLevel() {
        // Tech levels are not currently implemented, but this method is here for future use
        setCurrentTechLevel(TechLevel.values()[currentTechLevel.ordinal() + 1]);
    }
}
