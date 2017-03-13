package com.quantumindustries.minecraft.mod.guis;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.blocks.grinder.ContainerGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if(tileEntity != null) {
            if(isGrinderID(ID)) {
                return new ContainerGrinder(player.inventory, (IInventory) tileEntity);
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if(tileEntity != null) {
            if(isGrinderID(ID)) {
                return new GuiGrinder(player.inventory, (IInventory) tileEntity);
            }
        }

        return null;
    }

    private boolean isGrinderID(int ID) {
        return ID == CustomMod.GUI.GRINDER.ordinal();
    }

}
