package com.quantumindustries.minecraft.mod.recipes;

import com.quantumindustries.minecraft.mod.blocks.ModBlocks;
import com.quantumindustries.minecraft.mod.items.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
    }

    private static void initShapeless() {
        // Insert shapeless recipes here
    }

    private static void initSmelting() {
        initIngotCobalt();
        initIngotRhodium();
    }

    // TODO(CM): Change experience output possibly
    private static void initIngotCobalt() {
        ItemStack ingotCobalt = new ItemStack(ModItems.ingotCobalt);
        GameRegistry.addSmelting(ModBlocks.oreCobalt, ingotCobalt, 0);
    }

    // TODO(CM): Change experience output possibly
    private static void initIngotRhodium() {
        ItemStack ingotRhodium = new ItemStack(ModItems.ingotRhodium);
        GameRegistry.addSmelting(ModBlocks.oreRhodium, ingotRhodium, 0);
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
