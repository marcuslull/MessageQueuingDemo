package com.marcuslull.momdemo.controller;

import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import com.marcuslull.momdemo.producer.Producer;
import com.marcuslull.momdemo.service.AssemblerService;
import com.marcuslull.momdemo.service.RecordService;
import com.marcuslull.momdemo.view.ViewModel;
import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class Simulation {
    private ViewModel viewModel;
    private RecordService recordService;
    private AssemblerService assemblerService;
    private Producer producer;
    private TechLevel currentTechLevel;
    public Simulation(ViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Autowired
    public void setViewModel(ViewModel viewModel) { this.viewModel = viewModel; }
    @Autowired
    public void setRecordService(RecordService recordService) { this.recordService = recordService; }
    @Autowired
    public void setProducer(Producer producer) { this.producer = producer; }
    @Autowired
    public void setAssembler(AssemblerService assemblerService) { this.assemblerService = assemblerService; }
    public void start() throws ExecutionException, InterruptedException {
        setCurrentTechLevel(TechLevel.TECH_LEVEL_1);

        // start producing water for free
        ResourceRecord waterRecord = recordService.getRecord("Water");
        Resource water = new Resource(waterRecord);
        producer.autoProduce(water);
        viewModel.setResources(water);


        // need food
        ResourceRecord foodRecord = recordService.getRecord("Food");
        Resource food = new Resource(foodRecord);
        assemblerService.assemble(food, 10);
        viewModel.setResources(food);

        UI.getCurrent().getPage().reload();
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
