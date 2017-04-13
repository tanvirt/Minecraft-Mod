package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) getAcceleratorController().getController().getPlasmaTank();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

}
