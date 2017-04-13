package com.quantumindustries.minecraft.mod.fluids;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class PlasmaTank extends FluidTank {

    public PlasmaTank(int capacity) {
        super(capacity);
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        if(fluid.getFluid() == ModFluids.plasma) {
            return true;
        }
        else
            return false;
    }
}
