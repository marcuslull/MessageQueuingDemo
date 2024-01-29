package com.marcuslull.momdemo.model;

public class Count {
    private final String name;
    private final int count;

    public Count(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
