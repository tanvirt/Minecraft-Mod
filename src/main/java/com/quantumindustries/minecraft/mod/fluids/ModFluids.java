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
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModFluids {

    public static void init() {
        initNitrogen();
        initOxygen();
        initArgon();
    }

    private static void initNitrogen() {
        String fluidName = "nitrogen";
        Fluid fluid = createFluid(fluidName);
        BlockFluidClassicBase block = createBlock(
                "blockFluidNitrogen",
                fluidName,
                fluid,
                Material.WATER
        );
        ItemBucket nitrogenBucket = new ItemBucketBase("itemBucketNitrogen", block);
        GameRegistry.register(nitrogenBucket);
    }

    private static void initOxygen() {
        String fluidName = "oxygen";
        Fluid fluid = createFluid(fluidName);
        createBlock("blockFluidOxygen", fluidName, fluid, Material.WATER);
    }

    private static void initArgon() {
        String fluidName = "argon";
        Fluid fluid = createFluid(fluidName);
        createBlock("blockFluidArgon", fluidName, fluid, Material.WATER);
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

    private static BlockFluidClassicBase createBlock(String blockName, String fluidName,
                                    Fluid fluid, Material material) {
        BlockFluidClassicBase block = new BlockFluidClassicBase(
                blockName,
                fluid,
                material
        );
        registerBlock(block);
        registerFluid(block, fluidName);
        return block;
    }

    private static void registerBlock(BlockFluidClassicBase block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);
    }

    private static void registerFluid(BlockFluidClassicBase block, String name) {
        Item item = Item.getItemFromBlock(block);
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
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return location;
            }
        });
    }

}
