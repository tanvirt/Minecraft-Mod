package com.quantumindustries.minecraft.mod.blocks.infiniteproducer;

import com.quantumindustries.minecraft.mod.tileentities.BlockTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockInfiniteProducer extends BlockTileEntity<TileEntityInfiniteProducer> {

    public BlockInfiniteProducer() {
        super(Material.ROCK, "infiniteProducer");
    }

    @Override
    public Class<TileEntityInfiniteProducer> getTileEntityClass() {
        return TileEntityInfiniteProducer.class;
    }

    @Nullable
    @Override
    public TileEntityInfiniteProducer createTileEntity(World world, IBlockState state) {
        return new TileEntityInfiniteProducer();
    }

}
