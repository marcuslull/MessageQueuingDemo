package com.marcuslull.momdemo.model;

import java.io.Serializable;

public class Labor implements Serializable {

    private String name;

    public Labor(String name) {
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
        return "Labor{" +
                "name='" + name + '\'' +
                '}';
    }
}
