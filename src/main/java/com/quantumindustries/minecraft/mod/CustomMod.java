package com.quantumindustries.minecraft.mod;

import com.quantumindustries.minecraft.mod.proxy.CommonProxy;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CustomMod.MODID, version = CustomMod.VERSION)
public class CustomMod  {

    public static final String MODID = "custommod";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "Custom Mod";

    @SidedProxy(clientSide = "com.quantumindustries.minecraft.mod.proxy.ClientProxy",
            serverSide = "com.quantumindustries.minecraft.mod.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static CustomMod instance;

    public static final CustomTab tab = new CustomTab();

    public enum GUI {
        GRINDER
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static void registerItemRenderer(Item item, int meta, String id) {
        proxy.registerItemRenderer(item, meta, id);
    }

}
