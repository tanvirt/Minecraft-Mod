package com.quantumindustries.minecraft.mod.proxy;

import com.quantumindustries.minecraft.mod.ModWorldGen;
import com.quantumindustries.minecraft.mod.blocks.ModBlocks;
import com.quantumindustries.minecraft.mod.fluids.ModFluids;
import com.quantumindustries.minecraft.mod.items.ModItems;
import com.quantumindustries.minecraft.mod.recipes.ModRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ModBlocks.init();
        ModItems.init();
        ModFluids.init();
        ModRecipes.init();
        GameRegistry.registerWorldGenerator(new ModWorldGen(), 3);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        // TODO
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        // TODO
    }

}
