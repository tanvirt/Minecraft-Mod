package com.quantumindustries.minecraft.mod.multiblock;

import net.minecraft.util.IStringSerializable;

public enum ParticleAcceleratorBlockType implements IStringSerializable {

    BeamSource, Controller, Detector, Input,
    Magnet, Output, Pipe, Power, Target, Wall;

    @Override
    public String getName() {
        return this.toString();
    }

}
