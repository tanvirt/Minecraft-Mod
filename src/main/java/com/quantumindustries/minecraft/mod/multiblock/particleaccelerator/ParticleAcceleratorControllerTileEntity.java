package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import com.quantumindustries.minecraft.mod.recipes.ParticleAcceleratorRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ParticleAcceleratorControllerTileEntity extends ParticleAcceleratorTileEntity {
    // TODO(CM): Either fix empty class or format to show we aren't using it.

    public static final int SIZE = 2;
    public CurrentAcceleration currentAcceleration = new CurrentAcceleration();
    public long maxPowerStorage = 1000;
    public long currentPowerStorage = 500;

    // This item handler will hold our inventory slots
    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            markDirty();
        }
    };

    public void setPowerContents(long max, long current) {
        maxPowerStorage = max;
        currentPowerStorage = current;
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
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
        compound.setTag("currentAcceleration", currentAcceleration.serializeNBT());
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
        if (itemStackHandler.getStackInSlot(GuiSlots.INPUT) == null) {
            resetCurrentAcceleration();
            return false;
        }
        else {
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(GuiSlots.INPUT);
            ItemStack itemstack = ParticleAcceleratorRecipes.instance().getAcceleratingResult(stackInSlot);
            long powerRateRequired = ParticleAcceleratorRecipes.instance().getAcceleratingRatePowerRequirement(stackInSlot);
            if (itemstack == null) {
                return false;
            }
            else if(powerRateRequired > maxPowerRate) {
                return false;
            }
            // TODO(CM): Check power and plasma as well.
            if (itemStackHandler.getStackInSlot(GuiSlots.OUTPUT) == null) {
                return true;
            }
            if (!itemStackHandler.getStackInSlot(GuiSlots.OUTPUT).isItemEqual(itemstack)) {
                return false;
            }
            int result = itemStackHandler.getStackInSlot(GuiSlots.OUTPUT).stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= itemStackHandler.getStackInSlot(GuiSlots.OUTPUT).getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
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

    public class GuiSlots {
        static final int INPUT = 0;
        static final int OUTPUT = 1;
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

}
