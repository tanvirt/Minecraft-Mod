package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import com.sun.jna.platform.win32.WinDef;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.MultiblockTileEntityBase;
import it.zerono.mods.zerocore.api.multiblock.rectangular.PartPosition;
import it.zerono.mods.zerocore.lib.BlockFacings;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ParticleAcceleratorTileEntity extends MultiblockTileEntityBase {

    private PartPosition position;
    private BlockFacings outwardFacings;
    private boolean rotateTop;

    public ParticleAcceleratorTileEntity() {
        position = PartPosition.Unknown;
        outwardFacings = BlockFacings.NONE;
        rotateTop = false;
    }

    /**
     * Get the outward facing of the part in the formed multiblock
     *
     * @return the outward facing of the part. A face is "set" in the BlockFacings object if that face is facing outward
     */
    @Nonnull
    public BlockFacings getOutwardsDir() {
        return outwardFacings;
    }

    /**
     * Get the position of the part in the formed multiblock
     *
     * @return the position of the part
     */
    @Nonnull
    public PartPosition getPartPosition() {
        return position;
    }

    @Nonnull
    public boolean getRotateTop() {
        return rotateTop;
    }

    @Override
    public void onMachineAssembled(MultiblockControllerBase multiblockControllerBase) {
        recalculateOutwardsDirection();
    }

    @Override
    public void onPreMachineAssembled(MultiblockControllerBase multiblockControllerBase) {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    public void onPostMachineAssembled(MultiblockControllerBase multiblockControllerBase) {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    public void onMachineBroken() {
        position = PartPosition.Unknown;
        outwardFacings = BlockFacings.NONE;
        rotateTop = false;
    }

    @Override
    public void onPreMachineBroken() {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    public void onPostMachineBroken() {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    public void onMachineActivated() {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    public void onMachineDeactivated() {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    public MultiblockControllerBase createNewMultiblock() {
        return new ParticleAcceleratorController(worldObj);
    }

    @Override
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
        return ParticleAcceleratorController.class;
    }

    public ParticleAcceleratorController getAcceleratorController() {
        return (ParticleAcceleratorController) getMultiblockController();
    }

    public void recalculateOutwardsDirection() {
        BlockPos myPosition = this.getPos();

        boolean downFacing = !blockAtPositionIsMultiBlock(myPosition.down());
        boolean upFacing = !blockAtPositionIsMultiBlock(myPosition.up());
        boolean northFacing = !blockAtPositionIsMultiBlock(myPosition.north());
        boolean southFacing = !blockAtPositionIsMultiBlock(myPosition.south());
        boolean eastFacing = !blockAtPositionIsMultiBlock(myPosition.east());
        boolean westFacing = !blockAtPositionIsMultiBlock(myPosition.west());

        outwardFacings = BlockFacings.from(downFacing, upFacing, northFacing, southFacing, westFacing, eastFacing);
        BlockFacings topOnly = BlockFacings.from(false, true, false, false, false, false);
        if(outwardFacings.equals(topOnly)) {
            rotateTop = blockAtPositionIsTopMid(myPosition);
        }

    }

    private boolean blockAtPositionIsTopMid(BlockPos position) {
        return getAcceleratorController().getBlockAtPosition(position.west().down()) instanceof ParticleAcceleratorBlockBeamPipe &&
                getAcceleratorController().getBlockAtPosition(position.east().down()) instanceof ParticleAcceleratorBlockBeamPipe;
    }

    private boolean blockAtPositionIsMultiBlock(BlockPos position) {
        return getAcceleratorController().getBlockAtPosition(position) instanceof ParticleAcceleratorBlockBase;
    }

    public void notifyUpdate() {
        getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), -999, writeToNBT(new NBTTagCompound()));
    }

}
