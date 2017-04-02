package com.quantumindustries.minecraft.mod.blocks;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.ItemModelProvider;
import com.quantumindustries.minecraft.mod.blocks.infiniteproducer.BlockInfiniteProducer;
import com.quantumindustries.minecraft.mod.blocks.poweranalyzer.BlockPowerAnalyzer;
import com.quantumindustries.minecraft.mod.items.ItemOreDict;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.*;
import com.quantumindustries.minecraft.mod.tileentities.BlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
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

    // Particle Accelerator Blocks
    public static ParticleAcceleratorBlockWall blockAcceleratorWall;
    public static ParticleAcceleratorBlockPort blockAcceleratorPowerPort;
    public static ParticleAcceleratorBlockPort blockAcceleratorInputPort;
    public static ParticleAcceleratorBlockPort blockAcceleratorOutputPort;
    public static ParticleAcceleratorBlockBeamPipe blockAcceleratorBeamPipe;
    public static ParticleAcceleratorBlockDetector blockAcceleratorDetector;
    public static ParticleAcceleratorBlockMagnet blockAcceleratorMagnet;
    public static ParticleAcceleratorBlockTarget blockAcceleratorTarget;
    public static ParticleAcceleratorBlockController blockAcceleratorController;
    public static ParticleAcceleratorBlockBeamSource blockAcceleratorBeamSource;

    public static void init() {
        initMagnetBlocks();
        initOres();
        initOreBlocks();
        initParticleAcceleratorBlocks();
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

    private static void initParticleAcceleratorBlocks() {
        blockAcceleratorWall = register(new ParticleAcceleratorBlockWall("particleAcceleratorCasing"));
        blockAcceleratorPowerPort = register(new ParticleAcceleratorBlockPort(
                "particleAcceleratorPowerPort",
                ParticleAcceleratorBlockType.Power)
        );
        blockAcceleratorInputPort = register(new ParticleAcceleratorBlockPort(
                "particleAcceleratorInputPort",
                ParticleAcceleratorBlockType.Input)
        );
        blockAcceleratorOutputPort = register(new ParticleAcceleratorBlockPort(
                "particleAcceleratorOutputPort",
                ParticleAcceleratorBlockType.Output)
        );
        blockAcceleratorBeamPipe = register(new ParticleAcceleratorBlockBeamPipe(
                "particleAcceleratorBeamPipe",
                ParticleAcceleratorBlockType.Pipe)
        );
        blockAcceleratorController = register(new ParticleAcceleratorBlockController(
                "particleAcceleratorController",
                ParticleAcceleratorBlockType.Controller)
        );
        blockAcceleratorDetector = register(new ParticleAcceleratorBlockDetector(
                "particleAcceleratorDetector",
                ParticleAcceleratorBlockType.Detector)
        );
        blockAcceleratorMagnet = register(new ParticleAcceleratorBlockMagnet(
                "particleAcceleratorMagnet",
                ParticleAcceleratorBlockType.Magnet)
        );
        blockAcceleratorTarget = register(new ParticleAcceleratorBlockTarget(
                "particleAcceleratorTarget",
                ParticleAcceleratorBlockType.Target)
        );
        blockAcceleratorBeamSource = register(new ParticleAcceleratorBlockBeamSource(
                "particleAcceleratorBeamSource",
                ParticleAcceleratorBlockType.BeamSource)
        );
        register(ParticleAcceleratorTileEntity.class);
        register(ParticleAcceleratorPowerTileEntity.class);
        register(ParticleAcceleratorIOPortTileEntity.class);
        register(ParticleAcceleratorControllerTileEntity.class);
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

    private static void register(Class<? extends TileEntity> tileEntityClass) {
        GameRegistry.registerTileEntity(tileEntityClass, CustomMod.MODID + tileEntityClass.getSimpleName());
    }

    private static <T extends Block> T register(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return register(block, itemBlock);
    }

}
