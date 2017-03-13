package com.quantumindustries.minecraft.mod.fluids;

import com.quantumindustries.minecraft.mod.CustomMod;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidClassicBase extends BlockFluidClassic {

    public BlockFluidClassicBase(String name, Fluid fluid, Material material) {
        super(fluid, material);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CustomMod.tab);
    }

    @Override
    public BlockFluidClassicBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}
