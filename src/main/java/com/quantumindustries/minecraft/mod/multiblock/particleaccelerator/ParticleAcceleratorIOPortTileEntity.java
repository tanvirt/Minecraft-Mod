package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import net.minecraft.nbt.NBTTagCompound;

public class ParticleAcceleratorIOPortTileEntity extends ParticleAcceleratorTileEntity {

    protected boolean isInput;

    public ParticleAcceleratorIOPortTileEntity(boolean isInput) {
        this.isInput = isInput;
    }

    public ParticleAcceleratorIOPortTileEntity() {
        this.isInput = false;
    }

    public boolean isInput() {
        return this.isInput;
    }

    @Override
    protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
        super.syncDataFrom(data, syncReason);

        if(data.hasKey("isInputAcceleratorIOPort")) {
            this.isInput = data.getBoolean("isInputAcceleratorIOPort");
        }
    }

    @Override
    protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
        super.syncDataTo(data, syncReason);
        data.setBoolean("isInputAcceleratorIOPort", this.isInput);
    }

}
