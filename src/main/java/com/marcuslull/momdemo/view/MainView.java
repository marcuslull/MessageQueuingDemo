package com.marcuslull.momdemo.view;

import com.marcuslull.momdemo.controller.Simulation;
import com.marcuslull.momdemo.model.Count;
import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.service.RecordService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Route
@PageTitle("Message queueing demo")
public class MainView extends VerticalLayout {
    private final Simulation simulation;
    private final ViewModel viewModel;
    private final RecordService recordService;
    private final Binder<ViewModel> binder;
    private final H2 titleH1 = new H2("Message queueing demo");
    private final Button startButton = new Button("Start");
    private final TextField techLevel = new TextField();
    private final Grid<Count> countGrid = new Grid<>(Count.class);
    private final List<Count> counts;
    private final Grid<Resource> resourceGrid = new Grid<>(Resource.class);
    private final List<Resource> resources;

    public MainView(Simulation simulation, ViewModel viewModel, RecordService recordService) {
        this.simulation = simulation;
        this.viewModel = viewModel;
        this.recordService = recordService;
        this.binder = new Binder<>(ViewModel.class);
        this.binder.bindInstanceFields(this);
        this.binder.setBean(viewModel);

        startButton.addClickListener(e -> {
            try {
                simulation.start();
            } catch (ExecutionException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        counts = viewModel.getCounts();
        countGrid.setItems(counts);

        resources = viewModel.getResources();
        resourceGrid.setItems(resources);
        resourceGrid.setColumns("name", "description", "rarity", "difficulty", "production", "techLevel", "requirements", "productionTime");

        add(titleH1, startButton, techLevel, countGrid, resourceGrid);
    }
}
