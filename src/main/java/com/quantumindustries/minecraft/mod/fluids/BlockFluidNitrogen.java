package com.quantumindustries.minecraft.mod.fluids;

import com.quantumindustries.minecraft.mod.CustomMod;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockFluidNitrogen extends BlockFluidClassic {

    public BlockFluidNitrogen(String name) {
        super(createNitrogenFluid(), Material.WATER);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CustomMod.tab);
    }

    private static Fluid createNitrogenFluid() {
        Fluid fluid = new Fluid(
                "fluid_nitrogen",
                new ResourceLocation(CustomMod.MODID + ":block/nitrogen_still"),
                new ResourceLocation(CustomMod.MODID + ":block/nitrogen_flow")
        );
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }

}
