package com.quantumindustries.minecraft.mod.multiblock;

import com.sun.jna.platform.unix.X11;
import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.lib.block.ModTileEntity;
import it.zerono.mods.zerocore.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class ParticleAcceleratorController extends MultiblockControllerBase {

    private ParticleAcceleratorPowerTileEntity powerPort;
    private ParticleAcceleratorIOPortTileEntity inputPort;
    private ParticleAcceleratorIOPortTileEntity outputPort;
    private ParticleAcceleratorControllerTileEntity controllerBlock;
    private BlockPos controllerBlockPosition;
    private boolean isActive;

    private static final int ACCELERATOR_SIZE_TOTAL = 27;
    private static final int ACCELERATOR_DIMENSIONAL_SIZE = 10;

    private static final int FRONT_LEFT_CORNER_INDEX = 0;
    private static final int FRONT_RIGHT_CORNER_INDEX = 1;
    private static final int BACK_LEFT_CORNER_INDEX = 2;
    private static final int BACK_RIGHT_CORNER_INDEX = 3;

    private static final int LEFT_BEAM_SOURCE_INDEX = 0;
    private static final int RIGHT_BEAM_SOURCE_INDEX = 1;

    // TODO(CM): Change this to directional enum.
    private char controllerBeamDirection;
    private char acceleratorDepthDirection;
    private int length;
    private int depth;

    public ParticleAcceleratorController(World world) {
        super(world);

        this.powerPort = null;
        this.inputPort = null;
        this.outputPort = null;
        this.controllerBlock = null;
        this.isActive = false;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void toggleActive() {
        this.setActive(!this.isActive);
    }

    public void setActive(boolean active) {

        if (this.isActive == active)
            return;

        // the state was changed, set it
        this.isActive = active;

        if (WorldHelper.calledByLogicalServer(this.WORLD)) {

            // on the server side, request an update to be sent to the client and mark the save delegate as dirty
            this.markReferenceCoordForUpdate();
            this.markReferenceCoordDirty();

        } else {

            // on the client, request a render update
            this.markMultiblockForRenderUpdate();
        }
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
        ParticleAcceleratorControllerTileEntity controllerTE;
        List<BlockPos> beamSourcePositions;
        List<BlockPos> cornerPositions;

        // Find the controller block to start validation.
        controllerTE = findControllerBlock();
        if (controllerTE == null)
            return false;

        // Set the XYZ coordinates of the controller.
        controllerBlockPosition = controllerTE.getPos();

        FMLLog.warning("[%s] Controller Found with position: %d %d %d", this.WORLD.isRemote ? "CLIENT" : "SERVER", controllerBlockPosition.getX(), controllerBlockPosition.getY(), controllerBlockPosition.getZ());
        // Find the beam sources.
        beamSourcePositions = getBeamSources(controllerBlockPosition);
        if (beamSourcePositions == null)
            return false;

        BlockPos beamSource1 = beamSourcePositions.get(0);
        BlockPos beamSource2 = beamSourcePositions.get(1);

        FMLLog.warning("[%s] Beam Source Found with position: %d %d %d", this.WORLD.isRemote ? "CLIENT" : "SERVER", beamSource1.getX(), beamSource1.getY(), beamSource1.getZ());
        FMLLog.warning("[%s] Beam Source Found with position: %d %d %d", this.WORLD.isRemote ? "CLIENT" : "SERVER", beamSource2.getX(), beamSource2.getY(), beamSource2.getZ());

        // Find the corners of the accelerator.
        cornerPositions = getAllCorners(beamSourcePositions);
        if (cornerPositions == null) {
            FMLLog.warning("Corners are NOT valid");
            return false;
        }

        FMLLog.warning("[%s] Corners are valid", this.WORLD.isRemote ? "CLIENT" : "SERVER");
        return false;
    }

    private ParticleAcceleratorControllerTileEntity findControllerBlock() {
        for (IMultiblockPart part : this.connectedParts) {
            if (part instanceof ParticleAcceleratorControllerTileEntity) {
                return (ParticleAcceleratorControllerTileEntity) part;
            }
        }
        return null;
    }

    private List<BlockPos> getBeamSources(BlockPos controllerPosition) {
        List<BlockPos> tempSources = new ArrayList<BlockPos>(2);
        boolean sourceXDir = isBeamSourceXDirection(controllerPosition);
        boolean sourceZDir = isBeamSourceZDirection(controllerPosition);
        if (sourceXDir && sourceZDir) {
            return null;
        }
        else if (sourceXDir) {
            Vec3i xVector = new Vec3i(1,0,0);
            tempSources.add(controllerPosition.add(xVector));
            tempSources.add(controllerPosition.subtract(xVector));
            return tempSources;
        }
        else if (sourceZDir) {
            Vec3i zVector = new Vec3i(0,0,1);
            tempSources.add(controllerPosition.add(zVector));
            tempSources.add(controllerPosition.subtract(zVector));
            return tempSources;
        }
        return null;
    }

    private boolean isBeamSourceXDirection(BlockPos position) {
        Vec3i xVector = new Vec3i(1,0,0);
        if (getBlockAtPosition(position.add(xVector)) instanceof ParticleAcceleratorBlockBeamSource &&
                getBlockAtPosition(position.subtract(xVector))instanceof ParticleAcceleratorBlockBeamSource)
            return true;
        return false;
    }

    private boolean isBeamSourceZDirection(BlockPos position) {
        Vec3i zVector = new Vec3i(0,0,1);
        if (getBlockAtPosition(position.add(zVector)) instanceof ParticleAcceleratorBlockBeamSource &&
                getBlockAtPosition(position.subtract(zVector)) instanceof ParticleAcceleratorBlockBeamSource)
            return true;
        return false;
    }

    private List<BlockPos> getAllCorners(List<BlockPos> beamPositions) {
        List<BlockPos> lengthCorners = new ArrayList<BlockPos>(2);
        List<BlockPos> depthCorners = new ArrayList<BlockPos>(2);
        List<BlockPos> cornerPositions = new ArrayList<BlockPos>(4);
        Vec3i positiveXVector = new Vec3i(1, 0, 0);
        Vec3i negativeXVector = new Vec3i(-1, 0, 0);
        Vec3i positiveZVector = new Vec3i(0, 0, 1);;
        Vec3i negativeZVector = new Vec3i(0, 0, -1);;

        if (beamPositions.get(0).getZ() == beamPositions.get(1).getZ()) {
            lengthCorners = calculateLengthCorners(beamPositions, positiveXVector, negativeXVector);
            if (lengthCorners == null)
                return null;
            if (getBlockAtPosition(lengthCorners.get(FRONT_LEFT_CORNER_INDEX).add(positiveZVector)) instanceof ParticleAcceleratorBlockBeamPipe &&
                    getBlockAtPosition(lengthCorners.get(FRONT_RIGHT_CORNER_INDEX).add(positiveZVector)) instanceof ParticleAcceleratorBlockBeamPipe) {
                depthCorners = calculateDepthCorners(lengthCorners, positiveZVector);
                if (depthCorners == null)
                    return null;
            }
            else {
                depthCorners = calculateDepthCorners(lengthCorners, negativeZVector);
                if (depthCorners == null)
                    return null;
            }
        }
        else if (beamPositions.get(0).getX() == beamPositions.get(1).getX()) {
            FMLLog.warning("Attempting to calculate Z Direction Length");
            lengthCorners = calculateLengthCorners(beamPositions, positiveZVector, negativeZVector);
            if (lengthCorners == null)
                return null;
            FMLLog.warning("Z Direction Length Successful");

            if (getBlockAtPosition(lengthCorners.get(FRONT_LEFT_CORNER_INDEX).add(positiveXVector)) instanceof ParticleAcceleratorBlockBeamPipe &&
                    getBlockAtPosition(lengthCorners.get(FRONT_RIGHT_CORNER_INDEX).add(positiveXVector)) instanceof ParticleAcceleratorBlockBeamPipe) {
                depthCorners = calculateDepthCorners(lengthCorners, positiveXVector);
                if (depthCorners == null)
                    return null;
                FMLLog.warning("X Direction Positive Depth Successful");
            }
            else {
                depthCorners = calculateDepthCorners(lengthCorners, negativeXVector);
                if (depthCorners == null)
                    return null;
                FMLLog.warning("X Direction Negative Depth Successful");
            }
        }

        cornerPositions.addAll(lengthCorners);
        cornerPositions.addAll(depthCorners);

        if (!areCornerCoordinatesValid(cornerPositions))
            return null;

        return cornerPositions;
    }

    private List<BlockPos> calculateLengthCorners(List<BlockPos> beamPositions, Vec3i positiveLength, Vec3i negativeLength) {
        FMLLog.warning("Attempting to Calculate Length");
        List<BlockPos> lengthCorners = new ArrayList<BlockPos>(2);
        BlockPos leftCorner = getSingleCorner(beamPositions.get(LEFT_BEAM_SOURCE_INDEX), positiveLength);
        BlockPos rightCorner = getSingleCorner(beamPositions.get(RIGHT_BEAM_SOURCE_INDEX), negativeLength);

        if (leftCorner == null || rightCorner == null)
            return null;
        FMLLog.warning("Length Corners are not null");
        lengthCorners.add(FRONT_LEFT_CORNER_INDEX, leftCorner);
        lengthCorners.add(FRONT_LEFT_CORNER_INDEX, rightCorner);

        if (areBlocksSymmetricalToController(lengthCorners.get(0), lengthCorners.get(1)))
            return lengthCorners;
        FMLLog.warning("Length Corners are not symmetrical");
        return null;
    }

    private List<BlockPos> calculateDepthCorners(List<BlockPos> lengthCorners, Vec3i depthVector) {
        List<BlockPos> depthCorners = new ArrayList<BlockPos>(2);
        BlockPos leftCorner = getSingleCorner(lengthCorners.get(FRONT_LEFT_CORNER_INDEX), depthVector);
        BlockPos rightCorner = getSingleCorner(lengthCorners.get(FRONT_RIGHT_CORNER_INDEX), depthVector);

        if (leftCorner == null || rightCorner == null)
            return null;

        depthCorners.add(leftCorner);
        depthCorners.add(rightCorner);

        if (areBlocksSymmetricalToController(depthCorners.get(0), depthCorners.get(1)))
            return depthCorners;

        return null;
    }

    private BlockPos getSingleCorner(BlockPos position, Vec3i addition) {
        Block nextBlock;

        // There must be at least one beam pipe, if not then immediately return null.
        if (!(getBlockAtPosition(position.add(addition)) instanceof ParticleAcceleratorBlockBeamPipe))
            return null;
        do {
            position = position.add(addition);
            nextBlock = getBlockAtPosition(position);
        }
        while (nextBlock instanceof ParticleAcceleratorBlockBeamPipe);
        return position.subtract(addition);
    }

    private boolean areCornerCoordinatesValid(List<BlockPos> cornerPositions) {
        int length1 = calculateCoordinateDistance(cornerPositions.get(FRONT_LEFT_CORNER_INDEX), cornerPositions.get(FRONT_RIGHT_CORNER_INDEX));
        int length2 = calculateCoordinateDistance(cornerPositions.get(BACK_LEFT_CORNER_INDEX), cornerPositions.get(BACK_RIGHT_CORNER_INDEX));
        int depth1 = calculateCoordinateDistance(cornerPositions.get(FRONT_LEFT_CORNER_INDEX), cornerPositions.get(BACK_LEFT_CORNER_INDEX));
        int depth2 = calculateCoordinateDistance(cornerPositions.get(FRONT_RIGHT_CORNER_INDEX), cornerPositions.get(BACK_RIGHT_CORNER_INDEX));
        return length1 - length2 + depth1 - depth2 == 0;
    }

    private Block getBlockAtPosition(BlockPos position) {
        return this.WORLD.getBlockState(position).getBlock();
    }

    private boolean areBlocksSymmetricalToController(BlockPos block1, BlockPos block2) {
        int distanceToController1 = calculateCoordinateDistance(block1, controllerBlockPosition);
        int distanceToController2 = calculateCoordinateDistance(block2, controllerBlockPosition);
        return distanceToController1 == distanceToController2;
    }

    private int calculateCoordinateDistance(BlockPos block1, BlockPos block2) {
        return (int) abs(block1.getDistance(block2.getX(), block2.getY(), block2.getZ()));
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
