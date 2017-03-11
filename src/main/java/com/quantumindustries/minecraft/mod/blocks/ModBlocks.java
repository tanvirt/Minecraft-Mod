package com.quantumindustries.minecraft.mod.blocks;

import com.quantumindustries.minecraft.mod.ItemModelProvider;
import com.quantumindustries.minecraft.mod.blocks.counter.BlockCounter;
import com.quantumindustries.minecraft.mod.blocks.infiniteproducer.BlockInfiniteProducer;
import com.quantumindustries.minecraft.mod.blocks.poweranalyzer.BlockPowerAnalyzer;
import com.quantumindustries.minecraft.mod.items.ItemOreDict;
import com.quantumindustries.minecraft.mod.tileentities.BlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static BlockInfiniteProducer producer;
    public static BlockPowerAnalyzer analyzer;

    public static void init() {
        BlockInfiniteProducer prod = new BlockInfiniteProducer();
        producer = register(prod);
        BlockPowerAnalyzer analyze = new BlockPowerAnalyzer();
        analyzer = register(analyze);
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
