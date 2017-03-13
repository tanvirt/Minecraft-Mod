package com.quantumindustries.minecraft.mod.blocks;

import com.quantumindustries.minecraft.mod.ItemModelProvider;
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
    public static BlockOre oreNeodymium;
    public static BlockOre oreRhodium;
    public static BlockOre blockCobalt;
    public static BlockOre blockNeodymium;
    public static BlockOre blockRhodium;

    // BlockBase
    public static BlockBase blockNeoIronMagnet;
    public static BlockBase blockNeoCobaltMagnet;
    public static BlockBase blockNeoRhodiumMagnet;

    public static void init() {
        initMagnetBlocks();
        initOres();
        initOreBlocks();
        register(new BlockInfiniteProducer());
        register(new BlockPowerAnalyzer());
    }

    private static void initMagnetBlocks() {
        blockNeoIronMagnet = register(new BlockBase(Material.ROCK, "blockNeoIronMagnet"));
        blockNeoCobaltMagnet = register(new BlockBase(Material.ROCK, "blockNeoCobaltMagnet"));
        blockNeoRhodiumMagnet = register(new BlockBase(Material.ROCK, "blockNeoRhodiumMagnet"));
    }

    // TODO(CM): Change hardness and resistances to have more variance
    // TODO(CM): Long term goal of config options to turn off/on ore registration for each ore and its variants.
    private static void initOres() {
        oreCobalt = register(new BlockOre("oreCobalt", "oreCobalt", 3f, 5f));
        oreNeodymium = register(new BlockOre("oreNeodymium", "oreNeodymium", 3f, 5f));
        oreRhodium = register(new BlockOre("oreRhodium", "oreRhodium", 3f, 5f));
    }

    // TODO(CM): Change hardness and resistances to have more variance
    private static void initOreBlocks() {
        blockCobalt = register(new BlockOre("blockCobalt", "blockCobalt", 3f, 5f));
        blockNeodymium = register(new BlockOre("blockNeodymium", "blockNeodymium", 3f, 5f));
        blockRhodium = register(new BlockOre("blockRhodium", "blockRhodium", 3f, 5f));
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
