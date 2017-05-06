package com.quantumindustries.minecraft.mod.proxy;

import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorControllerTileEntity;
import com.quantumindustries.minecraft.mod.containers.ParticleAcceleratorControllerContainer;
import com.quantumindustries.minecraft.mod.guis.ParticleAcceleratorControllerGui;
import com.quantumindustries.minecraft.mod.util.BaseMachineContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if(tileEntity != null) {
            if(isParticleAcceleratorController(tileEntity)) {
                return getParticleAcceleratorControllerContainer(player, tileEntity);
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if(tileEntity != null) {
            if(isParticleAcceleratorController(tileEntity)) {
                return getParticleAcceleratorControllerGui(player, tileEntity);
            }
        }

        return null;
    }

    private boolean isParticleAcceleratorController(TileEntity tileEntity) {
        return tileEntity instanceof ParticleAcceleratorControllerTileEntity;
    }

    private ParticleAcceleratorControllerContainer getParticleAcceleratorControllerContainer(EntityPlayer player,
                                                                                             TileEntity tileEntity) {
        BaseMachineContainer powerPort = ((ParticleAcceleratorControllerTileEntity) tileEntity).
                                            getAcceleratorController().getPowerPort().getContainer();
        ParticleAcceleratorControllerTileEntity controllerTileEntity = (ParticleAcceleratorControllerTileEntity) tileEntity;
        return new ParticleAcceleratorControllerContainer(player.inventory, controllerTileEntity, powerPort);
    }

    private ParticleAcceleratorControllerGui getParticleAcceleratorControllerGui(EntityPlayer player,
                                                                                 TileEntity tileEntity) {
        BaseMachineContainer powerPort = ((ParticleAcceleratorControllerTileEntity) tileEntity).
                getAcceleratorController().getPowerPort().getContainer();

        return new ParticleAcceleratorControllerGui(powerPort, (ParticleAcceleratorControllerTileEntity) tileEntity,
                getParticleAcceleratorControllerContainer(player, tileEntity));
    }

}
