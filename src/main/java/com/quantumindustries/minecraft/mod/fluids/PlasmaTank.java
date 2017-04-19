package com.quantumindustries.minecraft.mod.fluids;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class PlasmaTank extends FluidTank {

    public PlasmaTank(int amount, int capacity) {
        super(ModFluids.plasma, amount, capacity);
    }

    public void setAmountStored(int amnt) {
        if(fluid != null) {
            fluid.amount = amnt;
        }
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        if(fluid.getFluid() == ModFluids.plasma) {
            return true;
        }
        else {
            return false;
        }
    }
}
