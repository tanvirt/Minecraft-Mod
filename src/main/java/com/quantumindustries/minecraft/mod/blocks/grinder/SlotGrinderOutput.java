package com.quantumindustries.minecraft.mod.blocks.grinder;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class SlotGrinderOutput extends Slot {

    private final EntityPlayer thePlayer;
    private int numGrinderOutput;

    public SlotGrinderOutput(EntityPlayer player, IInventory inventory,
                             int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
        thePlayer = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if(getHasStack()) {
            numGrinderOutput += Math.min(amount, getStack().stackSize);
        }
        return super.decrStackSize(amount);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        onCrafting(stack);
        super.onPickupFromSlot(playerIn, stack);
    }

    /**
     * The itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood
     */
    @Override
    protected void onCrafting(ItemStack itemStack, int amountGround) {
        numGrinderOutput += amountGround;
        onCrafting(itemStack);
    }

    /**
     * The itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    @Override
    protected void onCrafting(ItemStack itemStack) {
        if(!thePlayer.worldObj.isRemote) {
            float expFactor = GrinderRecipes.getGrindingExperience(itemStack);
            int expEarned = getExpEarned(numGrinderOutput, expFactor);
            createExperienceOrbs(expEarned);
        }
        numGrinderOutput = 0;
    }

    private int getExpEarned(int expEarned, float expFactor) {
        if(expFactor == 0) {
            expEarned = 0;
        }
        else if(expFactor < 1.0f) {
            int possibleExpEarned = MathHelper.floor_float(expEarned*expFactor);

            if(possibleExpEarned < MathHelper.ceiling_float_int(expEarned*expFactor) &&
                    Math.random() < expEarned*expFactor - possibleExpEarned) {
                ++possibleExpEarned;
            }

            expEarned = possibleExpEarned;
        }
        return expEarned;
    }

    private void createExperienceOrbs(int expEarned) {
        int expInOrb;
        while(expEarned > 0) {
            expInOrb = EntityXPOrb.getXPSplit(expEarned);
            expEarned -= expInOrb;
            thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(
                    thePlayer.worldObj,
                    thePlayer.posX,
                    thePlayer.posY + 0.5d,
                    thePlayer.posZ + 0.5d,
                    expInOrb
            ));
        }
    }

}