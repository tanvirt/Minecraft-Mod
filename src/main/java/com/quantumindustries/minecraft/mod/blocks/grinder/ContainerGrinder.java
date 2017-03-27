package com.quantumindustries.minecraft.mod.blocks.grinder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerGrinder extends Container {

    private final IInventory tileGrinder;
    private final int sizeInventory;
    private int ticksGrindingItemSoFar;
    private int ticksPerItem;

    public ContainerGrinder(InventoryPlayer parInventoryPlayer, IInventory parIInventory) {
        tileGrinder = parIInventory;
        sizeInventory = tileGrinder.getSizeInventory();
        addContainerSlots(parInventoryPlayer);
        addPlayerInventorySlots(parInventoryPlayer);
        addHotbarSlots(parInventoryPlayer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < listeners.size(); i++) {
            IContainerListener listener = listeners.get(i);

            if(ticksGrindingItemSoFar != tileGrinder.getField(1)) {
                listener.sendProgressBarUpdate(this, 1, tileGrinder.getField(1));
            }

            if(ticksPerItem != tileGrinder.getField(2)) {
                listener.sendProgressBarUpdate(this, 2, tileGrinder.getField(2));
            }
        }

        ticksGrindingItemSoFar = tileGrinder.getField(1);
        ticksPerItem = tileGrinder.getField(2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        tileGrinder.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileGrinder.isUseableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex) {
        ItemStack itemStack1 = null;
        Slot slot = inventorySlots.get(slotIndex);

        if(isValidSlot(slot)) {
            ItemStack itemStack2 = slot.getStack();
            itemStack1 = itemStack2.copy();

            if(isOutputSlot(slotIndex)) {
                int start = sizeInventory;
                int end = sizeInventory + 36;
                if(!mergeItemStack(itemStack2, start, end, true)) {
                    return null;
                }
                slot.onSlotChange(itemStack2, itemStack1);
            }
            else {
                Indices indices = getMergeIndices(itemStack2, slotIndex);
                if(indices != null &&
                        !mergeItemStack(
                                itemStack2,
                                indices.start,
                                indices.end,
                                false)
                        ) {
                    return null;
                }
            }

            if(itemStack2.stackSize == 0) {
                slot.putStack(null);
            }
            else {
                slot.onSlotChanged();
            }

            if(itemStack2.stackSize == itemStack1.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemStack2);
        }

        return itemStack1;
    }

    private boolean isPlayerInventorySlot(int slotIndex) {
        return slotIndex >= sizeInventory && slotIndex < sizeInventory + 27;
    }

    private void addContainerSlots(InventoryPlayer parInventoryPlayer) {
        addSlotToContainer(new Slot(
                tileGrinder,
                TileEntityGrinder.slotEnum.INPUT_SLOT.ordinal(),
                56,
                35
        ));
        addSlotToContainer(new SlotGrinderOutput(
                parInventoryPlayer.player,
                tileGrinder,
                TileEntityGrinder.slotEnum.OUTPUT_SLOT.ordinal(),
                116,
                35
        ));
    }

    private void addPlayerInventorySlots(InventoryPlayer parInventoryPlayer) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(
                        parInventoryPlayer,
                        j + i * 9 + 9,
                        8 + j * 18,
                        84 + i * 18
                ));
            }
        }
    }

    private void addHotbarSlots(InventoryPlayer parInventoryPlayer) {
        for(int i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(
                    parInventoryPlayer,
                    i,
                    8 + i * 18,
                    142
            ));
        }
    }

    private boolean isValidSlot(Slot slot) {
        return slot != null && slot.getHasStack();
    }

    private boolean isInputSlot(int slotIndex) {
        return slotIndex == TileEntityGrinder.slotEnum.INPUT_SLOT.ordinal();
    }

    private boolean isOutputSlot(int slotIndex) {
        return slotIndex == TileEntityGrinder.slotEnum.OUTPUT_SLOT.ordinal();
    }

    private Indices getMergeIndices(ItemStack itemStack, int slotIndex) {
        if(!isInputSlot(slotIndex)) {
            if(grindingRecipeExists(itemStack)) {
                return new Indices(0, 1);
            }
            else if(isPlayerInventorySlot(slotIndex)) {
                return new Indices(sizeInventory + 27, sizeInventory + 36);
            }
            else if(isHotbarSlot(slotIndex)) {
                return new Indices(sizeInventory + 1, sizeInventory + 27);
            }
            return null;
        }
        else {
            return new Indices(sizeInventory, sizeInventory + 36);
        }
    }

    private boolean grindingRecipeExists(ItemStack itemStack2) {
        return GrinderRecipes.getGrindingResult(itemStack2) != null;
    }

    private boolean isHotbarSlot(int slotIndex) {
        return slotIndex >= sizeInventory + 27 &&
                slotIndex < sizeInventory + 36;
    }

    private class Indices {

        public int start;
        public int end;

        public Indices(int start, int end) {
            this.start = start;
            this.end = end;
        }

    }

}
