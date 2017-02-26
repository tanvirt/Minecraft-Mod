package com.quantumindustries.minecraft.mod.proxy;

import com.quantumindustries.minecraft.mod.blocks.ModBlocks;
import com.quantumindustries.minecraft.mod.items.ModItems;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ModItems.init();
        ModBlocks.init();
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
