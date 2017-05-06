package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.util.INBTSerializable;

public enum ParticleAcceleratorBeamType implements IStringSerializable {

    PROTON, ION, NEUTRON, ELECTRON;

    @Override
    public String getName() {
        return this.toString();
    }

    public int getOrdinalType() {
        switch(this) {
            case PROTON:
                return 0;
            case ION:
                return 1;
            case NEUTRON:
                return 2;
            case ELECTRON:
                return 3;
            default:
                return -1;
        }
    }

    public ParticleAcceleratorBeamType setOrdinalType(int value) {
        switch(value) {
            case 0:
                return PROTON;
            case 1:
                return ION;
            case 2:
                return NEUTRON;
            case 3:
                return ELECTRON;
            default:
                return null;
        }
    }

    public ParticleAcceleratorBeamType cycleBeam() {
        switch(this) {
            case PROTON:
                return ION;
            case ION:
                return NEUTRON;
            case NEUTRON:
                return ELECTRON;
            case ELECTRON:
                return PROTON;
            default:
                return null;
        }
    }
}
