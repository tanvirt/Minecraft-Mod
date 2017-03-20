package com.quantumindustries.minecraft.mod.multiblock;

import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.MultiblockTileEntityBase;
import it.zerono.mods.zerocore.api.multiblock.rectangular.PartPosition;
import it.zerono.mods.zerocore.lib.BlockFacings;

import javax.annotation.Nonnull;

public class ParticleAcceleratorTileEntity extends MultiblockTileEntityBase {

    private PartPosition position;
    private BlockFacings outwardFacings;

    public ParticleAcceleratorTileEntity() {
        position = PartPosition.Unknown;
        outwardFacings = BlockFacings.NONE;
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

    @Override
    public void onMachineAssembled(MultiblockControllerBase multiblockControllerBase) {

    }

    @Override
    public void onPreMachineAssembled(MultiblockControllerBase multiblockControllerBase) {

    }

    @Override
    public void onPostMachineAssembled(MultiblockControllerBase multiblockControllerBase) {

    }

    @Override
    public void onMachineBroken() {
        position = PartPosition.Unknown;
        outwardFacings = BlockFacings.NONE;
    }

    @Override
    public void onPreMachineBroken() {

    }

    @Override
    public void onPostMachineBroken() {

    }

    @Override
    public void onMachineActivated() {

    }

    @Override
    public void onMachineDeactivated() {

    }

    @Override
    public MultiblockControllerBase createNewMultiblock() {
        return new ParticleAcceleratorController(this.worldObj);
    }

    @Override
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
        return ParticleAcceleratorController.class;
    }

    public ParticleAcceleratorController getAcceleratorController() {
        return (ParticleAcceleratorController)this.getMultiblockController();
    }
}
