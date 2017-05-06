package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import com.quantumindustries.minecraft.mod.fluids.PlasmaTank;
import com.quantumindustries.minecraft.mod.recipes.ParticleAcceleratorRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ParticleAcceleratorControllerTileEntity extends ParticleAcceleratorTileEntity {
    // TODO(CM): Either fix empty class or format to show we aren't using it.

    public static final int SIZE = 2;
    private CurrentAcceleration currentAcceleration = new CurrentAcceleration();
    // TODO(CM): Balance tank capacity
    private PlasmaTank plasmaTank = new PlasmaTank(0, 10000);
    private AcceleratorBeams acceleratorBeams = new AcceleratorBeams();
//    private ParticleAcceleratorBeamType leftBeam = ParticleAcceleratorBeamType.PROTON;
//    private ParticleAcceleratorBeamType rightBeam = ParticleAcceleratorBeamType.PROTON;

    // This item handler will hold our inventory slots
    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            markDirty();
        }
    };

    public PlasmaTank getPlasmaTank() {
        return plasmaTank;
    }

    public ParticleAcceleratorBeamType getLeftBeam() {
        return acceleratorBeams.leftBeam;
    }

    public ParticleAcceleratorBeamType getRightBeam() {
        return acceleratorBeams.rightBeam;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        if(compound.hasKey("currentAcceleration")) {
            currentAcceleration.deserializeNBT((NBTTagCompound) compound.getTag("currentAcceleration"));
        }
        if(compound.hasKey("acceleratorBeams")) {
            acceleratorBeams.deserializeNBT((NBTTagCompound) compound.getTag("acceleratorBeams"));
        }
        plasmaTank.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
        compound.setTag("currentAcceleration", currentAcceleration.serializeNBT());
        compound.setTag("acceleratorBeams", acceleratorBeams.serializeNBT());
        plasmaTank.writeToNBT(compound);
        return compound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemStackHandler;
        }
        return super.getCapability(capability, facing);
    }

    // -----------------------------------------------------------------
    // ItemStack Inventory Functions
    // -----------------------------------------------------------------

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean canAccelerate(long maxPowerRate){
        if(itemStackHandler.getStackInSlot(GuiSlots.INPUT) == null) {
            resetCurrentAcceleration();
            return false;
        }
        if(plasmaTank.getFluidAmount() == 0) {
            return false;
        }
        else {
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(GuiSlots.INPUT);
            ItemStack itemstack = ParticleAcceleratorRecipes.instance().getAcceleratingResult(stackInSlot);
            long powerRateRequired = ParticleAcceleratorRecipes.instance().getAcceleratingRatePowerRequirement(stackInSlot);
            if(itemstack == null) {
                return false;
            }
            else if(powerRateRequired > maxPowerRate) {
                return false;
            }
            // TODO(CM): 2000 means 2 buckets of plasma, instead of hardcode maybe dynamic? plasma req. in recipes?
            else if(2000 > plasmaTank.getFluidAmount()) {
                return false;
            }
            // TODO(CM): Check power and plasma as well.
            if(itemStackHandler.getStackInSlot(GuiSlots.OUTPUT) == null) {
                return true;
            }
            if(!itemStackHandler.getStackInSlot(GuiSlots.OUTPUT).isItemEqual(itemstack)) {
                return false;
            }
            int result = itemStackHandler.getStackInSlot(GuiSlots.OUTPUT).stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= itemStackHandler.getStackInSlot(GuiSlots.OUTPUT).getMaxStackSize();
        }
    }

    public void acceleratingItem(ParticleAcceleratorPowerTileEntity powerPort) {
        //If not Active, we are starting a new acceleration
        if(!currentAcceleration.isAccelerating) {
            getAcceleratorController().setActive(true);
            setNewAcceleration(ParticleAcceleratorRecipes.instance().getAcceleratingTotalPowerRequirement(itemStackHandler.getStackInSlot(GuiSlots.INPUT)));
        }
        else if(canAccelerate(powerPort.getInputRate())){
            if(currentAcceleration.totalPowerNeeded > currentAcceleration.totalPowerUsed) {
                long totalPowerStillNeeded = currentAcceleration.totalPowerNeeded - currentAcceleration.totalPowerUsed;
                currentAcceleration.totalPowerUsed += powerPort.consumePower(totalPowerStillNeeded);
            }
            else if(currentAcceleration.totalPowerNeeded <= currentAcceleration.totalPowerUsed) {
                finishAcceleration();
            }
        }
        else {
            resetCurrentAcceleration();
            getAcceleratorController().setActive(false);
        }
    }

    public void finishAcceleration() {
        int INPUT = 0;
        int OUTPUT = 1;
        ItemStack inputStack = itemStackHandler.getStackInSlot(INPUT);

        ItemStack itemStack = ParticleAcceleratorRecipes.instance().getAcceleratingResult(inputStack);

        if(itemStackHandler.getStackInSlot(OUTPUT) == null) {
            itemStackHandler.insertItem(
                    OUTPUT,
                    itemStack.copy(),
                    false
            );
        }
        else if(itemStackHandler.getStackInSlot(OUTPUT).getItem() == itemStack.getItem()) {
            itemStackHandler.insertItem(OUTPUT, itemStack.copy(), false);
        }

        // TODO(CM): Change this value to not be hard coded?
        plasmaTank.drain(2000, true);
        itemStackHandler.extractItem(INPUT, 1, false);
        resetCurrentAcceleration();
        getAcceleratorController().setActive(false);
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public void setNewAcceleration(long powerNeeded) {
        currentAcceleration.totalPowerUsed = 0;
        currentAcceleration.totalPowerNeeded = powerNeeded;
        currentAcceleration.isAccelerating = true;
    }

    public void resetCurrentAcceleration() {
        currentAcceleration = new CurrentAcceleration();
    }

    public void cycleLeftBeam() {
        acceleratorBeams.cycleLeftBeam();
    }

    public void cycleRightBeam() {
        acceleratorBeams.cycleRightBeam();
    }

    public class GuiSlots {
        static final int INPUT = 0;
        static final int OUTPUT = 1;
    }

    public class AcceleratorBeams implements INBTSerializable<NBTTagCompound> {

        ParticleAcceleratorBeamType leftBeam = ParticleAcceleratorBeamType.PROTON;
        ParticleAcceleratorBeamType rightBeam = ParticleAcceleratorBeamType.PROTON;

        public void cycleLeftBeam() {
            leftBeam = leftBeam.cycleBeam();
            FMLLog.warning("Left Beam: %s", leftBeam.toString());
        }

        public void cycleRightBeam() {
            rightBeam = rightBeam.cycleBeam();
            FMLLog.warning("Right Beam: %s", rightBeam.toString());
        }

        @Override
        public NBTTagCompound serializeNBT() {
            final NBTTagCompound dataTag = new NBTTagCompound();
            dataTag.setInteger("leftBeam", leftBeam.getOrdinalType());
            dataTag.setInteger("rightBeam", rightBeam.getOrdinalType());

            return dataTag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            if(nbt.hasKey("leftBeam")) {
                leftBeam.setOrdinalType(nbt.getInteger("leftBeam"));
            }
            if(nbt.hasKey("rightBeam")) {
                rightBeam.setOrdinalType(nbt.getInteger("rightBeam"));
            }
        }
    }

    public class CurrentAcceleration implements  INBTSerializable<NBTTagCompound> {
        long totalPowerUsed = 0;
        long totalPowerNeeded = 0;
        boolean isAccelerating = false;

        @Override
        public NBTTagCompound serializeNBT() {
            final NBTTagCompound dataTag = new NBTTagCompound();
            dataTag.setLong("totalPowerUsed", totalPowerUsed);
            dataTag.setLong("totalPowerNeeded", totalPowerNeeded);
            dataTag.setBoolean("isAccelerating", isAccelerating);

            return dataTag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            if (nbt.hasKey("totalPowerUsed")) {
                currentAcceleration.totalPowerUsed = nbt.getLong("totalPowerUsed");
            }
            if (nbt.hasKey("totalPowerNeeded")) {
                currentAcceleration.totalPowerNeeded = nbt.getLong("totalPowerNeeded");
            }
            if (nbt.hasKey("isAccelerating")) {
                currentAcceleration.isAccelerating = nbt.getBoolean("isAccelerating");
            }
        }
    }

    public long getField(int id) {
        switch(id) {
            case 0:
                return getAcceleratorController().getPowerPort().getCapacity();
            case 1:
                return getAcceleratorController().getPowerPort().getCurrentPower();
            case 2:
                return plasmaTank.getCapacity();
            case 3:
                return plasmaTank.getFluidAmount();
            case 4:
                return acceleratorBeams.leftBeam.getOrdinalType();
            case 5:
                return acceleratorBeams.rightBeam.getOrdinalType();
            default:
                return 0;
        }
    }

    public FluidStack getFieldFluid(int id) {
        switch(id) {
            case 4:
                return plasmaTank.getFluid();
            default:
                return null;
        }
    }

    public void setField(int id, long value) {
        switch (id) {
            case 0:
                getAcceleratorController().getPowerPort().setCapacity(value);
                break;
            case 1:
                getAcceleratorController().getPowerPort().setCurrentPower(value);
                break;
            case 2:
                plasmaTank.setCapacity((int) value);
                break;
            case 3:
                plasmaTank.setAmountStored((int) value);
                break;
            case 4:
                acceleratorBeams.leftBeam.setOrdinalType((int) value);
                break;
            case 5:
                acceleratorBeams.rightBeam.setOrdinalType((int) value);
                break;
        }
    }

    public void setFieldFluid(int id, FluidStack fluid) {
        switch (id) {
            case 4:
                plasmaTank.setFluid(fluid);
                break;
        }
    }

}
