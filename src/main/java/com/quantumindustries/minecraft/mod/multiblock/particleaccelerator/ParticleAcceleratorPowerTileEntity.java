package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import com.quantumindustries.minecraft.mod.util.BaseMachineContainer;
import javafx.beans.InvalidationListener;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLLog;

public class ParticleAcceleratorPowerTileEntity extends ParticleAcceleratorTileEntity {

    // An instance of something that implements ITeslaConsumer, ITeslaProducer or
    // ITeslaHandler. In this case we use the BaseTeslaContainer which makes use of all three.
    // The purpose of this instance is to handle all tesla related logic for the TileEntity.
    private BaseMachineContainer container;

    public ParticleAcceleratorPowerTileEntity() {
        // Initializes the container. Very straight forward.
        this.container = new BaseMachineContainer();
    }

    public void setNewContainer(long capacity, long rate) {
        container = new BaseMachineContainer(capacity, rate, rate);
    }

    public void setNewContainer(long power, long capacity, long input) {
        container = new BaseMachineContainer(power, capacity, input, input);
    }

    public void setContainer(BaseMachineContainer container) {
        this.container = container;
        notifyUpdate();
    }

    public BaseMachineContainer getContainer() {
        return container;
    }

    public void setCapacity(long power) {
        container.setCapacity(power);
        notifyUpdate();
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
        long powerConsumed = container.takePower(power, false);
        notifyUpdate();
        return powerConsumed;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        FMLLog.warning("Reading data from NBT Power: %s", compound.toString());
        super.readFromNBT(compound);
        // It is important for the power being stored to be persistent. The BaseTeslaContainer
        // includes a method to make reading one from a compound tag very easy. This method is
        // completely optional though, you can handle saving however you prefer. You could even
        // choose not to, but then power won't be saved when you close the game.
        this.container = new BaseMachineContainer(compound.getCompoundTag("PowerPortContainer"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        FMLLog.warning("Writing data to NBT Power: %s", compound.toString());
        // It is important for the power being stored to be persistent. The BaseTeslaContainer
        // includes a method to make writing one to a compound tag very easy. This method is
        // completely optional though, you can handle saving however you prefer. You could even
        // choose not to, but then power won't be saved when you close the game.
        compound.setTag("PowerPortContainer", this.container.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        // This method is where other things will try to access your TileEntity's Tesla
        // capability. In the case of the analyzer, is a consumer, producer and holder so we
        // can allow requests that are looking for any of those things. This example also does
        // not care about which side is being accessed, however if you wanted to restrict which
        // side can be used, for example only allow power input through the back, that could be
        // done here.
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER ||
                capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return (T) container;
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        // This method replaces the instanceof checks that would be used in an interface based
        // system. It can be used by other things to see if the TileEntity uses a capability or
        // not. This example is a Consumer, Producer and Holder, so we return true for all
        // three. This can also be used to restrict access on certain sides, for example if you
        // only accept power input from the bottom of the block, you would only return true for
        // Consumer if the facing parameter was down.
        return capability == TeslaCapabilities.CAPABILITY_CONSUMER ||
                capability == TeslaCapabilities.CAPABILITY_HOLDER ||
                super.hasCapability(capability, facing);
    }

    public long getField(int id)
    {
        switch (id)
        {
            case 0:
                return container.getCapacity();
            case 1:
                return container.getStoredPower();
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                container.setCapacity(value);
                break;
            case 1:
                container.setStoredPower(value);
        }
    }

}
