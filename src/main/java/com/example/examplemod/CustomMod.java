package com.example.examplemod;

import com.example.examplemod.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CustomMod.MODID, version = CustomMod.VERSION)
public class CustomMod  {

    public static final String MODID = "custommod";
    public static final String VERSION = "1.0";
    public static final String NAME = "Custom Mod";

    @SidedProxy(clientSide = "com.example.examplemod.proxy.ClientProxy",
            serverSide = "com.example.examplemod.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static CustomMod instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Override
    public String toString() {
        return "CustomMod class";
    }

}
