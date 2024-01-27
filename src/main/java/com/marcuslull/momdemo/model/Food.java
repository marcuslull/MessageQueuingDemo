package com.marcuslull.momdemo.model;

import com.marcuslull.momdemo.model.records.ResourceRecord;

// Description provided by: https://bard.google.com/
public class Food extends Resource {
    private final String name = "Food";
    private final String description = "Not plates, but palms, cradling hard-won berries, the earth's rough bounty. " +
            "Each bite, a testament to survival, a gruel of grit and grace, sung in the rustle of leaves, " +
            "the crackle of fire. Food, not feast, a primal song of need, where the hunt echoes in every swallow, " +
            "the sun's kiss lingers on every tongue.";
    private int productionTime;
    public Food(ResourceRecord record) {
        this.rarity = record.rarity();
        this.difficulty = record.difficulty();
        this.production = record.production();
        this.techLevel = record.techLevel();
        this.productionTime = (this.difficulty.ordinal() * this.rarity.ordinal() * this.production.ordinal() *
                this.techLevel.ordinal());

        System.out.println(this.description);
        System.out.println(this.name + " food production time: " + this.productionTime);
    }
    public Food() {
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getProductionTime() {
        return productionTime;
    }
    public void setProductionTime(int productionTime) {
        this.productionTime = productionTime;
    }
}
