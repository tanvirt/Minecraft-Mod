package com.quantumindustries.minecraft.mod.blocks;

import com.quantumindustries.minecraft.mod.ItemModelProvider;
import com.quantumindustries.minecraft.mod.blocks.counter.BlockCounter;
import com.quantumindustries.minecraft.mod.blocks.infiniteproducer.BlockInfiniteProducer;
import com.quantumindustries.minecraft.mod.blocks.poweranalyzer.BlockPowerAnalyzer;
import com.quantumindustries.minecraft.mod.items.ItemOreDict;
import com.quantumindustries.minecraft.mod.tileentities.BlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    // BlockOres
    public static BlockOre oreCobalt;
    public static BlockOre oreRhodium;
    public static BlockOre blockCobalt;
    public static BlockOre blockRhodium;

    // TODO(CM): Separate out different registers into functions (register ores, blocks, etc.)
    public static void init() {
        oreCobalt = register(new BlockOre("oreCobalt", "oreCobalt", 3f, 5f));
        oreRhodium = register(new BlockOre("oreRhodium", "oreRhodium", 3f, 5f));
        blockCobalt = register(new BlockOre("blockCobalt", "blockCobalt", 3f, 5f));
        blockRhodium = register(new BlockOre("blockRhodium", "blockRhodium", 3f, 5f));
        register(new BlockInfiniteProducer());
        register(new BlockPowerAnalyzer());
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
