package com.quantumindustries.minecraft.mod.proxy;

import com.quantumindustries.minecraft.mod.CustomMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        // TODO
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(CustomMod.instance, new GuiProxy() );
    }

    public void postInit(FMLPostInitializationEvent event) {
        // TODO
    }

    public void registerItemRenderer(Item item, int meta, String id) {
        ModelResourceLocation location = new ModelResourceLocation(
                CustomMod.MODID + ":" + id,
                "inventory"
        );
        ModelLoader.setCustomModelResourceLocation(item, meta, location);
    }

}
