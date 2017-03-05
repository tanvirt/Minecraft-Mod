package com.quantumindustries.minecraft.mod.blocks;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.ItemModelProvider;
import com.quantumindustries.minecraft.mod.blocks.counter.BlockCounter;
import com.quantumindustries.minecraft.mod.fluids.BlockFluidNitrogen;
import com.quantumindustries.minecraft.mod.fluids.FluidNitrogen;
import com.quantumindustries.minecraft.mod.items.ItemOreDict;
import com.quantumindustries.minecraft.mod.tileentities.BlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    // TODO: Remove these. These are test objects.
    public static BlockOre oreCopper;
    public static BlockCounter counter;

    public static BlockFluidNitrogen blockFluidNitrogen;

    public static void init() {
        // TODO: Remove these. These are test objects.

        // Register Blocks
        BlockOre ore = new BlockOre("oreCopper", "oreCopper");
        oreCopper = register(ore);

        // Register Tile Entities
        BlockCounter count = new BlockCounter();
        counter = register(count);

        FluidNitrogen fluidNitrogen = new FluidNitrogen();
        FluidRegistry.registerFluid(fluidNitrogen);
        BlockFluidNitrogen blockNitrogen = new BlockFluidNitrogen(fluidNitrogen, "blockFluidNitrogen");
        blockFluidNitrogen = register(blockNitrogen);
        registerFluid(blockFluidNitrogen, "nitrogen");
    }

    public static void registerFluid(BlockFluidClassic blockFluid, String name) {
        Item item = Item.getItemFromBlock(blockFluid);
        ModelResourceLocation location = new ModelResourceLocation(
                CustomMod.MODID + ":fluids",
                name
        );
        ModelBakery.registerItemVariants(item, location);
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

    // Registers blocks and checks what they are instanceof
    // for further registrations.
    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);

        if(itemBlock != null) {
            GameRegistry.register(itemBlock);

            if(block instanceof ItemModelProvider) {
                ((ItemModelProvider) block).registerItemModel(itemBlock);
            }

            if(block instanceof BlockTileEntity) {
                GameRegistry.registerTileEntity(
                        ((BlockTileEntity<?>) block).getTileEntityClass(),
                        block.getRegistryName().toString()
                );
            }

            if(itemBlock instanceof ItemOreDict) {
                ((ItemOreDict) itemBlock).initOreDict();
            }

        }

        return block;
    }

    private static <T extends Block> T register(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return register(block, itemBlock);
    }

}
