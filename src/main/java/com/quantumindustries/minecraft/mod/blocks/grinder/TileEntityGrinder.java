package com.quantumindustries.minecraft.mod.blocks.grinder;

import com.quantumindustries.minecraft.mod.CustomMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TileEntityGrinder extends TileEntityLockable
        implements ITickable, ISidedInventory {

    public enum slotEnum {
        INPUT_SLOT, OUTPUT_SLOT
    }

    private static final int[] slotsTop = new int[] { slotEnum.INPUT_SLOT.ordinal() };
    private static final int[] slotsBottom = new int[] { slotEnum.OUTPUT_SLOT.ordinal() };
    private static final int[] slotsSides = new int[] {};
    private ItemStack[] grinderItemStackArray = new ItemStack[2];
    private int timeCanGrind;
    private int currentItemGrindTime;
    private int ticksGrindingItemSoFar;
    private int ticksPerItem;
    private String grinderCustomName;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState,
                                 IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public int getSizeInventory() {
        return grinderItemStackArray.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return grinderItemStackArray[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(grinderItemStackArray[index] != null) {
            ItemStack itemstack;

            if(grinderItemStackArray[index].stackSize <= count) {
                itemstack = grinderItemStackArray[index];
                grinderItemStackArray[index] = null;
                return itemstack;
            }
            else {
                itemstack = grinderItemStackArray[index].splitStack(count);

                if(grinderItemStackArray[index].stackSize == 0) {
                    grinderItemStackArray[index] = null;
                }

                return itemstack;
            }
        }
        else {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
    public ItemStack removeStackFromSlot(int index) {
        if(grinderItemStackArray[index] != null) {
            ItemStack itemstack = grinderItemStackArray[index];
            grinderItemStackArray[index] = null;
            return itemstack;
        }
        else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        boolean isSameItemStackAlreadyInSlot = stack != null &&
                stack.isItemEqual(grinderItemStackArray[index]) &&
                ItemStack.areItemStackTagsEqual(stack, grinderItemStackArray[index]);
        grinderItemStackArray[index] = stack;

        if(stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        // if input slot, reset the grinding timers
        if(index == slotEnum.INPUT_SLOT.ordinal() && !isSameItemStackAlreadyInSlot) {
            ticksPerItem = timeToGrindOneItem(stack);
            ticksGrindingItemSoFar = 0;
            markDirty();
        }
    }

    @Override
    public String getName() {
        if(hasCustomName()) {
            return grinderCustomName;
        }
        else {
            return "container.grinder";
        }
    }

    @Override
    public boolean hasCustomName() {
        return grinderCustomName != null && grinderCustomName.length() > 0;
    }

    public void setCustomInventoryName(String parCustomName) {
        grinderCustomName = parCustomName;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        grinderItemStackArray = new ItemStack[getSizeInventory()];

        for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
            byte slot = nbtTagCompound.getByte("Slot");

            if(slot >= 0 && slot < grinderItemStackArray.length) {
                grinderItemStackArray[slot] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
            }
        }

        timeCanGrind = compound.getShort("GrindTime");
        ticksGrindingItemSoFar = compound.getShort("CookTime");
        ticksPerItem = compound.getShort("CookTimeTotal");

        if(compound.hasKey("CustomName", 8)) {
            grinderCustomName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("GrindTime", (short) timeCanGrind);
        compound.setShort("CookTime", (short) ticksGrindingItemSoFar);
        compound.setShort("CookTimeTotal", (short) ticksPerItem);
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < grinderItemStackArray.length; ++i) {
            if(grinderItemStackArray[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                grinderItemStackArray[i].writeToNBT(nbtTagCompound);
                nbttaglist.appendTag(nbtTagCompound);
            }
        }

        compound.setTag("Items", nbttaglist);

        if(hasCustomName()) {
            compound.setString("CustomName", grinderCustomName);
        }

        return compound;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    private boolean grindingSomething() {
        // TODO(TT): actually determine if we're grinding something
        return true;
    }

    @Override
    public void update() {
        // TODO(TT): fix this method. it actually makes no sense
        boolean hasBeenGrinding = grindingSomething();
        boolean changedGrindingState = false;

        if(grindingSomething()) {
            --timeCanGrind;
        }

        if(!worldObj.isRemote) {
            if(inputSlotIsOccupied()) {
                if(shouldStartGrinding()) {
                    changedGrindingState = startGrinding(changedGrindingState);
                }

                if(shouldContinueGrinding()) {
                    changedGrindingState = continueGrinding(changedGrindingState);
                }
                else {
                    ticksGrindingItemSoFar = 0;
                }
            }
            changedGrindingState = updateGrindingState(hasBeenGrinding, changedGrindingState);
        }

        if(changedGrindingState) {
            markDirty();
        }
    }

    private boolean updateGrindingState(boolean hasBeenGrinding, boolean changedGrindingState) {
        // started or stopped grinding, update block to change to active or inactive model
        if(hasBeenGrinding != grindingSomething()) {
            // the isGrinding() value may have changed due to call to grindItem() earlier
            changedGrindingState = true;
        }
        return changedGrindingState;
    }

    private boolean shouldContinueGrinding() {
        return grindingSomething() && canGrind();
    }

    private boolean shouldStartGrinding() {
        return !grindingSomething() && canGrind();
    }

    private boolean continueGrinding(boolean changedGrindingState) {
        ++ticksGrindingItemSoFar;

        // check if completed grinding an item
        if(grindingCompleted()) {
            ticksGrindingItemSoFar = 0;
            ticksPerItem = timeToGrindOneItem(grinderItemStackArray[0]);
            grindItem();
            changedGrindingState = true;
        }
        return changedGrindingState;
    }

    private boolean grindingCompleted() {
        return ticksGrindingItemSoFar == ticksPerItem;
    }

    private boolean startGrinding(boolean changedGrindingState) {
        timeCanGrind = 150;

        if(grindingSomething()) {
            changedGrindingState = true;
        }
        return changedGrindingState;
    }

    private boolean inputSlotIsOccupied() {
        return grinderItemStackArray[slotEnum.INPUT_SLOT.ordinal()] != null;
    }

    private int timeToGrindOneItem(ItemStack parItemStack) {
        // TODO(TT): check types of item stack to determine grind time
        return 200;
    }

    private boolean canGrind() {
        int inputSlot = slotEnum.INPUT_SLOT.ordinal();
        int outputSlot = slotEnum.OUTPUT_SLOT.ordinal();
        ItemStack inputStack = grinderItemStackArray[inputSlot];
        ItemStack outputStack = grinderItemStackArray[outputSlot];
        if(inputStack == null) {
            return false;
        }
        else {
            ItemStack itemStackToOutput = GrinderRecipes.instance().getGrindingResult(inputStack);
            if(itemStackToOutput == null) {
                return false;
            }
            if(outputStack == null) {
                return true;
            }
            if(!outputStack.isItemEqual(itemStackToOutput)) {
                return false;
            }
            int result = outputStack.stackSize + itemStackToOutput.stackSize;
            return result <= getInventoryStackLimit() &&
                    result <= outputStack.getMaxStackSize();
        }
    }

    public void grindItem() {
        if(canGrind()) {
            int inputSlot = slotEnum.INPUT_SLOT.ordinal();
            int outputSlot = slotEnum.OUTPUT_SLOT.ordinal();
            ItemStack inputStack = grinderItemStackArray[inputSlot];
            ItemStack outputStack = grinderItemStackArray[outputSlot];

            ItemStack itemstack = GrinderRecipes.instance().getGrindingResult(inputStack);

            // check if output slot is empty
            if(outputStack == null) {
                grinderItemStackArray[outputSlot] = itemstack.copy();
            }
            else if(outputStack.getItem() == itemstack.getItem()) {
                outputStack.stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            --inputStack.stackSize;

            if(inputStack.stackSize <= 0) {
                grinderItemStackArray[inputSlot] = null;
            }
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        if(worldObj.getTileEntity(pos) != this) {
            return false;
        }
        else {
            return playerIn.getDistanceSq(
                    pos.getX() + 0.5D,
                    pos.getY() + 0.5D,
                    pos.getZ() + 0.5D
            ) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {}

    @Override
    public void closeInventory(EntityPlayer playerIn) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        // can always put things in input (may not grind though) and can't put anything in output
        return index == slotEnum.INPUT_SLOT.ordinal();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if(side == EnumFacing.DOWN) {
            return slotsBottom;
        }
        else {
            if(side == EnumFacing.UP) {
                return slotsTop;
            }
            else {
                return slotsSides;
            }
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int parSlotIndex, ItemStack parStack, EnumFacing parFacing) {
        return true;
    }

    @Override
    public String getGuiID() {
        return CustomMod.MODID + ":blockGrinder";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerGrinder(playerInventory, this);
    }

    @Override
    public int getField(int id) {
        switch(id) {
            case 0:
                return timeCanGrind;
            case 1:
                return currentItemGrindTime;
            case 2:
                return ticksGrindingItemSoFar;
            case 3:
                return ticksPerItem;
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch(id) {
            case 0:
                timeCanGrind = value;
            case 1:
                currentItemGrindTime = value;
            case 2:
                ticksGrindingItemSoFar = value;
            case 3:
                ticksPerItem = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void clear() {
        for(int i = 0; i < grinderItemStackArray.length; ++i) {
            grinderItemStackArray[i] = null;
        }
    }

}
