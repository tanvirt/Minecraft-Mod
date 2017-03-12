package com.quantumindustries.minecraft.mod.recipes;

import com.quantumindustries.minecraft.mod.blocks.BlockOre;
import com.quantumindustries.minecraft.mod.blocks.ModBlocks;
import com.quantumindustries.minecraft.mod.items.ItemOre;
import com.quantumindustries.minecraft.mod.items.ModItems;
import com.sun.org.apache.xpath.internal.operations.Mod;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

    // ---------------------------------------------------------------------
    // Examples of how to add recipes:
    //
    // ItemStack foo = new ItemStack(ModItems.<item>);
    // GameRegistry.addShapelessRecipe(foo, craft result);
    // GameRegistry.addShapedRecipe(foo, "XXX", "XXX", "XXX", 'X', ModItems.newFoo);
    // GameRegistry.addSmelting(<block>, <item result>, <float number exp>);
    // ---------------------------------------------------------------------
    public static void init() {
        initShaped();
        initShapeless();
        initSmelting();
    }

    private static void initShaped() {
        initAirFilter();
        initBlockOre(ModBlocks.blockCobalt, ModItems.ingotCobalt);
        initBlockOre(ModBlocks.blockRhodium, ModItems.ingotRhodium);
    }

    private static void initShapeless() {
        initIngotFromBlock(ModItems.ingotCobalt, ModBlocks.blockCobalt);
        initIngotFromBlock(ModItems.ingotRhodium, ModBlocks.blockRhodium);
    }

    private static void initSmelting() {
        initIngotCobalt();
        initIngotRhodium();
    }

    // TODO(CM): Change experience output possibly
    private static void initIngotCobalt() {
        ItemStack ingotCobalt = new ItemStack(ModItems.ingotCobalt);
        GameRegistry.addSmelting(ModBlocks.oreCobalt, ingotCobalt, 0);
        GameRegistry.addSmelting(ModItems.dustCobalt, ingotCobalt, 0);
    }

    // TODO(CM): Change experience output possibly
    private static void initIngotRhodium() {
        ItemStack ingotRhodium = new ItemStack(ModItems.ingotRhodium);
        GameRegistry.addSmelting(ModBlocks.oreRhodium, ingotRhodium, 0);
        GameRegistry.addSmelting(ModItems.dustRhodium, ingotRhodium, 0);
    }

    private static void initBlockOre(BlockOre block, ItemOre item) {
        ItemStack blockOre = new ItemStack(block);
        GameRegistry.addShapedRecipe(
                blockOre, "III", "III", "III",
                'I', item
        );
    }

    private static void initIngotFromBlock(ItemOre item, BlockOre block) {
        ItemStack ingot = new ItemStack(item, 9);
        GameRegistry.addShapelessRecipe(ingot, block);
    }

    private static void initAirFilter() {
        ItemStack airFilter = new ItemStack(ModItems.airFilter);
        GameRegistry.addShapedRecipe(
                airFilter, "PPP", "PCP", "PPP",
                'P', Items.PAPER,
                'C', Items.COAL
        );
    }

}
