package com.quantumindustries.minecraft.mod.multiblock;

import net.minecraft.util.IStringSerializable;

public enum ParticleAcceleratorBlockType implements IStringSerializable {
    Detector,
    Magnet,
    Target,
    Controller,
    BeamSource,
    Pipe,
    Power,
    Input,
    Output,
    Wall;

    @Override
    public String getName() {
        return this.toString();
    }
}
