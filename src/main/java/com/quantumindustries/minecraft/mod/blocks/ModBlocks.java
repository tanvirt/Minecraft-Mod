package com.quantumindustries.minecraft.mod.blocks;

import com.quantumindustries.minecraft.mod.ItemModelProvider;
import com.quantumindustries.minecraft.mod.blocks.counter.BlockCounter;
import com.quantumindustries.minecraft.mod.items.ItemOreDict;
import com.quantumindustries.minecraft.mod.tileentities.BlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    /*// TODO: Remove these. These are test objects.
    public static BlockOre oreCopper;
    public static BlockCounter counter;*/

    public static void init() {
        /*// TODO: Remove these. These are test objects.

        // Register Blocks
        BlockOre ore = new BlockOre("oreCopper", "oreCopper");
        oreCopper = register(ore);

        // Register Tile Entities
        BlockCounter count = new BlockCounter();
        counter = register(count);*/
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
