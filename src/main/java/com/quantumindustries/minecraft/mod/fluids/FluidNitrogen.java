package com.quantumindustries.minecraft.mod.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.awt.*;

public class FluidNitrogen extends Fluid {

    public FluidNitrogen() {
        super(
                "fluid_nitrogen",
                new ResourceLocation("blocks/water_still"),
                new ResourceLocation("blocks/water_flow")
        );
    }

    @Override
    public int getColor() {
        return new Color(0, 255, 2).getRGB();
    }

}
