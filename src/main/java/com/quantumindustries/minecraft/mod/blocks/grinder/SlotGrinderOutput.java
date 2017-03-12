package com.quantumindustries.minecraft.mod.blocks.grinder;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class SlotGrinderOutput extends Slot {
    /** The player that is using the GUI where this slot resides. */
    private final EntityPlayer thePlayer;
    private int numGrinderOutput;

    public SlotGrinderOutput(EntityPlayer parPlayer, IInventory parIInventory,
                             int parSlotIndex, int parXDisplayPosition, int parYDisplayPosition) {
        super(parIInventory, parSlotIndex, parXDisplayPosition, parYDisplayPosition);
        thePlayer = parPlayer;
    }

    /**
     * Check if the stack is a valid item for this slot. .
     */
    @Override
    public boolean isItemValid(ItemStack stack) {
        return false; // can't place anything into it
    }

    /**
     * Decrease the size of the stack in slot by the amount of the int arg. Returns the new
     * stack.
     */
    @Override
    public ItemStack decrStackSize(int parAmount) {
        if(getHasStack()) {
            numGrinderOutput += Math.min(parAmount, getStack().stackSize);
        }

        return super.decrStackSize(parAmount);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        onCrafting(stack);
        super.onPickupFromSlot(playerIn, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    @Override
    protected void onCrafting(ItemStack parItemStack, int parAmountGround) {
        numGrinderOutput += parAmountGround;
        onCrafting(parItemStack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    @Override
    protected void onCrafting(ItemStack parItemStack) {
        if(!thePlayer.worldObj.isRemote) {
            int expEarned = numGrinderOutput;
            float expFactor = GrinderRecipes.instance().getGrindingExperience(parItemStack);

            if(expFactor == 0.0f) {
                expEarned = 0;
            }
            else if (expFactor < 1.0f) {
                int possibleExpEarned = MathHelper.floor_float(expEarned * expFactor);

                if(possibleExpEarned < MathHelper.ceiling_float_int(expEarned*expFactor) &&
                        Math.random() < expEarned*expFactor - possibleExpEarned) {
                    ++possibleExpEarned;
                }

                expEarned = possibleExpEarned;
            }

            // create experience orbs
            int expInOrb;
            while(expEarned > 0) {
                expInOrb = EntityXPOrb.getXPSplit(expEarned);
                expEarned -= expInOrb;
                thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(
                        thePlayer.worldObj,
                        thePlayer.posX,
                        thePlayer.posY + 0.5D,
                        thePlayer.posZ + 0.5D,
                        expInOrb
                ));
            }
        }

        numGrinderOutput = 0;
    }

}