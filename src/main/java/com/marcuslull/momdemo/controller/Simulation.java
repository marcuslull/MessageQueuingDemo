package com.marcuslull.momdemo.controller;

import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import com.marcuslull.momdemo.service.AssemblerService;
import com.marcuslull.momdemo.service.CountService;
import com.marcuslull.momdemo.service.ProducerService;
import com.marcuslull.momdemo.service.RecordService;
import com.marcuslull.momdemo.view.ViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class Simulation {
    private ViewModel viewModel;
    private RecordService recordService;
    private AssemblerService assemblerService;
    private CountService countService;
    private ProducerService producerService;
    private TechLevel currentTechLevel;
    private Resource water;
    private Resource food;
    private Resource work;
    private Resource education;
    private Resource stone;
    private Resource wood;
    private Resource energy;

    public Simulation(ViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Autowired
    public void setViewModel(ViewModel viewModel) { this.viewModel = viewModel; }
    @Autowired
    public void setRecordService(RecordService recordService) { this.recordService = recordService; }
    @Autowired
    public void setProducer(ProducerService producerService) { this.producerService = producerService; }
    @Autowired
    public void setAssembler(AssemblerService assemblerService) { this.assemblerService = assemblerService; }
    @Autowired
    public void setCountService(CountService countService) { this.countService = countService; }
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
        countService.monitorCount();
    }
    public void start() throws ExecutionException, InterruptedException {
        setCurrentTechLevel(TechLevel.TECH_LEVEL_1);
        producerService.autoProduce(water);
        //assemblerService.assemble(food, 10);
    }
    private void setCurrentTechLevel(TechLevel newTechLevel) {
        this.currentTechLevel = newTechLevel;
        switch (newTechLevel) {
            case TECH_LEVEL_1 -> viewModel.setTechLevel("Tech Level 1 achieved!");
            case TECH_LEVEL_2 -> viewModel.setTechLevel("Tech Level 2 achieved!");
            case TECH_LEVEL_3 -> viewModel.setTechLevel("Tech Level 3 achieved!");
            default -> viewModel.setTechLevel("Simulation complete!");
        }
    }
    public void advanceTechLevel() { setCurrentTechLevel(TechLevel.values()[currentTechLevel.ordinal() + 1]); }
}
