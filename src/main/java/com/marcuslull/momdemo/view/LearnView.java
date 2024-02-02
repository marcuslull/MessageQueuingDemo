package com.marcuslull.momdemo.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/learn")
@PageTitle("Learn About")
public class LearnView extends VerticalLayout {
    private final ViewModel viewModel;

    public LearnView(ViewModel viewModel) {
        this.viewModel = viewModel;

        // components
        H2 h2 = new H2("Message Queueing and Threading Demo");
        Label descriptionLabel = new Label("Description");
        descriptionLabel.getStyle().set("fontWeight", "bold");
        Paragraph descriptionParagraph = new Paragraph(viewModel.getDescriptionParagraph());
        Label startStopResetLabel = new Label("Start Stop Reset");
        startStopResetLabel.getStyle().set("fontWeight", "bold");
        Paragraph startStopResetParagraph = new Paragraph(viewModel.getStartStopResetParagraph());
        descriptionLabel.getStyle().set("fontWeight", "bold");
        Label focusLabel = new Label("Focus");
        focusLabel.getStyle().set("fontWeight", "bold");
        Paragraph focusParagraph = new Paragraph(viewModel.getFocusParagraph());
        Label availableResourcesLabel = new Label("Available Resources");
        availableResourcesLabel.getStyle().set("fontWeight", "bold");
        Paragraph availableResourcesParagraph = new Paragraph(viewModel.getAvailableResourcesParagraph());
        Label produceButtonsLabel = new Label("Produce Buttons");
        produceButtonsLabel.getStyle().set("fontWeight", "bold");
        Paragraph produceButtonsParagraph = new Paragraph(viewModel.getProduceButtonsParagraph());
        Label resourceInformationLabel = new Label("Resource Information");
        resourceInformationLabel.getStyle().set("fontWeight", "bold");
        Paragraph resourceInformationParagraph = new Paragraph(viewModel.getResourceInformationParagraph());

        // layout
        setMargin(true);
        setWidth("98%");
        add(h2, descriptionLabel, descriptionParagraph, startStopResetLabel, startStopResetParagraph, focusLabel,
                focusParagraph, availableResourcesLabel, availableResourcesParagraph, produceButtonsLabel,
                produceButtonsParagraph, resourceInformationLabel, resourceInformationParagraph);
    }
}
