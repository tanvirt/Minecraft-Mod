package com.quantumindustries.minecraft.mod.multiblock;

public class ParticleAcceleratorBlockPort extends ParticleAcceleratorBlockBase {

    public ParticleAcceleratorBlockPort(String name, ParticleAcceleratorBlockType portType) {
        super(name, portType);

        if (ParticleAcceleratorBlockType.Wall == portType)
            throw new IllegalArgumentException("Invalid port type");
    }
}
