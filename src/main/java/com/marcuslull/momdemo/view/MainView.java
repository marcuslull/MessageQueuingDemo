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
@PageTitle("Message Queueing Demo")
public class MainView extends VerticalLayout {
    private ScheduledExecutorService scheduledExecutorService;
    private final int EXECUTOR_CORE_POOL_SIZE = 1;
    private final Button startButton = new Button("Start");
    private final Button stopButton = new Button("Stop");
    private final Button resetButton = new Button("Reset");
    private final Label techLabel = new Label();
    private final Label focusLabel = new Label();
    private final Grid<Count> countGrid = new Grid<>(Count.class);
    private final HorizontalLayout createButtonLayout = new HorizontalLayout();

    public MainView(SimulationService simulationService, ViewModel viewModel, RecordService recordService, AssemblerService assemblerService, ExecutorTrackingService executorTrackingService, CountService countService) {

        // Initialize the backend
        simulationService.init();

        // UI Components
        H2 titleH1 = new H2("Message Queueing and Threading Demo");
        Label aboutLabel = new Label("Learn about how this was created");
        aboutLabel.getStyle().set("color", "blue");
        aboutLabel.getStyle().set("textDecoration", "underline");
        HorizontalLayout startStopButtonLayout = new HorizontalLayout();
        HorizontalLayout statusLayout = new HorizontalLayout();
        Label countGridLabel = new Label("Available resources");
        countGridLabel.getStyle().set("fontWeight", "bold");
        Button foodButton = new Button("Produce food");
        Button workButton = new Button("Produce work");
        Button educationButton = new Button("Produce education");
        Button stoneButton = new Button("Produce stone");
        Button woodButton = new Button("Produce wood");
        Button energyButton = new Button("Produce energy");
        Grid<Resource> resourceGrid = new Grid<>(Resource.class);
        this.stopButton.setEnabled(false);
        startStopButtonLayout.add(startButton, stopButton, resetButton);
        this.techLabel.setText("Tech level: " + viewModel.getTechLabel());
        this.techLabel.getStyle().set("fontWeight", "bold");
        this.focusLabel.setText("Available focus: " + viewModel.getFocus());
        this.focusLabel.getStyle().set("fontWeight", "bold");
        statusLayout.setWidth("100%");
        statusLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
        statusLayout.add(techLabel, focusLabel);
        List<Count> counts = viewModel.getCounts();
        this.countGrid.setItems(counts);
        this.countGrid.setColumns("water", "food", "work", "education", "stone", "wood", "energy");
        this.countGrid.setHeight("100px");
        this.createButtonLayout.setWidth("100%");
        this.createButtonLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
        foodButton.setEnabled(false);
        workButton.setEnabled(false);
        educationButton.setEnabled(false);
        stoneButton.setEnabled(false);
        woodButton.setEnabled(false);
        energyButton.setEnabled(false);
        this.createButtonLayout.add(foodButton, workButton, educationButton, stoneButton, woodButton, energyButton);
        Label resourceGridLabel = new Label("Resource information");
        resourceGridLabel.getStyle().set("fontWeight", "bold");
        List<Resource> resources = viewModel.getResources();
        resourceGrid.setItems(resources);
        resourceGrid.setColumns("name", "description", "rarity", "difficulty", "production", "techLevel", "requirements", "productionTime");
        resourceGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        resourceGrid.setHeight("1000px");
        resourceGrid.getColumnByKey("description").setFlexGrow(8);
        resourceGrid.getColumnByKey("requirements").setFlexGrow(4);
        Label techLevelLabel = new Label("Unlock more resources by increasing the tech level. ***NYI***");

        // Layout
        setMargin(true);
        setWidth("98%");
        add(titleH1, aboutLabel, startStopButtonLayout, statusLayout, countGridLabel, countGrid, createButtonLayout, resourceGridLabel, resourceGrid, techLevelLabel);

        // Button listeners
        aboutLabel.getElement().addEventListener("click", event ->
                aboutLabel.getUI().ifPresent(ui -> ui.navigate("learn")));

        startButton.addClickListener(e -> {
            try {
                simulationService.start(); // start the simulation
            } catch (ExecutionException | InterruptedException ex) {
                // This should be thrown if the simulation fails to start - something is fundamentally wrong
                throw new RuntimeException(ex);
            }
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            resetButton.setEnabled(false);
            createButtonLayout.getChildren().forEach(button -> button.getElement().setEnabled(true)); // add buttons for available resources
            // Start the UI update thread. Loops every 200ms and updates the UI.
            scheduledExecutorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
            Runnable runnable = getRunnable(viewModel, assemblerService);
            scheduledExecutorService.scheduleWithFixedDelay(runnable, 200, 200, TimeUnit.MILLISECONDS);
            executorTrackingService.register(scheduledExecutorService); // register the executor to be able to shut it down later
        });

        stopButton.addClickListener(e -> {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            resetButton.setEnabled(true);
            createButtonLayout.getChildren().forEach(button -> {
                button.getElement().setEnabled(false);
            });
            // shuts down all thread executors effectively pausing the simulation
            executorTrackingService.shutdownAll();
        });

        resetButton.addClickListener(e -> {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            simulationService.reset(); // reset the simulation, purging all message queues
            viewModel.getCounts().clear();
            countGrid.setItems(viewModel.getCounts());
        });

        createButtonLayout.getChildren().forEach(button -> {
            button.getElement().addEventListener("click", event -> {
                // Assemble the resource according to the button pushed by parsing the button text to get the resource name
                assemblerService.assemble(new Resource(recordService.getRecord(button.getElement().getText()
                        .substring(8))), 1);
            });
        });
    }

    // Private helper methods
    private Runnable getRunnable(ViewModel viewModel, AssemblerService assemblerService) {
        // Vaadin requires the UI be updated from the UI thread. This Runnable is used to update the UI.
        // The Runnable is scheduled to run every 200ms.
        UI ui = UI.getCurrent();
        return () -> {
            ui.access(() -> { // the Vaadin entry point to update the UI. End with ui.push() to push the changes to the UI
                countGrid.setItems(viewModel.getCounts()); // update the count grid
                techLabel.setText("Tech level: " + viewModel.getTechLabel()); // update the tech level
                assemblerService.updateViewModel(); // update the focus count (available threads in the thread pool)
                // disable/enable all create according to focus availability
                if (viewModel.getFocus() == 0) {
                    createButtonLayout.getChildren().forEach(button -> {
                        button.getElement().setEnabled(false);
                    });
                } else {
                    createButtonLayout.getChildren().forEach(button -> {
                        button.getElement().setEnabled(true);
                    });
                }
                focusLabel.setText("Available focus: " + viewModel.getFocus()); // update the focus label
                ui.push(); // push the changes to the UI
            });
        };
    }
}
