package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import com.quantumindustries.minecraft.mod.recipes.ParticleAcceleratorRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ParticleAcceleratorControllerTileEntity extends ParticleAcceleratorTileEntity {
    // TODO(CM): Either fix empty class or format to show we aren't using it.

    public static final int SIZE = 2;
    private static final int TOTAL_POWER_INDEX = 0;
    private static final int POWER_RATE_INDEX = 1;

    // This item handler will hold our inventory slots
    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            markDirty();
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
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

    public long getCurrentPower() {
        return ((ParticleAcceleratorController) getMultiblockController()).getPowerPort().getCurrentPower();
    }

    public long getCurrentCapacity() {
        return ((ParticleAcceleratorController) getMultiblockController()).getPowerPort().getCapacity();
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
            return false;
        }
        else {
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(GuiSlots.INPUT);
            ItemStack itemstack = ParticleAcceleratorRecipes.instance().getAcceleratingResult(stackInSlot);
            long totalPowerRequired = ParticleAcceleratorRecipes.instance().getAcceleratingTotalPowerRequirement(stackInSlot);
            if (itemstack == null) {
                return false;
            }
            else if(totalPowerRequired > maxPowerRate) {
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

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public class GuiSlots {
        static final int INPUT = 0;
        static final int OUTPUT = 1;
    }
}
