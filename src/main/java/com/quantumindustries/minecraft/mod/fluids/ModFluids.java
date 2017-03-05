package com.quantumindustries.minecraft.mod.fluids;

import com.quantumindustries.minecraft.mod.CustomMod;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModFluids {

    public static void init() {
        initNitrogenFluid();
        initOxygenFluid();
        initArgonFluid();
    }

    private static void initNitrogenFluid() {
        Fluid fluid = createFluid("nitrogen");
        initBlockFluid("blockFluidNitrogen", "nitrogen", fluid, Material.WATER);
    }

    private static void initOxygenFluid() {
        Fluid fluid = createFluid("oxygen");
        initBlockFluid("blockFluidOxygen", "oxygen", fluid, Material.WATER);
    }

    private static void initArgonFluid() {
        Fluid fluid = createFluid("argon");
        initBlockFluid("blockFluidArgon", "argon", fluid, Material.WATER);
    }

    private static Fluid createFluid(String name) {
        String texturePrefix = CustomMod.MODID + ":block/" + name;
        Fluid fluid = new Fluid(
                "fluid_" + name,
                new ResourceLocation(texturePrefix + "_still"),
                new ResourceLocation(texturePrefix + "_flow")
        );
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }

    private static void initBlockFluid(String blockFluidName, String fluidName, Fluid fluid, Material material) {
        BlockFluidClassicBase blockFluid = new BlockFluidClassicBase(blockFluidName, fluid, material);
        registerFluid(blockFluid, fluidName);
    }

    private static void registerFluid(BlockFluidClassicBase blockFluid, String name) {
        registerBlock(blockFluid);
        Item item = Item.getItemFromBlock(blockFluid);
        ModelResourceLocation location = new ModelResourceLocation(
                CustomMod.MODID + ":fluids",
                name
        );
        ModelBakery.registerItemVariants(item);
        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return location;
            }
        });
        ModelLoader.setCustomStateMapper(blockFluid, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return location;
            }
        });
    }

    private static void registerBlock(BlockFluidClassicBase blockFluid) {
        ItemBlock itemBlock = new ItemBlock(blockFluid);
        itemBlock.setRegistryName(blockFluid.getRegistryName());
        GameRegistry.register(blockFluid);
        GameRegistry.register(itemBlock);
    }

}
