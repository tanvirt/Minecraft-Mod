package com.quantumindustries.minecraft.mod.util;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class BaseMachineContainer implements ITeslaConsumer, ITeslaHolder, ITeslaProducer, INBTSerializable<NBTTagCompound> {

    /**
     * The amount of stored Tesla power.
     */
    private long stored;

    /**
     * The maximum amount of Tesla power that can be stored.
     */
    private long capacity;

    /**
     * The maximum amount of Tesla power that can be accepted.
     */
    private long inputRate;

    /**
     * The maximum amount of Tesla power that can be extracted
     */
    private long outputRate;

    /**
     * Default constructor. Sets capacity to 5000 and transfer rate to 50. This constructor
     * will not set the amount of stored power. These values are arbitrary and should not be
     * taken as a base line for balancing.
     */
    public BaseMachineContainer() {
        this(0, 0, 0);
    }

    /**
     * Constructor for setting the basic values. Will not construct with any stored power.
     *
     * @param capacity The maximum amount of Tesla power that the container should hold.
     * @param input The maximum rate of power that can be accepted at a time.
     * @param output The maximum rate of power that can be extracted at a time.
     */
    public BaseMachineContainer(long capacity, long input, long output) {
        this(0, capacity, input, output);
    }

    /**
     * Constructor for setting all of the base values, including the stored power.
     *
     * @param stored The amount of stored power to initialize the container with.
     * @param capacity The maximum amount of Tesla power that the container should hold.
     * @param inputRate The maximum rate of power that can be accepted at a time.
     * @param outputRate The maximum rate of power that can be extracted at a time.
     */
    public BaseMachineContainer(long stored, long capacity, long inputRate, long outputRate) {
        this.stored = stored;
        this.capacity = capacity;
        this.inputRate = inputRate;
        this.outputRate = outputRate;
    }

    /**
     * Constructor for creating an instance directly from a compound tag. This expects that the
     * compound tag has some of the required data. @See {@link #deserializeNBT(NBTTagCompound)}
     * for precise info on what is expected. This constructor will only set the stored power if
     * it has been written on the compound tag.
     *
     * @param dataTag The NBTCompoundTag to read the important data from.
     */
    public BaseMachineContainer(NBTTagCompound dataTag) {
        deserializeNBT(dataTag);
    }

    @Override
    public long getStoredPower () {
        return stored;
    }

    public void setStoredPower(long power) {
        stored = power;
    }

    @Override
    public long givePower (long Tesla, boolean simulated) {
        final long acceptedTesla = Math.min(getCapacity() - stored, Math.min(getInputRate(), Tesla));

        if (!simulated) {
            stored += acceptedTesla;
        }
        return acceptedTesla;
    }

    @Override
    public long takePower (long Tesla, boolean simulated) {
        final long removedPower = Math.min(stored, Math.min(this.getOutputRate(), Tesla));

        if (!simulated) {
            stored -= removedPower;
        }
        return removedPower;
    }

    @Override
    public long getCapacity () {
        return capacity;
    }

    @Override
    public NBTTagCompound serializeNBT () {
        final NBTTagCompound dataTag = new NBTTagCompound();
        dataTag.setLong("TeslaPower", stored);
        dataTag.setLong("TeslaCapacity", capacity);
        dataTag.setLong("TeslaInput", inputRate);
        dataTag.setLong("TeslaOutput", outputRate);

        return dataTag;
    }

    @Override
    public void deserializeNBT (NBTTagCompound nbt) {
        stored = nbt.getLong("TeslaPower");

        if (nbt.hasKey("TeslaCapacity")) {
            capacity = nbt.getLong("TeslaCapacity");
        }
        if (nbt.hasKey("TeslaInput")) {
            inputRate = nbt.getLong("TeslaInput");
        }
        if (nbt.hasKey("TeslaOutput")) {
            outputRate = nbt.getLong("TeslaOutput");
        }
        if (stored > getCapacity()) {
            stored = getCapacity();
        }
    }

    /**
     * Sets the capacity of the the container. If the existing stored power is more than the
     * new capacity, the stored power will be decreased to match the new capacity.
     *
     * @param capacity The new capacity for the container.
     * @return The instance of the container being updated.
     */
    public BaseMachineContainer setCapacity (long capacity) {
        this.capacity = capacity;

        if (stored > capacity) {
            stored = capacity;
        }
        return this;
    }

    /**
     * Gets the maximum amount of Tesla power that can be accepted by the container.
     *
     * @return The amount of Tesla power that can be accepted at any time.
     */
    public long getInputRate () {
        return inputRate;
    }

    /**
     * Sets the maximum amount of Tesla power that can be accepted by the container.
     *
     * @param rate The amount of Tesla power to accept at a time.
     * @return The instance of the container being updated.
     */
    public BaseMachineContainer setInputRate (long rate) {
        inputRate = rate;
        return this;
    }

    /**
     * Gets the maximum amount of Tesla power that can be pulled from the container.
     *
     * @return The amount of Tesla power that can be extracted at any time.
     */
    public long getOutputRate () {
        return outputRate;
    }

    /**
     * Sets the maximum amount of Tesla power that can be pulled from the container.
     *
     * @param rate The amount of Tesla power that can be extracted.
     * @return The instance of the container being updated.
     */
    public BaseMachineContainer setOutputRate (long rate) {
        outputRate = rate;
        return this;
    }

    /**
     * Sets both the input and output rates of the container at the same time. Both rates will
     * be the same.
     *
     * @param rate The input/output rate for the Tesla container.
     * @return The instance of the container being updated.
     */
    public BaseMachineContainer setTransferRate (long rate) {
        setInputRate(rate);
        setOutputRate(rate);
        return this;
    }

    public long getField(int id)
    {
        switch (id) {
            case 0:
                return capacity;
            case 1:
                return stored;
            default:
                return 0;
        }
    }

    public void setField(int id, long value)
    {
        switch (id) {
            case 0:
                capacity = value;
                break;
            case 1:
                stored = value;
        }
    }
}
