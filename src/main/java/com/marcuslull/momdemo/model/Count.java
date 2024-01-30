package com.marcuslull.momdemo.model;

public class Count {
    private String water;
    private String food;
    private String work;
    private String education;
    private String stone;
    private String wood;
    private String energy;

    public Count(String water, String food, String work, String education, String stone, String wood, String energy) {
        this.water = water;
        this.food = food;
        this.work = work;
        this.education = education;
        this.stone = stone;
        this.wood = wood;
        this.energy = energy;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getStone() {
        return stone;
    }

    public void setStone(String stone) {
        this.stone = stone;
    }

    public String getWood() {
        return wood;
    }

    public void setWood(String wood) {
        this.wood = wood;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }
}
