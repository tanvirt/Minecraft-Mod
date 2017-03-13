package com.quantumindustries.minecraft.mod.proxy;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.ModWorldGen;
import com.quantumindustries.minecraft.mod.blocks.ModBlocks;
import com.quantumindustries.minecraft.mod.fluids.ModFluids;
import com.quantumindustries.minecraft.mod.items.ModItems;
import com.quantumindustries.minecraft.mod.recipes.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ModBlocks.init();
        ModFluids.init();
        ModItems.init();
        ModRecipes.init();
        GameRegistry.registerWorldGenerator(new ModWorldGen(), 3);

        System.out.println("DEBUG: Client proxy preInit()");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(
                Item.getItemFromBlock(ModBlocks.blockGrinder),
                0,
                new ModelResourceLocation(
                        CustomMod.MODID + ":" + ModBlocks.blockGrinder
                                .getUnlocalizedName(),
                        "inventory"
                )
        );
        super.init(event);

        System.out.println("DEBUG: Client proxy init()");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        // TODO
        System.out.println("DEBUG: Client proxy postInit()");
    }

}
