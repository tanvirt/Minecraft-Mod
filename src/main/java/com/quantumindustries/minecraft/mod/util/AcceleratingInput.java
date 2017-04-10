package com.quantumindustries.minecraft.mod.util;

import net.minecraft.item.ItemStack;

public class AcceleratingInput {

    private ItemStack inputStack;
    private long totalPowerRequired;
    private long powerRateRequired;

    public AcceleratingInput(ItemStack input, long power, long powerRate) {
        inputStack = input;
        totalPowerRequired = power;
        powerRateRequired = powerRate;
    }

    public ItemStack getInputStack() {
        return inputStack;
    }

    public long getTotalPowerRequired() {
        return totalPowerRequired;
    }

    public long getPowerRateRequired() {
        return powerRateRequired;
    }

    public void setInputStack(ItemStack input) {
        inputStack = input;
    }

    public void setPowerRequired(long power) {
        totalPowerRequired = power;
    }

    public void setPowerRateRequired(long power) {
        powerRateRequired = power;
    }
}
