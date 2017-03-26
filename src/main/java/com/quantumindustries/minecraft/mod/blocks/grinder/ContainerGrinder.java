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

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
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

        ticksGrindingItemSoFar = tileGrinder.getField(1); // tick grinding item so far
        ticksPerItem = tileGrinder.getField(2); // ticks per item
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

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex) {
        // TODO(TT): Break this method up. Too many lines.
        ItemStack itemStack1 = null;
        Slot slot = inventorySlots.get(slotIndex);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack1 = itemStack2.copy();

            if(slotIndex == TileEntityGrinder.slotEnum.OUTPUT_SLOT.ordinal()) {
                if(!mergeItemStack(
                        itemStack2,
                        sizeInventory,
                        sizeInventory + 36,
                        true
                )) {
                    return null;
                }

                slot.onSlotChange(itemStack2, itemStack1);
            }
            else if(slotIndex != TileEntityGrinder.slotEnum.INPUT_SLOT.ordinal()) {
                // check if there is a grinding recipe for the stack
                if(GrinderRecipes.getGrindingResult(itemStack2) != null) {
                    if(!mergeItemStack(itemStack2, 0, 1, false)) {
                        return null;
                    }
                }
                else if(slotIndex >= sizeInventory && slotIndex < sizeInventory + 27) { // player inventory slots
                    if(!mergeItemStack(
                            itemStack2,
                            sizeInventory + 27,
                            sizeInventory + 36,
                            false
                    )) {
                        return null;
                    }
                }
                else if(slotIndex >= sizeInventory + 27 &&
                        slotIndex < sizeInventory + 36 &&
                        !mergeItemStack(
                                itemStack2,
                                sizeInventory + 1,
                                sizeInventory + 27,
                                false
                        )) { // hotbar slots
                    return null;
                }
            }
            else if(!mergeItemStack(
                    itemStack2,
                    sizeInventory,
                    sizeInventory + 36,
                    false
            )) {
                return null;
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
        // note that the slot numbers are within the player inventory
        // so can they be the same those for the tile entity inventory
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

}
