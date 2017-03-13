package com.quantumindustries.minecraft.mod.blocks;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.ItemModelProvider;
import com.quantumindustries.minecraft.mod.blocks.grinder.BlockGrinder;
import com.quantumindustries.minecraft.mod.blocks.infiniteproducer.BlockInfiniteProducer;
import com.quantumindustries.minecraft.mod.blocks.poweranalyzer.BlockPowerAnalyzer;
import com.quantumindustries.minecraft.mod.items.ItemOreDict;
import com.quantumindustries.minecraft.mod.tileentities.BlockContainerTileEntity;
import com.quantumindustries.minecraft.mod.tileentities.BlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    // BlockOres
    public static BlockOre oreCobalt;
    public static BlockOre oreRhodium;
    public static BlockOre blockCobalt;
    public static BlockOre blockRhodium;

    public static BlockGrinder blockGrinder;

    public static void init() {
        initOres();
        initOreBlocks();
        /*register(new BlockInfiniteProducer());
        register(new BlockPowerAnalyzer());*/

        initBlockGrinder();
    }

    private static void initOres() {
        oreCobalt = register(new BlockOre("oreCobalt", "oreCobalt", 3f, 5f));
        oreRhodium = register(new BlockOre("oreRhodium", "oreRhodium", 3f, 5f));
    }

    private static void initOreBlocks() {
        blockCobalt = register(new BlockOre("blockCobalt", "blockCobalt", 3f, 5f));
        blockRhodium = register(new BlockOre("blockRhodium", "blockRhodium", 3f, 5f));
    }

    private static void initBlockGrinder() {
        blockGrinder = register(new BlockGrinder());
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

            if(block instanceof BlockContainerTileEntity) {
                GameRegistry.registerTileEntity(
                        ((BlockContainerTileEntity<?>) block).getTileEntityClass(),
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
