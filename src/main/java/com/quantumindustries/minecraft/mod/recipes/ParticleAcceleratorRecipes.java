package com.quantumindustries.minecraft.mod.recipes;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;

import com.quantumindustries.minecraft.mod.items.ModItems;
import com.quantumindustries.minecraft.mod.util.AcceleratingInput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ParticleAcceleratorRecipes {

    private static final ParticleAcceleratorRecipes ACCELERATOR_BASE = new ParticleAcceleratorRecipes();
    private final Map<AcceleratingInput, ItemStack> acceleratorList = Maps.newHashMap();

    public static ParticleAcceleratorRecipes instance() {
        return ACCELERATOR_BASE;
    }

    private ParticleAcceleratorRecipes() {
        // TODO(CM): fix power totals and power rates for recipes
        addAccelerating(ModItems.netherStarTarget, new ItemStack(ModItems.neutronStarParticle), 1000000, 1000);
    }

    public void addAccelerating(Item input, ItemStack stack, long powerTotal, long powerRate) {
        addAcceleratingRecipe(new ItemStack(input, 1, 32767), powerTotal, powerRate, stack);
    }

    public void addAcceleratingRecipe(ItemStack input, long powerTotal, long powerRate, ItemStack stack) {
        if(getAcceleratingResult(input) != null) {
            return;
        }

        acceleratorList.put(new AcceleratingInput(input, powerTotal, powerRate), stack);
    }

    public ItemStack getAcceleratingResult(ItemStack stack) {
        for(Entry<AcceleratingInput, ItemStack> entry : acceleratorList.entrySet()) {
            if(compareItemStacks(stack, entry.getKey().getInputStack())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public long getAcceleratingTotalPowerRequirement(ItemStack stack) {
        for(Entry<AcceleratingInput, ItemStack> entry : acceleratorList.entrySet()) {
            if(compareItemStacks(stack, entry.getKey().getInputStack())) {
                return entry.getKey().getTotalPowerRequired();
            }
        }
        return 0;
    }

    public long getAcceleratingRatePowerRequirement(ItemStack stack) {
        for(Entry<AcceleratingInput, ItemStack> entry : acceleratorList.entrySet()) {
            if(compareItemStacks(stack, entry.getKey().getInputStack())) {
                return entry.getKey().getPowerRateRequired();
            }
        }
        return 0;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() &&
                (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<AcceleratingInput, ItemStack> getAcceleratorList() {
        return acceleratorList;
    }
}
