package com.quantumindustries.minecraft.mod.blocks.counter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

// Test class for tile entity counter.
// TODO(CM): remove this class later.
public class TileEntityCounter extends TileEntity {

    private int count;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("count", count);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        count = compound.getInteger("count");
        super.readFromNBT(compound);
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
        markDirty();
    }

    public void decrementCount() {
        count--;
        markDirty();
    }

}
