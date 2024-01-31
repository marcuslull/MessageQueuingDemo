package com.marcuslull.momdemo.view;

import com.marcuslull.momdemo.model.Count;
import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.service.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
    private final SimulationService simulationService;
    private final ViewModel viewModel;
    private final RecordService recordService;
    private final AssemblerService assemblerService;
    private ScheduledExecutorService scheduledExecutorService;
    private final ExecutorTrackingService executorTrackingService;
    private final CountService countService;
    private final int EXECUTOR_CORE_POOL_SIZE = 1;
    private final H2 titleH1 = new H2("Message queueing and Threading demo.");
    private final HorizontalLayout startStopButtonLayout = new HorizontalLayout();
    private final Button startButton = new Button("Start");
    private final Button stopButton = new Button("Stop");
    private final Button resetButton = new Button("Reset");
    private final HorizontalLayout statusLayout = new HorizontalLayout();
    private final Label techLabel = new Label();
    private final Label focusLabel = new Label();
    private final Label countGridLabel = new Label("Available resources");
    private final Grid<Count> countGrid = new Grid<>(Count.class);
    private List<Count> counts;
    private final Grid<Resource> resourceGrid = new Grid<>(Resource.class);
    private final HorizontalLayout createButtonLayout = new HorizontalLayout();
    private final Button foodButton = new Button("Produce food");
    private final Button workButton = new Button("Produce work");
    private final Button educationButton = new Button("Produce education");
    private final Button stoneButton = new Button("Produce stone");
    private final Button woodButton = new Button("Produce wood");
    private final Button energyButton = new Button("Produce energy");
    private final List<Resource> resources;
    private final Label resourceGridLabel = new Label("Resource information");
    private final Label techLevelLabel = new Label("Unlock more resources by increasing the tech level.");

    public MainView(SimulationService simulationService, ViewModel viewModel, RecordService recordService, AssemblerService assemblerService, ExecutorTrackingService executorTrackingService, CountService countService) {
        this.simulationService = simulationService;
        this.viewModel = viewModel;
        this.recordService = recordService;
        this.assemblerService = assemblerService;
        this.executorTrackingService = executorTrackingService;
        this.countService = countService;
        this.simulationService.init();
        this.stopButton.setEnabled(false);
        this.startStopButtonLayout.add(startButton, stopButton, resetButton);
        this.techLabel.setText("Tech level: " + viewModel.getTechLabel());
        this.focusLabel.setText("Available focus: " + viewModel.getFocus());
        this.statusLayout.setWidth("100%");
        this.statusLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
        this.statusLayout.add(techLabel, focusLabel);
        this.counts = viewModel.getCounts();
        this.countGrid.setItems(counts);
        this.countGrid.setColumns("water", "food", "work", "education", "stone", "wood", "energy");
        this.countGrid.setHeight("100px");
        this.createButtonLayout.setWidth("100%");
        this.createButtonLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
        this.foodButton.setEnabled(false);
        this.workButton.setEnabled(false);
        this.educationButton.setEnabled(false);
        this.stoneButton.setEnabled(false);
        this.woodButton.setEnabled(false);
        this.energyButton.setEnabled(false);
        this.createButtonLayout.add(foodButton, workButton, educationButton, stoneButton, woodButton, energyButton);
        this.resources = viewModel.getResources();
        this.resourceGrid.setItems(resources);
        this.resourceGrid.setColumns("name", "description", "rarity", "difficulty", "production", "techLevel", "requirements", "productionTime");
        this.resourceGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        this.resourceGrid.setHeight("1000px");
        this.resourceGrid.getColumnByKey("description").setFlexGrow(8);
        this.resourceGrid.getColumnByKey("requirements").setFlexGrow(4);

        setMargin(true);
        setWidth("98%");
        add(titleH1, startStopButtonLayout, statusLayout, countGridLabel, countGrid, createButtonLayout, resourceGridLabel, resourceGrid, techLevelLabel);

        // Listeners
        startButton.addClickListener(e -> {
            try {
                simulationService.start();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                resetButton.setEnabled(false);
                createButtonLayout.getChildren().forEach(button -> {
                    button.getElement().setEnabled(true);
                });
            } catch (ExecutionException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            scheduledExecutorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
            UI ui = UI.getCurrent();
            Runnable runnable = () -> {
                ui.access(() -> {
                    countGrid.setItems(viewModel.getCounts());
                    techLabel.setText("Tech level: " + viewModel.getTechLabel());
                    assemblerService.updateViewModel();
                    if (viewModel.getFocus() == 0) {
                        createButtonLayout.getChildren().forEach(button -> {
                            button.getElement().setEnabled(false);
                        });
                    } else {
                        createButtonLayout.getChildren().forEach(button -> {
                            button.getElement().setEnabled(true);
                        });
                    }
                    focusLabel.setText("Available focus: " + viewModel.getFocus());
                    ui.push();
                });
            };
            scheduledExecutorService.scheduleWithFixedDelay(runnable, 200, 200, TimeUnit.MILLISECONDS);
            executorTrackingService.register(scheduledExecutorService);
        });

        stopButton.addClickListener(e -> {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            resetButton.setEnabled(true);
            createButtonLayout.getChildren().forEach(button -> {
                button.getElement().setEnabled(false);
            });
            executorTrackingService.shutdownAll();
        });

        resetButton.addClickListener(e -> {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            simulationService.reset();
            viewModel.getCounts().clear();
            countGrid.setItems(viewModel.getCounts());
        });

        createButtonLayout.getChildren().forEach(button -> {
            button.getElement()
                    .addEventListener("click", event -> {
                        assemblerService.assemble(new Resource(recordService.getRecord(
                                button.getElement().getText().substring(8))), 1);
            });
        });
    }
}
