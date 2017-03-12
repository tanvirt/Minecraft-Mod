package com.quantumindustries.minecraft.mod.recipes;

import com.quantumindustries.minecraft.mod.blocks.BlockOre;
import com.quantumindustries.minecraft.mod.blocks.ModBlocks;
import com.quantumindustries.minecraft.mod.items.ItemOre;
import com.quantumindustries.minecraft.mod.items.ModItems;
import com.sun.org.apache.xpath.internal.operations.Mod;
import javafx.scene.control.MultipleSelectionModelBuilder;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

    public static void init() {
        initShaped();
        initShapeless();
        initSmelting();
    }

    private static void initShaped() {
        initAirFilter();
        initNeoIronMagnet();
        initNeoCobaltMagnet();
        initNeoRhodiumMagnet();
        initBlockOre(ModBlocks.blockCobalt, ModItems.ingotCobalt);
        initBlockOre(ModBlocks.blockRhodium, ModItems.ingotRhodium);
        initBlockOre(ModBlocks.blockNeodymium, ModItems.ingotNeodymium);
    }

    private static void initShapeless() {
        initIngotFromBlock(ModItems.ingotCobalt, ModBlocks.blockCobalt);
        initIngotFromBlock(ModItems.ingotRhodium, ModBlocks.blockRhodium);
        initIngotFromBlock(ModItems.ingotNeodymium, ModBlocks.blockNeodymium);
    }

    private static void initSmelting() {
        initStandardOreSmelting(ModItems.ingotCobalt, ModBlocks.oreCobalt, ModItems.dustCobalt);
        initStandardOreSmelting(ModItems.ingotNeodymium, ModBlocks.oreNeodymium, ModItems.dustNeodymium);
        initStandardOreSmelting(ModItems.ingotRhodium, ModBlocks.oreRhodium, ModItems.dustRhodium);
    }

    // ------------------------------------------------------------------------
    // Smelting Recipes Section
    // ------------------------------------------------------------------------
    // TODO(CM): Change experience output possibly
    private static void initStandardOreSmelting(ItemOre item, BlockOre ore, ItemOre dust) {
        ItemStack ingot = new ItemStack(item);
        GameRegistry.addSmelting(ore, ingot, 0);
        GameRegistry.addSmelting(dust, ingot, 0);
    }

    private static void initBlockOre(BlockOre block, ItemOre item) {
        ItemStack blockOre = new ItemStack(block);
        GameRegistry.addShapedRecipe(
                blockOre, "III", "III", "III",
                'I', item
        );
    }

    // ------------------------------------------------------------------------
    // Shapeless Recipes Section
    // ------------------------------------------------------------------------
    private static void initIngotFromBlock(ItemOre item, BlockOre block) {
        ItemStack ingot = new ItemStack(item, 9);
        GameRegistry.addShapelessRecipe(ingot, block);
    }

    // ------------------------------------------------------------------------
    // Shaped Recipes Section
    // ------------------------------------------------------------------------
    private static void initAirFilter() {
        ItemStack airFilter = new ItemStack(ModItems.airFilter);
        GameRegistry.addShapedRecipe(
                airFilter, "PPP", "PCP", "PPP",
                'P', Items.PAPER,
                'C', Items.COAL
        );
    }

    private static void initNeoCobaltMagnet() {
        ItemStack neoCobaltMagnet = new ItemStack(ModBlocks.blockNeoCobaltMagnet);
        GameRegistry.addShapedRecipe(
                neoCobaltMagnet, "MCM", "CMC", "MCM",
                'M', ModBlocks.blockNeoIronMagnet,
                'C', ModItems.ingotCobalt
        );
    }

    private static void initNeoIronMagnet() {
        ItemStack neoIronMagnet = new ItemStack(ModBlocks.blockNeoIronMagnet);
        GameRegistry.addShapedRecipe(
                neoIronMagnet, "NIN", "ICI", "NIN",
                'N', ModItems.ingotNeodymium,
                'I', Items.IRON_INGOT,
                'C', Items.COAL
        );
    }

    private static void initNeoRhodiumMagnet() {
        ItemStack neoRhodiumMagnet = new ItemStack(ModBlocks.blockNeoRhodiumMagnet);
        GameRegistry.addShapedRecipe(
                neoRhodiumMagnet, "MRM", "RMR", "MRM",
                'M', ModBlocks.blockNeoCobaltMagnet,
                'R', ModItems.ingotRhodium
        );
    }

}
