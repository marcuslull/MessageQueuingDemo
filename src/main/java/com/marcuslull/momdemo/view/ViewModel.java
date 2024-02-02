package com.marcuslull.momdemo.view;

import com.marcuslull.momdemo.model.Count;
import com.marcuslull.momdemo.model.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ViewModel {
    private String techLabel = "0";
    private final List<Count> counts = new ArrayList<>();
    private final List<Resource> resources = new ArrayList<>();
    private int focus = 0;
    private String descriptionParagraph = "This interactive demo represents a learning exercise designed to familiarize myself with " +
            "the message oriented middle-ware architecture and asynchronous message passing. The application uses Spring " +
            "Boot, Spring AMQP using Rabbit and JUnit/Mockito testing dependencies. The front-end uses Vaadin Flow to create " +
            "the UI using only Java. " +
            "This demo is in the form of an " +
            "incremental game which I thought would be a novel approach to a messaging system. While it is a single " +
            "application, I've replicated a distributed, scalable and loosely " +
            "coupled system utilizing produced resources to replicate each distributed system. Each resource both " +
            "consumes other resources and produces an output resource each of which are represented by a message queue" +
            " to keep track of resource count.";
    private String startStopResetParagraph = "The first two buttons represent starting or stopping ongoing asynchronous " +
            "tasks that update the demo state or act as workers to consume and produce the various resources. In most cases " +
            "either a scheduled thread pool executor or a standard thread pool executor are used. The stop button makes " +
            "an attempt to shutdown all threads gracefully. " +
            "The reset button simply resets the demo by purging all message queues effectively resetting demo state.";
    private String focusParagraph = "The focus counter is a real-time view of active threads of a fixed size thread pool" +
            " executor. In the demo this should come across as a finite amount of focus, or workers, available to produce" +
            " resources. Producing a resource takes a single thread of focus for a fixed amount of time. During that " +
            "time the thread is not available, intentionally blocked with a sleep duration according to its production " +
            "time. The thread, or focus, is responsible for consuming resources from the message queues " +
            "according to resource requirements, and producing an output resource to the appropriate message queue" +
            " after the production time has elapsed.";
    private String availableResourcesParagraph = "Available resources is a live count of the various message queues. " +
            "Each queue representing a different resource of the demo. The messages per queue will fluctuate up or " +
            "down depending on the production and consumption of resources (messages).";
    private String produceButtonsParagraph = "Produce buttons allow for allocating focus (an asynchronously running thread) " +
            "to the production of that resource. There are only ten threads in the pool specified for the executor and" +
            " the production of each resource takes a fixed amount of time, so the focus will be tied up until the " +
            "production is complete. The focus thread pool uses a synchronous queue to maintain the order of production.";
    private String resourceInformationParagraph = "The resource information informs the demo user of various information " +
            "about the resource and its production and consumption. The requirements attribute indicates precursor " +
            "resources required to produce. Upon production, these resources will be consumed from the appropriate message " +
            "queue. Production time is derived from the rarity, difficulty, production, and tech level attributes of the " +
            "resource. This represents the amount of time in seconds a focus (thread) will be tied up to produce the resource. " +
            "Upon successful production the resource will be added to the appropriate message queue ready to be used by " +
            "other resources.";

    public ViewModel() {
    }

    public String getTechLabel() {
        return techLabel;
    }

    public void setTechLabel(String techLabel) {
        this.techLabel = techLabel;
    }

    public List<Count> getCounts() {
        return counts;
    }

    public void setCounts(Count count) {
        this.counts.clear();
        this.counts.add(count);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(Resource resource) {
        this.resources.add(resource);
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public String getDescriptionParagraph() {
        return descriptionParagraph;
    }

    public String getStartStopResetParagraph() {
        return startStopResetParagraph;
    }

    public String getFocusParagraph() {
        return focusParagraph;
    }

    public String getAvailableResourcesParagraph() {
        return availableResourcesParagraph;
    }

    public String getProduceButtonsParagraph() {
        return produceButtonsParagraph;
    }

    public String getResourceInformationParagraph() {
        return resourceInformationParagraph;
    }
}
