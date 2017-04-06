package com.quantumindustries.minecraft.mod.recipes;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;

import com.quantumindustries.minecraft.mod.items.ModItems;
import net.minecraft.block.BlockFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.FMLLog;

public class ParticleAcceleratorRecipes {

    private static final ParticleAcceleratorRecipes ACCELERATOR_BASE = new ParticleAcceleratorRecipes();
    private final Map<ItemStack, ItemStack> acceleratorList = Maps.<ItemStack, ItemStack>newHashMap();

    public static ParticleAcceleratorRecipes instance() {
        return ACCELERATOR_BASE;
    }

    private ParticleAcceleratorRecipes() {
        addAccelerating(ModItems.netherStarTarget, new ItemStack(ModItems.neutronStarParticle));
    }

    public void addAccelerating(Item input, ItemStack stack) {
        addAcceleratingRecipe(new ItemStack(input, 1, 32767), stack);
    }

    public void addAcceleratingRecipe(ItemStack input, ItemStack stack) {
        if(getAcceleratingResult(input) != null) {
            FMLLog.info(
                    "Ignored accelerator recipe with conflicting input: %s = %s",
                    input.toString(), stack.toString()
            );
            return;
        }
        acceleratorList.put(input, stack);
    }

    public ItemStack getAcceleratingResult(ItemStack stack) {
        for(Entry<ItemStack, ItemStack> entry : acceleratorList.entrySet()) {
            if(compareItemStacks(stack, (ItemStack) entry.getKey())) {
                return (ItemStack) entry.getValue();
            }
        }
        return null;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getAcceleratorList() {
        return acceleratorList;
    }
}
