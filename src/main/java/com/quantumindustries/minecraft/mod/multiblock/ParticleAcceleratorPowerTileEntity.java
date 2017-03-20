package com.quantumindustries.minecraft.mod.multiblock;

import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.api.multiblock.validation.ValidationError;

public class ParticleAcceleratorPowerTileEntity extends ParticleAcceleratorTileEntity {

    private static ValidationError s_invalidPosition = new ValidationError(
            "custommod:api.multiblock.validation.powerport_invalid_position");
}
