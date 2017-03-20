package com.quantumindustries.minecraft.mod.multiblock;

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

        if (data.hasKey("paIOdir"))
            this.isInput = data.getBoolean("paIOdir");
    }

    @Override
    protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {

        super.syncDataTo(data, syncReason);
        data.setBoolean("paIOdir", this.isInput);
    }
}
