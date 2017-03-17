package com.quantumindustries.minecraft.mod.multiblock;

import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.MultiblockTileEntityBase;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.api.multiblock.validation.ValidationError;
import it.zerono.mods.zerocore.lib.block.ModTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleAcceleratorController extends MultiblockControllerBase {

    private ParticleAcceleratorPowerTileEntity powerPort;
    private ParticleAcceleratorIOPortTileEntity inputPort;
    private ParticleAcceleratorIOPortTileEntity outputPort;
    private boolean isActive;

    private static final int ACCELERATOR_SIZE_TOTAL = 27;
    private static final int ACCELERATOR_DIMENSIONAL_SIZE = 10;

    public ParticleAcceleratorController(World world) {
        super(world);

        this.powerPort = null;
        this.inputPort = null;
        this.outputPort = null;
        this.isActive = false;
    }

    @Override
    public void onAttachedPartWithMultiblockData(IMultiblockPart iMultiblockPart, NBTTagCompound nbtTagCompound) {

    }

    @Override
    protected void onBlockAdded(IMultiblockPart iMultiblockPart) {

    }

    @Override
    protected void onBlockRemoved(IMultiblockPart iMultiblockPart) {

    }

    @Override
    protected void onMachineAssembled() {

    }

    @Override
    protected void onMachineRestored() {

    }

    @Override
    protected void onMachinePaused() {

    }

    @Override
    protected void onMachineDisassembled() {

    }

    @Override
    protected int getMinimumNumberOfBlocksForAssembledMachine() {
        return ACCELERATOR_SIZE_TOTAL;
    }

    @Override
    protected int getMaximumXSize() {
        return ACCELERATOR_DIMENSIONAL_SIZE;
    }

    @Override
    protected int getMaximumZSize() {
        return ACCELERATOR_DIMENSIONAL_SIZE;
    }

    @Override
    protected int getMaximumYSize() {
        return 3;
    }

    @Override
    protected int getMinimumXSize() {
        return 3;
    }

    @Override
    protected int getMinimumZSize() {

        return ACCELERATOR_DIMENSIONAL_SIZE;
    }

    @Override
    protected int getMinimumYSize() {
        return ACCELERATOR_DIMENSIONAL_SIZE;
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        ParticleAcceleratorPowerTileEntity powerPortFound = null;
        ParticleAcceleratorIOPortTileEntity inputPortFound = null;
        ParticleAcceleratorIOPortTileEntity outputPortFound = null;

        int controllerX = this.getReferenceCoord().getX();
        int controllerY = this.getReferenceCoord().getY();
        int controllerZ = this.getReferenceCoord().getZ();

        if (connectedParts.size() < getMinimumNumberOfBlocksForAssembledMachine()) {
            validatorCallback.setLastError(ValidationError.VALIDATION_ERROR_TOO_FEW_PARTS);
            return false;
        }

        BlockPos maximumCoord = this.getMaximumCoord();
        BlockPos minimumCoord = this.getMinimumCoord();

        int minX = minimumCoord.getX();
        int minY = minimumCoord.getY();
        int minZ = minimumCoord.getZ();
        int maxX = maximumCoord.getX();
        int maxY = maximumCoord.getY();
        int maxZ = maximumCoord.getZ();

        int deltaX = getDelta(maxX, minX);
        int deltaY = getDelta(maxY, minY);
        int deltaZ = getDelta(maxZ, minZ);

        int maxXSize = this.getMaximumXSize();
        int maxYSize = this.getMaximumYSize();
        int maxZSize = this.getMaximumZSize();
        int minXSize = this.getMinimumXSize();
        int minYSize = this.getMinimumYSize();
        int minZSize = this.getMinimumZSize();

        if (!isMachineTooLarge(maxXSize, deltaX, "X", validatorCallback))
            return false;
        if (!isMachineTooLarge(maxYSize, deltaY, "Y", validatorCallback))
            return false;
        if (!isMachineTooLarge(maxZSize, deltaZ, "Z", validatorCallback))
            return false;
        if (!isMachineTooSmall(minXSize, deltaX, "X", validatorCallback))
            return false;
        if (!isMachineTooSmall(minYSize, deltaY, "Y", validatorCallback))
            return false;
        if (!isMachineTooSmall(minZSize, deltaZ, "Z", validatorCallback))
            return false;

        TileEntity tile;
        MultiblockTileEntityBase multiblockPart;
        Class<? extends MultiblockControllerBase> particleController = this.getClass();
        int extremes;
        boolean isPartValid;

        return false;
    }

    @Override
    protected void onAssimilate(MultiblockControllerBase multiblockControllerBase) {

    }

    @Override
    protected void onAssimilated(MultiblockControllerBase multiblockControllerBase) {

    }

    @Override
    protected boolean updateServer() {
        return false;
    }

    @Override
    protected void updateClient() {

    }

    @Override
    protected boolean isBlockGoodForFrame(World world, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        iMultiblockValidator.setLastError("custommod:api.multiblock.validation.invalid_block", x, y, z);
        return false;
    }

    @Override
    protected boolean isBlockGoodForTop(World world, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForBottom(World world, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForSides(World world, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected void syncDataFrom(NBTTagCompound nbtTagCompound, ModTileEntity.SyncReason syncReason) {

    }

    @Override
    protected void syncDataTo(NBTTagCompound nbtTagCompound, ModTileEntity.SyncReason syncReason) {

    }

    private int getDelta(int max, int min) {
        return max - min + 1;
    }

    private boolean isMachineTooSmall(int min, int delta, String dir, IMultiblockValidator validatorCallback) {
        if (delta < min) {
            validatorCallback.setLastError(
                    "zerocore:zerocore:api.multiblock.validation.machine_too_small", min, dir
            );
            return false;
        }
        return true;
    }

    private boolean isMachineTooLarge(int max, int delta, String dir, IMultiblockValidator validatorCallback) {
        if (max > 0 && delta > max) {
            validatorCallback.setLastError("zerocore:api.multiblock.validation.machine_too_large", max, dir);
            return false;
        }
        return true;
    }
}
