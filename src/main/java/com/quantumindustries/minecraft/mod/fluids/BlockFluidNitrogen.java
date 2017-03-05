package com.quantumindustries.minecraft.mod.fluids;

import com.quantumindustries.minecraft.mod.CustomMod;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidNitrogen extends BlockFluidClassic {

    public BlockFluidNitrogen(Fluid fluid, String name) {
        super(fluid, Material.WATER);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CustomMod.tab);
    }

}
