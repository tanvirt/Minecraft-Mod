package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import com.quantumindustries.minecraft.mod.util.BaseMachineContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class ParticleAcceleratorPowerTileEntity extends ParticleAcceleratorTileEntity {

    private BaseMachineContainer container;

    public ParticleAcceleratorPowerTileEntity() {
        container = new BaseMachineContainer();
    }

    public void setNewContainer(long capacity, long rate) {
        container = new BaseMachineContainer(capacity, rate, rate);
    }

    public void setNewContainer(long power, long capacity, long input) {
        container = new BaseMachineContainer(power, capacity, input, input);
    }

    public void setContainer(BaseMachineContainer container) {
        this.container = container;
    }

    public BaseMachineContainer getContainer() {
        return container;
    }

    public void setCapacity(long power) {
        container.setCapacity(power);
    }

    public long getCapacity() {
        return container.getCapacity();
    }

    public void setInputRate(long rate) {
        container.setInputRate(rate);
    }

    public long getInputRate() {
        return container.getInputRate();
    }

    public void setOutputRate(long rate) {
        container.setOutputRate(rate);
    }

    public long getOutputRate() {
        return container.getOutputRate();
    }

    public long getCurrentPower() {
        return container.getStoredPower();
    }

    public long consumePower(long power) {
        return container.takePower(power, false);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        container = new BaseMachineContainer(compound.getCompoundTag("PowerPortContainer"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("PowerPortContainer", container.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER ||
                capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return (T) container;
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == TeslaCapabilities.CAPABILITY_CONSUMER ||
                capability == TeslaCapabilities.CAPABILITY_HOLDER ||
                super.hasCapability(capability, facing);
    }

}
