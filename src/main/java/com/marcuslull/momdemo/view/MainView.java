package com.marcuslull.momdemo.view;

import com.marcuslull.momdemo.controller.Simulation;
import com.marcuslull.momdemo.model.Count;
import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.service.RecordService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Route
@PageTitle("Message queueing demo")
public class MainView extends VerticalLayout {
    private final Simulation simulation;
    private final ViewModel viewModel;
    private final RecordService recordService;
    private final ScheduledExecutorService scheduledExecutorService;
    private final int EXECUTOR_CORE_POOL_SIZE = 1;
    private final H2 titleH1 = new H2("Message queueing demo");
    private final HorizontalLayout startStopButtonLayout = new HorizontalLayout();
    private final Button startButton = new Button("Start");
    private final Button terminateButton = new Button("Terminate");
    private final TextField techLevel = new TextField();
    private final Grid<Count> countGrid = new Grid<>(Count.class);
    private List<Count> counts;
    private final Grid<Resource> resourceGrid = new Grid<>(Resource.class);
    private final List<Resource> resources;
    private final Label techLevelLabel = new Label("Unlock more resources by increasing the tech level.");
    Binder<ViewModel> binder = new Binder<>(ViewModel.class);

    public MainView(Simulation simulation, ViewModel viewModel, RecordService recordService) {
        this.simulation = simulation;
        this.simulation.init();
        this.viewModel = viewModel;
        this.recordService = recordService;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
        this.binder.bindInstanceFields(this);
        this.binder.setBean(viewModel);

        setMargin(true);
        setWidth("98%");

        startStopButtonLayout.add(startButton, terminateButton);
        startButton.addClickListener(e -> {
            try {
                simulation.start();
            } catch (ExecutionException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            UI ui = UI.getCurrent();
            Runnable runnable = () -> {
                ui.access(() -> {
                    countGrid.setItems(viewModel.getCounts());
                    techLevel.setValue(viewModel.getTechLevel());
                    System.out.println(counts.getFirst().getWater());
                    ui.push();
                });
            };
            scheduledExecutorService.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.SECONDS);
        });

        terminateButton.addClickListener(e -> {
            scheduledExecutorService.shutdown();
            try {
                scheduledExecutorService.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        techLevel.setValue(viewModel.getTechLevel());

        counts = viewModel.getCounts();
        countGrid.setItems(counts);
        countGrid.setColumns("water", "food", "work", "education", "stone", "wood", "energy");
        countGrid.setHeight("100px");

        resources = viewModel.getResources();
        resourceGrid.setItems(resources);
        resourceGrid.setColumns("name", "description", "rarity", "difficulty", "production", "techLevel", "requirements", "productionTime");
        resourceGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        resourceGrid.setHeight("1000px");
        resourceGrid.getColumnByKey("description").setFlexGrow(8);
        resourceGrid.getColumnByKey("requirements").setFlexGrow(4);

        add(titleH1, startStopButtonLayout, techLevel, countGrid, resourceGrid, techLevelLabel);
    }
}
