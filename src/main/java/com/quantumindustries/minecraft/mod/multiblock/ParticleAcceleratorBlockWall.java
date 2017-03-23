package com.quantumindustries.minecraft.mod.multiblock;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleAcceleratorBlockWall extends ParticleAcceleratorBlockBase {

    public ParticleAcceleratorBlockWall(String name) {
        super(name, ParticleAcceleratorBlockType.Wall);
    }

//    @SideOnly(Side.CLIENT)
//    public EnumBlockRenderType blockRenderType() {
//        return EnumBlockRenderType.valueOf();
//    }
    public boolean isVisuallyOpaque() {
        return false;
    }
}
