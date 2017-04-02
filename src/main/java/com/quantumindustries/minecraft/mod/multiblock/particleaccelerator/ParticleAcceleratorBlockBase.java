package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.blocks.BlockBase;
import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.validation.ValidationError;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

public abstract class ParticleAcceleratorBlockBase extends BlockBase {

    private ParticleAcceleratorBlockType blockType;
    private static int CONTROLLER_GUI_ID = 1;

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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand,
                                    ItemStack heldItem, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        // Only execute on the server
        if (world.isRemote) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof ParticleAcceleratorControllerTileEntity)) {
            return false;
        }
        player.addChatMessage(new TextComponentString("activated PA"));
        player.openGui(CustomMod.instance, CONTROLLER_GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    private boolean canActivate(World world, EnumHand hand, ItemStack heldItem) {
        if(world.isRemote) {
            return false;
        }
        else if(hand != EnumHand.MAIN_HAND) {
            return false;
        }
        else if(heldItem != null) {
            return false;
        }
        return true;
    }

    protected IMultiblockPart getMultiblockPartAt(IBlockAccess world, BlockPos position) {
        TileEntity tile = world.getTileEntity(position);
        if(tile instanceof IMultiblockPart) {
            return (IMultiblockPart) tile;
        }
        return null;
    }

    protected MultiblockControllerBase getMultiblockController(IBlockAccess world, BlockPos position) {
        IMultiblockPart part = getMultiblockPartAt(world, position);
        if(part != null) {
            return part.getMultiblockController();
        }
        return null;
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

    private boolean isPlayerHittingController(IBlockState state) {
        return state.getBlock() instanceof ParticleAcceleratorBlockController;
    }
}
