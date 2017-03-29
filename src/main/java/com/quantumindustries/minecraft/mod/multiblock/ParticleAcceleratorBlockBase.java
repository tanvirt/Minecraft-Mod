package com.quantumindustries.minecraft.mod.multiblock;

import com.quantumindustries.minecraft.mod.blocks.BlockBase;
import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.validation.ValidationError;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class ParticleAcceleratorBlockBase extends BlockBase {

    private ParticleAcceleratorBlockType blockType;

    public ParticleAcceleratorBlockBase(String name, ParticleAcceleratorBlockType type) {
        super(Material.IRON, name);
        this.setHardness(1.5f);
        this.setResistance(10.0f);
        this.setSoundType(SoundType.METAL);

        this.blockType = type;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        switch(this.blockType) {
            case Power:
                return new ParticleAcceleratorPowerTileEntity();
            case Input:
                return new ParticleAcceleratorIOPortTileEntity(true);
            case Output:
                return new ParticleAcceleratorIOPortTileEntity(false);
            case Controller:
                return new ParticleAcceleratorControllerTileEntity();
            default:
                return new ParticleAcceleratorTileEntity();
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos position, IBlockState state,
                                    EntityPlayer player, EnumHand hand, ItemStack heldItem,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!canActivate(world, hand, heldItem)) {
            return false;
        }

        ParticleAcceleratorController controller = this.getAcceleratorController(world, position);

        // TODO(CM): fix this logic to actually work for our machine instead of tutorial
        if(controller != null) {
            if(player.isSneaking()) {
                controller.toggleActive();
                return true;
            }
            else {
                ValidationError status = controller.getLastError();
                if(status != null) {
                    player.addChatMessage(status.getChatMessage());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canActivate(World world, EnumHand hand, ItemStack heldItem) {
        if (world.isRemote) {
            return false;
        }
        else if (hand != EnumHand.OFF_HAND) {
            return false;
        }
        else if (heldItem != null) {
            return false;
        }
        else {
            return true;
        }
    }

    protected IMultiblockPart getMultiblockPartAt(IBlockAccess world, BlockPos position) {
        TileEntity tile = world.getTileEntity(position);
        if(tile instanceof IMultiblockPart) {
            return (IMultiblockPart) tile;
        }
        else {
            return null;
        }
    }

    protected MultiblockControllerBase getMultiblockController(IBlockAccess world, BlockPos position) {
        IMultiblockPart part = getMultiblockPartAt(world, position);
        if(part != null) {
            return part.getMultiblockController();
        }
        else {
            return null;
        }
    }

    protected ParticleAcceleratorController getAcceleratorController(IBlockAccess world, BlockPos position) {
        MultiblockControllerBase controller = getMultiblockController(world, position);
        if(controller instanceof ParticleAcceleratorController) {
            return (ParticleAcceleratorController) controller;
        }
        else {
            return null;
        }
    }
}
