package com.quantumindustries.minecraft.mod.blocks.infiniteproducer;

import net.darkhax.tesla.api.implementation.InfiniteTeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.util.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityInfiniteProducer extends TileEntity implements ITickable{
    /*
     *  Tile Entity for testing, produces infinite power on all sides every tick.
     */

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == TeslaCapabilities.CAPABILITY_PRODUCER)
            return (T) new InfiniteTeslaProducer();

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == TeslaCapabilities.CAPABILITY_PRODUCER ||
                super.hasCapability(capability, facing);
    }

    @Override
    public void update() {
        TeslaUtils.distributePowerToAllFaces(this.worldObj, this.pos, 50, false);
    }

}
