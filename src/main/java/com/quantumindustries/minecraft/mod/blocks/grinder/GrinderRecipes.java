package com.quantumindustries.minecraft.mod.blocks.grinder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GrinderRecipes {

    private static Map<ItemStack, ItemStack> grindingList;
    private static Map<ItemStack, Float> experienceList;
    private static Map<String, Integer> grindingTimes;
    private static int metadata;

    public static void init() {
        grindingList = new HashMap<ItemStack, ItemStack>();
        experienceList = new HashMap<ItemStack, Float>();
        grindingTimes = new HashMap<String, Integer>();
        metadata = 32767;

        initRecipe(Blocks.STONEBRICK, Item.getItemFromBlock(Blocks.GRAVEL), 1, 200);
        initRecipe(Blocks.STONE_SLAB, Item.getItemFromBlock(Blocks.GRAVEL), 1, 200);
        initRecipe(Blocks.STONE_SLAB2, Item.getItemFromBlock(Blocks.GRAVEL), 1, 200);
        initRecipe(Blocks.SANDSTONE_STAIRS, Item.getItemFromBlock(Blocks.GRAVEL), 1, 200);
        initRecipe(Blocks.STONE, Item.getItemFromBlock(Blocks.GRAVEL), 1, 200);
        initRecipe(Blocks.GRAVEL, Item.getItemFromBlock(Blocks.SAND), 1, 200);
        initRecipe(Blocks.SANDSTONE, Item.getItemFromBlock(Blocks.SAND), 1, 200);
        initRecipe(Blocks.GLASS, Item.getItemFromBlock(Blocks.SAND), 1, 200);
        initRecipe(Blocks.BRICK_BLOCK, Item.getItemFromBlock(Blocks.GRAVEL), 1, 200);
        initRecipe(Blocks.PLANKS, Items.PAPER, 10, 200);
        initRecipe(Blocks.LOG, Items.PAPER, 1, 200);
        initRecipe(Blocks.LOG2, Items.PAPER, 1, 200);
        initRecipe(Blocks.NETHER_BRICK, Item.getItemFromBlock(Blocks.NETHERRACK), 1, 200);
        initRecipe(Blocks.NETHER_BRICK_STAIRS, Item.getItemFromBlock(Blocks.NETHERRACK), 1, 200);
        initRecipe(Blocks.NETHER_BRICK_FENCE, Item.getItemFromBlock(Blocks.NETHERRACK), 1, 200);
        initRecipe(Blocks.NETHERRACK, Item.getItemFromBlock(Blocks.SOUL_SAND), 1, 200);
        initRecipe(Blocks.SOUL_SAND, Items.GUNPOWDER, 5, 200);
        initRecipe(Blocks.SLIME_BLOCK, Items.SLIME_BALL, 10, 200);
        initRecipe(Blocks.OBSIDIAN, Items.FLINT, 10, 200);
        initRecipe(Blocks.PRISMARINE, Items.PRISMARINE_SHARD, 10, 200);
        initRecipe(Blocks.SEA_LANTERN, Items.PRISMARINE_CRYSTALS, 10, 200);
    }

    public static void addGrindingRecipe(ItemStack parItemStackIn, ItemStack parItemStackOut,
                                  float parExperience, int time) {
        grindingList.put(parItemStackIn, parItemStackOut);
        experienceList.put(parItemStackOut, Float.valueOf(parExperience));
        grindingTimes.put(parItemStackIn.getItem().getUnlocalizedName(), time);
    }

    public static ItemStack getGrindingResult(ItemStack parItemStack) {
        Iterator<Entry<ItemStack, ItemStack>> iterator = grindingList.entrySet().iterator();
        Entry<ItemStack, ItemStack> entry;

        do {
            if(!iterator.hasNext()) {
                return null;
            }
            entry = iterator.next();
        }
        while(!areItemStacksEqual(parItemStack, entry.getKey()));

        return entry.getValue();
    }

    public static float getGrindingExperience(ItemStack parItemStack) {
        Iterator<Entry<ItemStack, Float>> iterator = experienceList.entrySet().iterator();
        Entry<ItemStack, Float> entry;

        do {
            if(!iterator.hasNext()) {
                return 0;
            }
            entry = iterator.next();
        }
        while(!areItemStacksEqual(parItemStack, entry.getKey()));

        return entry.getValue();
    }

    public static int getGrindingTime(String name) {
        return grindingTimes.get(name);
    }

    private static void initRecipe(Block block, Item item, int amount, int time) {
        addGrindingRecipe(
                new ItemStack(Item.getItemFromBlock(block), 1, metadata),
                new ItemStack(item, amount), 0.7f, time
        );
    }

    private static boolean areItemStacksEqual(ItemStack parItemStack1, ItemStack parItemStack2) {
        return parItemStack2.getItem() == parItemStack1.getItem() &&
                (parItemStack2.getMetadata() == metadata ||
                        parItemStack2.getMetadata() == parItemStack1.getMetadata()
                );
    }

}
