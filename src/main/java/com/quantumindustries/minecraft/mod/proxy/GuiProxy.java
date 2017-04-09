package com.quantumindustries.minecraft.mod.proxy;

import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorController;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorControllerTileEntity;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorPowerTileEntity;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.containers.ParticleAcceleratorControllerContainer;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.gui.ParticleAcceleratorControllerGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof ParticleAcceleratorControllerTileEntity) {
            ParticleAcceleratorPowerTileEntity powerPort =
                    ((ParticleAcceleratorControllerTileEntity) te).getAcceleratorController().getPowerPort();
            return new ParticleAcceleratorControllerContainer(
                    player.inventory,(ParticleAcceleratorControllerTileEntity) te, powerPort
            );
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof ParticleAcceleratorControllerTileEntity) {
            ParticleAcceleratorPowerTileEntity powerPort =
                    ((ParticleAcceleratorControllerTileEntity) te).getAcceleratorController().getPowerPort();
            ParticleAcceleratorControllerTileEntity containerTileEntity = (ParticleAcceleratorControllerTileEntity) te;
            return new ParticleAcceleratorControllerGui(
                    containerTileEntity,
                    powerPort,
                    new ParticleAcceleratorControllerContainer(player.inventory, containerTileEntity, powerPort)
            );
        }
        return null;
    }
}