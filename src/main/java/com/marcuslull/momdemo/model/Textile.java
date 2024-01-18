package com.marcuslull.momdemo.model;

import java.io.Serializable;

public class Textile implements Serializable {

    private String name;

    public Textile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Textile{" +
                "name='" + name + '\'' +
                '}';
    }
}
