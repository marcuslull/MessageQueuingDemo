package com.marcuslull.momdemo.view;

import com.marcuslull.momdemo.model.Count;
import com.marcuslull.momdemo.model.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ViewModel {
    private String techLevel = "Press start to begin";
    private List<Count> counts = new ArrayList<>();
    private List<Resource> resources = new ArrayList<>();

    public ViewModel() {
    }

    public String getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(String techLevel) {
        this.techLevel = techLevel;
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
}
