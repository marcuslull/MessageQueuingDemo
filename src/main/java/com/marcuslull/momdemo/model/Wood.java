package com.marcuslull.momdemo.model;

import java.io.Serializable;

public class Wood implements Serializable {

    private String name;

    public Wood(String name) {
        this.name = name;
    }

    public Wood() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Wood{" +
                "name='" + name + '\'' +
                '}';
    }
}
