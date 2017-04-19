package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import net.minecraft.util.IStringSerializable;

public enum ParticleAcceleratorBeamType implements IStringSerializable{

    PROTON, ION, NEUTRON, ELECTRON;

    @Override
    public String getName() {
        return this.toString();
    }
}
