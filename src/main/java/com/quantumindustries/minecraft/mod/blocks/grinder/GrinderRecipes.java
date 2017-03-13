package com.quantumindustries.minecraft.mod.blocks.grinder;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GrinderRecipes {

    private static final GrinderRecipes grindingBase = new GrinderRecipes();
    private final Map grindingList = Maps.newHashMap();
    private final Map experienceList = Maps.newHashMap();

    public static GrinderRecipes instance() {
        return grindingBase;
    }

    private GrinderRecipes() {
        initRecipe(Blocks.STONEBRICK, Item.getItemFromBlock(Blocks.GRAVEL), 1);
        initRecipe(Blocks.STONE_SLAB, Item.getItemFromBlock(Blocks.GRAVEL), 1);
        initRecipe(Blocks.STONE_SLAB2, Item.getItemFromBlock(Blocks.GRAVEL), 1);
        initRecipe(Blocks.SANDSTONE_STAIRS, Item.getItemFromBlock(Blocks.GRAVEL), 1);
        initRecipe(Blocks.STONE, Item.getItemFromBlock(Blocks.GRAVEL), 1);
        initRecipe(Blocks.GRAVEL, Item.getItemFromBlock(Blocks.SAND), 1);
        initRecipe(Blocks.SANDSTONE, Item.getItemFromBlock(Blocks.SAND), 1);
        initRecipe(Blocks.GLASS, Item.getItemFromBlock(Blocks.SAND), 1);
        initRecipe(Blocks.BRICK_BLOCK, Item.getItemFromBlock(Blocks.GRAVEL), 1);
        initRecipe(Blocks.PLANKS, Items.PAPER, 10);
        initRecipe(Blocks.LOG, Items.PAPER, 1);
        initRecipe(Blocks.LOG2, Items.PAPER, 1);
        initRecipe(Blocks.NETHER_BRICK, Item.getItemFromBlock(Blocks.NETHERRACK), 1);
        initRecipe(Blocks.NETHER_BRICK_STAIRS, Item.getItemFromBlock(Blocks.NETHERRACK), 1);
        initRecipe(Blocks.NETHER_BRICK_FENCE, Item.getItemFromBlock(Blocks.NETHERRACK), 1);
        initRecipe(Blocks.NETHERRACK, Item.getItemFromBlock(Blocks.SOUL_SAND), 1);
        initRecipe(Blocks.SOUL_SAND, Items.GUNPOWDER, 5);
        initRecipe(Blocks.SLIME_BLOCK, Items.SLIME_BALL, 10);
        initRecipe(Blocks.OBSIDIAN, Items.FLINT, 10);
        initRecipe(Blocks.PRISMARINE, Items.PRISMARINE_SHARD, 10);
        initRecipe(Blocks.SEA_LANTERN, Items.PRISMARINE_CRYSTALS, 10);
    }

    private void initRecipe(Block block, Item item, int amount) {
        addGrindingRecipe(
                new ItemStack(Item.getItemFromBlock(block), 1, 32767),
                new ItemStack(item, amount), 0.7f
        );
    }

    public void addGrindingRecipe(ItemStack parItemStackIn, ItemStack parItemStackOut,
                                  float parExperience) {
        grindingList.put(parItemStackIn, parItemStackOut);
        experienceList.put(parItemStackOut, Float.valueOf(parExperience));
    }

    public ItemStack getGrindingResult(ItemStack parItemStack) {
        Iterator iterator = grindingList.entrySet().iterator();
        Entry entry;

        do {
            if(!iterator.hasNext()) {
                return null;
            }

            entry = (Entry) iterator.next();
        }
        while(!areItemStacksEqual(parItemStack, (ItemStack)entry.getKey()));

        return (ItemStack) entry.getValue();
    }

    private boolean areItemStacksEqual(ItemStack parItemStack1, ItemStack parItemStack2) {
        return parItemStack2.getItem() == parItemStack1.getItem() &&
                (parItemStack2.getMetadata() == 32767 ||
                        parItemStack2.getMetadata() == parItemStack1.getMetadata()
                );
    }

    public float getGrindingExperience(ItemStack parItemStack) {
        Iterator iterator = experienceList.entrySet().iterator();
        Entry entry;

        do {
            if(!iterator.hasNext()) {
                return 0;
            }

            entry = (Entry) iterator.next();
        }
        while (!areItemStacksEqual(parItemStack, (ItemStack) entry.getKey()));

        return ((Float) entry.getValue()).floatValue();
    }

}
