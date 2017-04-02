package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.containers;

import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorControllerTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class ParticleAcceleratorControllerContainer extends Container {

    private ParticleAcceleratorControllerTileEntity te;

    public ParticleAcceleratorControllerContainer(IInventory playerInventory, ParticleAcceleratorControllerTileEntity te) {
        this.te = te;

        // This container references items out of our own inventory (the 9 slots we hold ourselves)
        // as well as the slots from the player inventory so that the user can transfer items between
        // both inventories. The two calls below make sure that slots are defined for both inventories.
        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 72 + 70;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // TODO(CM): Convert the 'slot' value to an enum list for different types of slots.
        SlotCoordinates input = new SlotCoordinates(64, 9, 0);
        SlotCoordinates output = new SlotCoordinates(64, 53, 1);

        addSlotToContainer(new SlotItemHandler(itemHandler, input.slotNumber, input.x, input.y));
        addSlotToContainer(new SlotItemHandler(itemHandler, output.slotNumber, output.x, output.y));
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();

            if (index < ParticleAcceleratorControllerTileEntity.SIZE) {
                if (!this.mergeItemStack(itemStack1, ParticleAcceleratorControllerTileEntity.SIZE,
                        this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemStack1, 0,
                    ParticleAcceleratorControllerTileEntity.SIZE, false)) {
                return null;
            }

            if (itemStack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }

    public class SlotCoordinates {
        int x;
        int y;
        int slotNumber;

        SlotCoordinates(int xIn, int yIn, int slot) {
            x = xIn;
            y = yIn;
            slotNumber = slot;
        }
    }
}
