package com.quantumindustries.minecraft.mod;

import com.quantumindustries.minecraft.mod.blocks.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class ModWorldGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
                         IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(dimensionIsOverworld(world)) {
            generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        }
    }

    private void generateOverworld(Random random, int chunkX, int chunkZ, World world,
                                   IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        // TODO: Remove this. This is a test mod item.
        generateOre(
                ModBlocks.oreCopper.getDefaultState(),
                world, random, chunkX * 16, chunkZ * 16,
                16, 64, 4 + random.nextInt(4), 6
        );
    }

    private void generateOre(IBlockState ore, World world, Random random,
                             int x, int z, int minY, int maxY, int size, int chances) {
        int deltaY = maxY - minY;
        for (int i = 0; i < chances; i++) {
            generateBlock(ore, world, random, x, z, minY, size, deltaY);
        }
    }

    private void generateBlock(IBlockState ore, World world, Random random,
                               int x, int z, int minY, int size, int deltaY) {
        BlockPos pos = generateBlockPosition(random, x, z, minY, deltaY);
        WorldGenMinable generator = new WorldGenMinable(ore, size);
        generator.generate(world, random, pos);
    }

    private BlockPos generateBlockPosition(Random random, int x, int z, int minY, int deltaY) {
        return new BlockPos(
                x + random.nextInt(16),
                minY + random.nextInt(deltaY),
                z + random.nextInt(16)
        );
    }

    private boolean dimensionIsOverworld(World world) {
        return world.provider.getDimension() == 0;
    }

}
