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
}
