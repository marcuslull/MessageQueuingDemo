package com.marcuslull.momdemo.model;

import java.io.Serializable;

public class Education implements Serializable {
    private String name;

    public Education(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Education{" +
                "name='" + name + '\'' +
                '}';
    }
}
