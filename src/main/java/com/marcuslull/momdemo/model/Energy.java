package com.marcuslull.momdemo.model;

import java.io.Serializable;

public class Energy implements Serializable {

    private String name;

    public Energy(String name) {
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
        return "Energy{" +
                "name='" + name + '\'' +
                '}';
    }
}
