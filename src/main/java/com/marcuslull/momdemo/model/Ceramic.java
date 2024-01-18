package com.marcuslull.momdemo.model;

import java.io.Serializable;

public class Ceramic implements Serializable {
    private String name;

    public Ceramic(String name) {
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
        return "Ceramic{" +
                "name='" + name + '\'' +
                '}';
    }
}
