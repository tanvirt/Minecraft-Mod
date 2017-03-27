package com.quantumindustries.minecraft.mod.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleAcceleratorBlockWall extends ParticleAcceleratorBlockBase {

    public ParticleAcceleratorBlockWall(String name) {
        super(name, ParticleAcceleratorBlockType.Wall);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
