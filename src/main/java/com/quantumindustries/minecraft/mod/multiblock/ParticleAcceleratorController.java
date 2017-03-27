package com.quantumindustries.minecraft.mod.multiblock;

import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.lib.block.ModTileEntity;
import it.zerono.mods.zerocore.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
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
    private int currentTargetBlockCount = 0;
    private int currentDetectorBlockCount = 0;
    private int currentBeamSourceCount = 0;
    private int currentPowerPortCount = 0;
    private int currentInputPortCount = 0;
    private int currentOutputPortCount = 0;
    private int currentControllerCount = 0;
    private int currentMagnetCount = 0;
    Class[] beamPipeCasingClasses = new Class[] {
            ParticleAcceleratorBlockMagnet.class,
            ParticleAcceleratorBlockWall.class,
            ParticleAcceleratorBlockDetector.class,
            ParticleAcceleratorBlockTarget.class
    };

    private static final int ACCELERATOR_SIZE_TOTAL = 27;
    private static final int ACCELERATOR_DIMENSIONAL_SIZE = 10;

    private static final int FRONT_LEFT_CORNER_INDEX = 0;
    private static final int FRONT_RIGHT_CORNER_INDEX = 1;
    private static final int BACK_LEFT_CORNER_INDEX = 2;
    private static final int BACK_RIGHT_CORNER_INDEX = 3;

    private static final int LEFT_BEAM_SOURCE_INDEX = 0;
    private static final int RIGHT_BEAM_SOURCE_INDEX = 1;

    private static final int EXPECTED_TARGET_COUNT = 8;
    private static final int EXPECTED_DETECTOR_COUNT = 16;
    private static final int EXPECTED_BEAM_SOURCE_COUNT = 2;
    private static final int EXPECTED_CONTROLLER_COUNT = 1;
    private static final int EXPECTED_POWER_PORT_COUNT = 1;
    private static final int EXPECTED_INPUT_PORT_COUNT = 1;
    private static final int EXPECTED_OUTPUT_PORT_COUNT = 1;

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

        if(this.isActive == active)
            return;

        // the state was changed, set it
        this.isActive = active;

        if(WorldHelper.calledByLogicalServer(this.WORLD)) {

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
        resetCounts();
        ParticleAcceleratorPowerTileEntity powerPortFound = null;
        ParticleAcceleratorIOPortTileEntity inputPortFound = null;
        ParticleAcceleratorIOPortTileEntity outputPortFound = null;
        ParticleAcceleratorControllerTileEntity controllerTE;
        List<BlockPos> beamSourcePositions;
        List<BlockPos> cornerPositions;
        try {

            for(IMultiblockPart part : this.connectedParts) {
                Block currentBlock = getBlockAtPosition(part.getWorldPosition());
                if(part instanceof ParticleAcceleratorControllerTileEntity && currentControllerCount == 0) {
                    ++currentControllerCount;
                    controllerBlockPosition = part.getWorldPosition();
                }
                else if(part instanceof ParticleAcceleratorControllerTileEntity && currentControllerCount == 1)
                    throw new InvalidControllerException("Found 2 controllers, there should only be 1.");
                else if(currentBlock instanceof ParticleAcceleratorBlockMagnet)
                    ++currentMagnetCount;
                else if(currentBlock instanceof ParticleAcceleratorBlockDetector)
                    ++currentDetectorBlockCount;
                else if(currentBlock instanceof ParticleAcceleratorBlockTarget)
                    ++currentTargetBlockCount;
                else if(currentBlock instanceof ParticleAcceleratorBlockBeamSource)
                    ++currentBeamSourceCount;
                else if(part instanceof ParticleAcceleratorPowerTileEntity) {
                    ++currentPowerPortCount;
                    powerPortFound = (ParticleAcceleratorPowerTileEntity) part;
                } else if(part instanceof ParticleAcceleratorIOPortTileEntity) {
                    ParticleAcceleratorIOPortTileEntity tempIO = (ParticleAcceleratorIOPortTileEntity) part;
                    if(tempIO.isInput()) {
                        ++currentInputPortCount;
                        inputPortFound = tempIO;
                    } else {
                        ++currentOutputPortCount;
                        outputPortFound = tempIO;
                    }
                }
            }

            checkBlockCounts();

            validateControllerTopBottom();

            // Find the beam sources.
            beamSourcePositions = getBeamSources(controllerBlockPosition);

            validateBeamSourcesTopBottom(beamSourcePositions.get(LEFT_BEAM_SOURCE_INDEX));
            validateBeamSourcesTopBottom(beamSourcePositions.get(RIGHT_BEAM_SOURCE_INDEX));

            // Find the corners of the accelerator.
            cornerPositions = getAllCorners(beamSourcePositions);
            int length = calculateDistanceBetweenCorners(
                    cornerPositions.get(FRONT_LEFT_CORNER_INDEX),
                    cornerPositions.get(FRONT_RIGHT_CORNER_INDEX)
            );
            int depth = calculateDistanceBetweenCorners(
                    cornerPositions.get(FRONT_LEFT_CORNER_INDEX),
                    cornerPositions.get(BACK_LEFT_CORNER_INDEX)
            );

            if(length < 9)
                throw new InvalidShapeException("Particle Accelerator must have a length of at least 9.");
            else if(depth < 5)
                throw new InvalidShapeException("Particle Accelerator must have a depth of at least 5.");

            validateAcceleratorSides(cornerPositions, beamSourcePositions);

            BlockPos backMidPoint = new BlockPos(
                    (cornerPositions.get(BACK_LEFT_CORNER_INDEX).getX() + cornerPositions.get(BACK_RIGHT_CORNER_INDEX).getX())/2,
                    (cornerPositions.get(BACK_LEFT_CORNER_INDEX).getY() + cornerPositions.get(BACK_RIGHT_CORNER_INDEX).getY())/2,
                    (cornerPositions.get(BACK_LEFT_CORNER_INDEX).getZ() + cornerPositions.get(BACK_RIGHT_CORNER_INDEX).getZ())/2
            );

            validateTargetBlocks(backMidPoint);
            validateDetectorBlocks(backMidPoint);

            validateExpectedTotalBlocks(cornerPositions);

            // TODO: Remove this warning and have the function return true
            FMLLog.warning("[%s] Sides are valid", this.WORLD.isRemote ? "CLIENT" : "SERVER");
            return false;
        }
        catch(Exception e) {
            // TODO: clean this up so the player can get chat messages and we don't output warnings.
            FMLLog.warning(e.getMessage());
            resetCounts();
            return false;
        }
    }

    private void validateExpectedTotalBlocks(List<BlockPos> corners) {
        int totalBlocks = 0;
        int length = calculateDistanceBetweenCorners(
                corners.get(BACK_LEFT_CORNER_INDEX),
                corners.get(BACK_RIGHT_CORNER_INDEX)
        ) - 1;
        int depth = calculateDistanceBetweenCorners(
                corners.get(FRONT_LEFT_CORNER_INDEX),
                corners.get(BACK_LEFT_CORNER_INDEX)
        ) - 1;

        totalBlocks += 4*12;
        totalBlocks += (length - 3) * 9 + 9;
        totalBlocks += (length) * 9;
        totalBlocks += depth * 9 * 2;
        totalBlocks -= 12;

        if(totalBlocks != this.connectedParts.size())
            throw new InvalidShapeException(
                    "Found too many blocks for current accelerator size, " +
                    "maybe there is an extra block connected somewhere?"
            );
    }

    private int calculateDistanceBetweenCorners(BlockPos start, BlockPos end) {
        return (int) start.getDistance(end.getX(), end.getY(), end.getZ());
    }

    private void resetCounts() {
        currentTargetBlockCount = 0;
        currentDetectorBlockCount = 0;
        currentBeamSourceCount = 0;
        currentPowerPortCount = 0;
        currentInputPortCount = 0;
        currentOutputPortCount = 0;
        currentControllerCount = 0;
        currentMagnetCount = 0;
    }

    private void checkBlockCounts() {
        if(currentDetectorBlockCount != EXPECTED_DETECTOR_COUNT)
            throw new InvalidBlockCountException("Incorrect number of Detectors, expected 16.");
        if(currentTargetBlockCount != EXPECTED_TARGET_COUNT)
            throw new InvalidBlockCountException("Incorrect number of Targets, expected 8.");
        if(currentBeamSourceCount != EXPECTED_BEAM_SOURCE_COUNT)
            throw new InvalidBlockCountException("Incorrect number of Beam Sources, expected 2.");
        if(currentControllerCount != EXPECTED_CONTROLLER_COUNT)
            throw new InvalidBlockCountException("Incorrect number of Controllers, expected 1.");
        if(currentPowerPortCount < EXPECTED_POWER_PORT_COUNT)
            throw new InvalidBlockCountException("Incorrect number of Power Ports, expected at least 1.");
        if(currentInputPortCount < EXPECTED_INPUT_PORT_COUNT)
            throw new InvalidBlockCountException("Incorrect number of Input Ports, expected at least 1.");
        if(currentOutputPortCount < EXPECTED_OUTPUT_PORT_COUNT)
            throw new InvalidBlockCountException("Incorrect number of Output Ports, expected at least 1.");
    }

    private void validateControllerTopBottom() {
        if(!(getBlockAtPosition(controllerBlockPosition.up()) instanceof ParticleAcceleratorBlockBase))
            throw new InvalidControllerException("Invalid block found above controller.");
        else if(!(getBlockAtPosition(controllerBlockPosition.down()) instanceof ParticleAcceleratorBlockBase))
            throw new InvalidControllerException("Invalid block found below controller.");
    }

    private void validateBeamSourcesTopBottom(BlockPos beamSource) {
        if(!(getBlockAtPosition(beamSource.up()) instanceof ParticleAcceleratorBlockBase))
            throw new InvalidBeamSourceException("Invalid block found above beam source.");
        else if(!(getBlockAtPosition(beamSource.down()) instanceof ParticleAcceleratorBlockBase))
            throw new InvalidBeamSourceException("Invalid block found below beam source.");
    }

    private void validateTargetBlocks(BlockPos midPoint) {
        if(midPoint.getX() == controllerBlockPosition.getX()) {
            validateAroundPipeZDir(midPoint, ParticleAcceleratorBlockTarget.class);
        }
        else {
            validateAroundPipeXDir(midPoint, ParticleAcceleratorBlockTarget.class);
        }
    }

    private void validateDetectorBlocks(BlockPos midPoint) {
        if(midPoint.getX() == controllerBlockPosition.getX()) {
            Vec3i xVector = new Vec3i(1,0,0);
            validateAroundPipeZDir(midPoint.add(xVector), ParticleAcceleratorBlockDetector.class);
            validateAroundPipeZDir(midPoint.subtract(xVector), ParticleAcceleratorBlockDetector.class);
        }
        else {
            Vec3i zVector = new Vec3i(0,0,1);
            validateAroundPipeXDir(midPoint.add(zVector), ParticleAcceleratorBlockDetector.class);
            validateAroundPipeXDir(midPoint.subtract(zVector), ParticleAcceleratorBlockDetector.class);
        }
    }

    private List<BlockPos> getBeamSources(BlockPos controllerPosition) {
        List<BlockPos> tempSources = new ArrayList<BlockPos>(2);
        boolean sourceXDir = isBeamSourceXDirection(controllerPosition);
        boolean sourceZDir = isBeamSourceZDirection(controllerPosition);
        if(sourceXDir && sourceZDir) {
            throw new InvalidBeamSourceException("Beam sources found in both Z and X directions.");
        }
        else if(sourceXDir) {
            Vec3i xVector = new Vec3i(1,0,0);
            tempSources.add(controllerPosition.add(xVector));
            tempSources.add(controllerPosition.subtract(xVector));
            return tempSources;
        }
        else if(sourceZDir) {
            Vec3i zVector = new Vec3i(0,0,1);
            tempSources.add(controllerPosition.add(zVector));
            tempSources.add(controllerPosition.subtract(zVector));
            return tempSources;
        }
        throw new InvalidBeamSourceException("Beam Sources not found.");
    }

    private boolean isBeamSourceXDirection(BlockPos position) {
        Vec3i xVector = new Vec3i(1,0,0);
        return (getBlockAtPosition(position.add(xVector)) instanceof ParticleAcceleratorBlockBeamSource &&
                getBlockAtPosition(position.subtract(xVector))instanceof ParticleAcceleratorBlockBeamSource);
    }

    private boolean isBeamSourceZDirection(BlockPos position) {
        Vec3i zVector = new Vec3i(0,0,1);
        return (getBlockAtPosition(position.add(zVector)) instanceof ParticleAcceleratorBlockBeamSource &&
                getBlockAtPosition(position.subtract(zVector)) instanceof ParticleAcceleratorBlockBeamSource);
    }

    private void validateAcceleratorSides(List<BlockPos> corners, List<BlockPos> beamSources) {
        // Check Front Left and Right
        validateSide(beamSources.get(LEFT_BEAM_SOURCE_INDEX), corners.get(FRONT_LEFT_CORNER_INDEX));
        validateSide(beamSources.get(RIGHT_BEAM_SOURCE_INDEX), corners.get(FRONT_RIGHT_CORNER_INDEX));

        // Check the depth-wise sides
        validateSide(corners.get(FRONT_LEFT_CORNER_INDEX), corners.get(BACK_LEFT_CORNER_INDEX));
        validateSide(corners.get(FRONT_RIGHT_CORNER_INDEX), corners.get(BACK_RIGHT_CORNER_INDEX));

        // Check the back side
        validateBackSide(corners.get(BACK_LEFT_CORNER_INDEX), corners.get(BACK_RIGHT_CORNER_INDEX));
    }

    private void validateSide(BlockPos startCorner, BlockPos endCorner) {
        if(checkBlockZCoordinateEquality(endCorner, startCorner)) {
            validateSingleSideXDir(
                    startCorner,
                    endCorner
            );
        }
        else {
            validateSingleSideZDir(
                    startCorner,
                    endCorner
            );
        }
    }

    // TODO: Change this to validate special cases for back side (detector and target)
    private void validateBackSide(BlockPos startCorner, BlockPos endCorner) {
        if(checkBlockZCoordinateEquality(endCorner, startCorner)) {
            validateSingleSideXDir(
                    startCorner,
                    endCorner
            );
        }
        else {
            validateSingleSideZDir(
                    startCorner,
                    endCorner
            );
        }
    }

    private void validateSingleSideXDir(BlockPos startCorner, BlockPos endCorner) {
        int endingX = abs(startCorner.getX() - endCorner.getX()) + 1;
        Vec3i xVector;
        BlockPos current = startCorner;
        if(startCorner.getX() < endCorner.getX())
            xVector = new Vec3i(1, 0, 0);
        else
            xVector = new Vec3i(-1, 0, 0);
        for(int x = 0; x <= endingX; ++x) {
            BlockPos block = new BlockPos(current);
            if(x == 0) {
                // DO NOTHING
            }
            else if(x == (endingX - 1)) {
                if(getBlockAtPosition(block) instanceof ParticleAcceleratorBlockBeamPipe)
                    validateAroundPipeZDirCorner(block);
            }
            else if(x == endingX) {
                if(getBlockAtPosition(block) instanceof ParticleAcceleratorBlockWall &&
                        getBlockAtPosition(block.up()) instanceof ParticleAcceleratorBlockWall)
                    validateAroundPipeZDir(block, getBlockAtPosition(block.up()).getClass());
                else
                    throw new InvalidSurroundBeamPipeException("Invalid blocks surrounding beam pipe corner.");
            }
            else {
                if(getBlockAtPosition(block.up()) instanceof BlockAir)
                    throw new InvalidSurroundBeamPipeException("[xDir] Air block found around beam pipe(up).");
                else if(!(getBlockAtPosition(block.up()) instanceof ParticleAcceleratorBlockBase))
                    throw new InvalidSurroundBeamPipeException("Block is not a valid accelerator block");
                else if(!(getBlockAtPosition(block) instanceof ParticleAcceleratorBlockBeamPipe))
                    throw new InvalidBeamPipeException("Missing beam pipe");
                else
                    validateAroundPipeZDir(block, getBlockAtPosition(block.up()).getClass());
            }
            current = current.add(xVector);
        }
    }

    private void validateSingleSideZDir(BlockPos startCorner, BlockPos endCorner) {
        int endingZ = abs(startCorner.getZ() - endCorner.getZ()) + 1;
        Vec3i zVector;
        BlockPos current = startCorner;
        if(startCorner.getZ() < endCorner.getZ())
            zVector = new Vec3i(0, 0, 1);
        else
            zVector = new Vec3i(0, 0, -1);
        for(int z = 0; z <= endingZ; ++z) {
            BlockPos block = new BlockPos(current);
            if(z == 0) {
                // DO NOTHING
            }
            else if(z == (endingZ - 1)) {
                if(getBlockAtPosition(block) instanceof ParticleAcceleratorBlockBeamPipe)
                    validateAroundPipeXDirCorner(block);
            }
            else if(z == endingZ) {
                if(getBlockAtPosition(block) instanceof ParticleAcceleratorBlockWall &&
                        getBlockAtPosition(block.up()) instanceof ParticleAcceleratorBlockWall)
                    validateAroundPipeXDir(block, getBlockAtPosition(block.up()).getClass());
                else
                    throw new InvalidSurroundBeamPipeException("Invalid blocks surrounding beam pipe corner.");
            }
            else {
                if(getBlockAtPosition(block.up()) instanceof BlockAir)
                    throw new InvalidSurroundBeamPipeException("[zDir] Air block found around beam pipe(up).");
                else if(!(getBlockAtPosition(block.up()) instanceof ParticleAcceleratorBlockBase))
                    throw new InvalidSurroundBeamPipeException("Block is not a valid accelerator block");
                else if(!(getBlockAtPosition(block) instanceof ParticleAcceleratorBlockBeamPipe))
                    throw new InvalidBeamPipeException("Missing beam pipe");
                else
                    validateAroundPipeXDir(block, getBlockAtPosition(block.up()).getClass());
            }
            current = current.add(zVector);
        }
    }

    private List<BlockPos> getAllCorners(List<BlockPos> beamPositions) {
        List<BlockPos> lengthCorners = new ArrayList<BlockPos>(2);
        List<BlockPos> depthCorners = new ArrayList<BlockPos>(2);
        List<BlockPos> cornerPositions = new ArrayList<BlockPos>(4);
        Vec3i positiveXVector = new Vec3i(1, 0, 0);
        Vec3i negativeXVector = new Vec3i(-1, 0, 0);
        Vec3i positiveZVector = new Vec3i(0, 0, 1);
        Vec3i negativeZVector = new Vec3i(0, 0, -1);

        if(beamPositions.get(LEFT_BEAM_SOURCE_INDEX).getZ() ==
                beamPositions.get(RIGHT_BEAM_SOURCE_INDEX).getZ()) {
            lengthCorners = calculateLengthCorners(beamPositions, positiveXVector, negativeXVector);

            Block frontLeft = getBlockAtPosition(lengthCorners.get(FRONT_LEFT_CORNER_INDEX).add(positiveZVector));
            Block frontRight = getBlockAtPosition(lengthCorners.get(FRONT_RIGHT_CORNER_INDEX).add(positiveZVector));
            if(frontLeft instanceof ParticleAcceleratorBlockBeamPipe &&
                    frontRight instanceof ParticleAcceleratorBlockBeamPipe) {
                depthCorners = calculateDepthCorners(lengthCorners, positiveZVector);
            }
            else {
                depthCorners = calculateDepthCorners(lengthCorners, negativeZVector);
            }
        }
        else if(beamPositions.get(LEFT_BEAM_SOURCE_INDEX).getX() ==
                beamPositions.get(RIGHT_BEAM_SOURCE_INDEX).getX()) {
            lengthCorners = calculateLengthCorners(beamPositions, positiveZVector, negativeZVector);

            Block frontLeft = getBlockAtPosition(lengthCorners.get(FRONT_LEFT_CORNER_INDEX).add(positiveXVector));
            Block frontRight = getBlockAtPosition(lengthCorners.get(FRONT_RIGHT_CORNER_INDEX).add(positiveXVector));
            if(frontLeft instanceof ParticleAcceleratorBlockBeamPipe &&
                    frontRight instanceof ParticleAcceleratorBlockBeamPipe) {
                depthCorners = calculateDepthCorners(lengthCorners, positiveXVector);
            }
            else {
                depthCorners = calculateDepthCorners(lengthCorners, negativeXVector);
            }
        }

        cornerPositions.addAll(lengthCorners);
        cornerPositions.addAll(depthCorners);

        if(!areCornerCoordinatesValid(cornerPositions))
            throw new InvalidShapeException("Not a valid rectangle according to the corners.");

        return cornerPositions;
    }

    private List<BlockPos> calculateLengthCorners(List<BlockPos> beamPositions, Vec3i positiveLength, Vec3i negativeLength) {
        List<BlockPos> lengthCorners = new ArrayList<BlockPos>(2);
        BlockPos leftCorner = getSingleCorner(beamPositions.get(LEFT_BEAM_SOURCE_INDEX), positiveLength);
        BlockPos rightCorner = getSingleCorner(beamPositions.get(RIGHT_BEAM_SOURCE_INDEX), negativeLength);

        lengthCorners.add(FRONT_LEFT_CORNER_INDEX, leftCorner);
        lengthCorners.add(FRONT_RIGHT_CORNER_INDEX, rightCorner);

        if(areBlocksSymmetricalToController(lengthCorners.get(0), lengthCorners.get(1)))
            return lengthCorners;

        throw new InvalidShapeException("Length corners are not symmetrical around controller.");
    }

    private List<BlockPos> calculateDepthCorners(List<BlockPos> lengthCorners, Vec3i depthVector) {
        List<BlockPos> depthCorners = new ArrayList<BlockPos>(2);
        BlockPos leftCorner = getSingleCorner(lengthCorners.get(FRONT_LEFT_CORNER_INDEX), depthVector);
        BlockPos rightCorner = getSingleCorner(lengthCorners.get(FRONT_RIGHT_CORNER_INDEX), depthVector);

        depthCorners.add(leftCorner);
        depthCorners.add(rightCorner);

        if(areBlocksSymmetricalToController(depthCorners.get(0), depthCorners.get(1)))
            return depthCorners;

        throw new InvalidShapeException("Depth corners are not symmetrical around controller.");
    }

    private BlockPos getSingleCorner(BlockPos position, Vec3i addition) {
        Block nextBlock;

        // There must be at least one beam pipe, if not then immediately return null.
        if(!(getBlockAtPosition(position.add(addition)) instanceof ParticleAcceleratorBlockBeamPipe))
            throw new InvalidBeamPipeException("Must have beam pipe adjacent to beam source.");
        do {
            position = position.add(addition);
            nextBlock = getBlockAtPosition(position);
        }
        while(nextBlock instanceof ParticleAcceleratorBlockBeamPipe);
        return position.subtract(addition);
    }

    private boolean validateAroundPipeXDir(BlockPos centerPosition, Class classType) {
        List<Block> surroundingBlocks = new ArrayList<Block>(8);
        surroundingBlocks.add(getBlockAtPosition(centerPosition.up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.down()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.east()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.west()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.east().up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.east().down()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.west().up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.west().down()));
        for(int block = 0; block < 8; ++block) {
            if(!(surroundingBlocks.get(block).getClass().isAssignableFrom(classType)))
                throw new InvalidSurroundBeamPipeException("Block surrounding beam pipe does not match the expected type.");
        }
        return true;
    }

    private void validateAroundPipeXDirCorner(BlockPos centerPosition) {
        List<Block> surroundingBlocks = new ArrayList<Block>(6);
        List<Block> sideBlocks = new ArrayList<Block>(2);
        surroundingBlocks.add(getBlockAtPosition(centerPosition.up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.down()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.east().up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.east().down()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.west().up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.west().down()));
        for(int block = 0; block < surroundingBlocks.size(); ++block) {
            if(!(surroundingBlocks.get(block) instanceof ParticleAcceleratorBlockWall))
                throw new InvalidSurroundBeamPipeException("Block surrounding beam pipe does not match the expected type at corner.");
        }
        int beamPipeCount = 0;
        sideBlocks.add(getBlockAtPosition(centerPosition.east()));
        sideBlocks.add(getBlockAtPosition(centerPosition.west()));
        for(int block = 0; block < sideBlocks.size(); ++block) {
            if(sideBlocks.get(block) instanceof ParticleAcceleratorBlockBeamPipe)
                ++beamPipeCount;
            else if(sideBlocks.get(block) instanceof ParticleAcceleratorBlockWall)
                continue;
            else
                throw new InvalidSurroundBeamPipeException("Invalid block surrounding beam pipe corner.");
        }

        if(beamPipeCount != 1)
            throw new InvalidBeamPipeException("Missing beam pipe at corner turn.");
    }

    private boolean validateAroundPipeZDir(BlockPos centerPosition, Class classType) {
        List<Block> surroundingBlocks = new ArrayList<Block>(8);
        surroundingBlocks.add(getBlockAtPosition(centerPosition.up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.down()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.north()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.south()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.north().up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.north().down()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.south().up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.south().down()));
        for(int block = 0; block < 8; ++block) {
            if(!(surroundingBlocks.get(block).getClass().isAssignableFrom(classType)))
                throw new InvalidSurroundBeamPipeException(
                        "Block surrounding beam pipe does not match the expected type.");
        }
        return true;
    }

    private void validateAroundPipeZDirCorner(BlockPos centerPosition) {
        List<Block> surroundingBlocks = new ArrayList<Block>(6);
        List<Block> sideBlocks = new ArrayList<Block>(2);
        surroundingBlocks.add(getBlockAtPosition(centerPosition.up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.down()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.north().up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.north().down()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.south().up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.south().down()));
        for(int block = 0; block < surroundingBlocks.size(); ++block) {
            if(!(surroundingBlocks.get(block) instanceof ParticleAcceleratorBlockWall))
                throw new InvalidSurroundBeamPipeException(
                        "Block surrounding beam pipe does not match the expected type at corner.");
        }
        int beamPipeCount = 0;
        sideBlocks.add(getBlockAtPosition(centerPosition.north()));
        sideBlocks.add(getBlockAtPosition(centerPosition.south()));
        for(int block = 0; block < sideBlocks.size(); ++block) {
            if(sideBlocks.get(block) instanceof ParticleAcceleratorBlockBeamPipe)
                ++beamPipeCount;
            else if(sideBlocks.get(block) instanceof ParticleAcceleratorBlockWall)
                continue;
            else
                throw new InvalidSurroundBeamPipeException("Invalid block surrounding beam pipe corner.");
        }

        if(beamPipeCount != 1)
            throw new InvalidBeamPipeException("Missing beam pipe at corner turn.");
    }

    private boolean areCornerCoordinatesValid(List<BlockPos> cornerPositions) {
        int length1 = calculateCoordinateDistance(
                cornerPositions.get(FRONT_LEFT_CORNER_INDEX),
                cornerPositions.get(FRONT_RIGHT_CORNER_INDEX)
        );
        int length2 = calculateCoordinateDistance(
                cornerPositions.get(BACK_LEFT_CORNER_INDEX),
                cornerPositions.get(BACK_RIGHT_CORNER_INDEX)
        );
        int depth1 = calculateCoordinateDistance(
                cornerPositions.get(FRONT_LEFT_CORNER_INDEX),
                cornerPositions.get(BACK_LEFT_CORNER_INDEX)
        );
        int depth2 = calculateCoordinateDistance(
                cornerPositions.get(FRONT_RIGHT_CORNER_INDEX),
                cornerPositions.get(BACK_RIGHT_CORNER_INDEX)
        );
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

    private boolean checkBlockZCoordinateEquality(BlockPos start, BlockPos end) {
        return (start.getZ() - end.getZ()) == 0;
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

    // -----------------------------------------------------------------------
    // Exception classes
    // -----------------------------------------------------------------------

    public class InvalidControllerException extends RuntimeException {
        InvalidControllerException(String message) {
            super(message);
        }
    }

    public class InvalidBeamSourceException extends RuntimeException {
        InvalidBeamSourceException(String message) {
            super(message);
        }
    }

    public class InvalidShapeException extends RuntimeException {
        InvalidShapeException(String message) {
            super(message);
        }
    }

    public class InvalidBeamPipeException extends RuntimeException {
        InvalidBeamPipeException(String message) {
            super(message);
        }
    }

    public class InvalidSurroundBeamPipeException extends RuntimeException {
        InvalidSurroundBeamPipeException(String message) {
            super(message);
        }
    }

    public class InvalidBlockCountException extends RuntimeException {
        InvalidBlockCountException(String message) {
            super(message);
        }
    }
}
