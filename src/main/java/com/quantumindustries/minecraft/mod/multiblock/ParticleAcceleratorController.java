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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.lang.Math.abs;

public class ParticleAcceleratorController extends MultiblockControllerBase {

    private ParticleAcceleratorPowerTileEntity powerPort;
    private ParticleAcceleratorIOPortTileEntity inputPort;
    private ParticleAcceleratorIOPortTileEntity outputPort;
    private ParticleAcceleratorControllerTileEntity controllerBlock;
    private BlockPos controllerBlockPosition;
    private boolean isActive;

    private CurrentCounts currentCounts;
    private ActualAcceleratorSizes actualAcceleratorSizes;

    private static final int ACCELERATOR_SIZE_TOTAL = 27;
    private static final int ACCELERATOR_DIMENSIONAL_SIZE = 10;

    public ParticleAcceleratorController(World world) {
        super(world);

        powerPort = null;
        inputPort = null;
        outputPort = null;
        controllerBlock = null;
        isActive = false;
        actualAcceleratorSizes = new ActualAcceleratorSizes();
    }

    public boolean isActive() {
        return isActive;
    }

    public void toggleActive() {
        setActive(!isActive);
    }

    public void setActive(boolean active) {
        if(isActive == active) {
            return;
        }

        // the state was changed, set it
        isActive = active;

        if(WorldHelper.calledByLogicalServer(WORLD)) {
            // on the server side, request an update to be sent to the client and mark the save delegate as dirty
            markReferenceCoordForUpdate();
            markReferenceCoordDirty();

        }
        else {
            // on the client, request a render update
            markMultiblockForRenderUpdate();
        }
    }

    @Override
    public void onAttachedPartWithMultiblockData(IMultiblockPart iMultiblockPart, NBTTagCompound nbtTagCompound) {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    protected void onBlockAdded(IMultiblockPart iMultiblockPart) {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    protected void onBlockRemoved(IMultiblockPart iMultiblockPart) {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    protected void onMachineAssembled() {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    protected void onMachineRestored() {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    protected void onMachinePaused() {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    protected void onMachineDisassembled() {
        // TODO(CM): This function in particular needs to be written to make sure all proper variables have been reset.
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
        ParticleAcceleratorControllerTileEntity controllerFound = null;
        List<ParticleAcceleratorIOPortTileEntity> outputPortsFound = new ArrayList<ParticleAcceleratorIOPortTileEntity>();
        List<ParticleAcceleratorIOPortTileEntity> inputPortsFound = new ArrayList<ParticleAcceleratorIOPortTileEntity>();

        List<BlockPos> beamSourcePositions;
        List<BlockPos> cornerPositions;

        try {
            for(IMultiblockPart part : connectedParts) {
                Block currentBlock = getBlockAtPosition(part.getWorldPosition());
                if(part instanceof ParticleAcceleratorControllerTileEntity && currentCounts.controllerBlock == 0) {
                    ++currentCounts.controllerBlock;
                    controllerBlockPosition = part.getWorldPosition();
                    controllerFound = (ParticleAcceleratorControllerTileEntity) part;
                }
                else if(part instanceof ParticleAcceleratorControllerTileEntity && currentCounts.controllerBlock == 1) {
                    throw new InvalidControllerException("Found 2 controllers, there should only be 1.");
                }
                else if(currentBlock instanceof ParticleAcceleratorBlockMagnet) {
                    ++currentCounts.magnetBlock;
                }
                else if(currentBlock instanceof ParticleAcceleratorBlockDetector) {
                    ++currentCounts.detectorBlock;
                }
                else if(currentBlock instanceof ParticleAcceleratorBlockTarget) {
                    ++currentCounts.targetBlock;
                }
                else if(currentBlock instanceof ParticleAcceleratorBlockBeamSource) {
                    ++currentCounts.beamSource;
                }
                else if(part instanceof ParticleAcceleratorPowerTileEntity) {
                    ++currentCounts.powerPort;
                    powerPortFound = (ParticleAcceleratorPowerTileEntity) part;
                }
                else if(part instanceof ParticleAcceleratorIOPortTileEntity) {
                    ParticleAcceleratorIOPortTileEntity tempIO = (ParticleAcceleratorIOPortTileEntity) part;
                    if(tempIO.isInput()) {
                        ++currentCounts.inputPort;
                        inputPortFound = tempIO;
                    }
                    else {
                        ++currentCounts.outputPort;
                        outputPortFound = tempIO;
                    }
                }
            }


            validateBlockCounts();

            validateControllerTopBottom();

            beamSourcePositions = getBeamSources(controllerBlockPosition);

            validateBeamSourcesTopBottom(beamSourcePositions.get(BeamSourceIndices.LEFT));
            validateBeamSourcesTopBottom(beamSourcePositions.get(BeamSourceIndices.RIGHT));

            cornerPositions = getAllCorners(beamSourcePositions);

            actualAcceleratorSizes.lengthPipe = calculateDistanceBetweenCorners(
                    cornerPositions.get(CornerIndices.FRONT_LEFT),
                    cornerPositions.get(CornerIndices.FRONT_RIGHT)
            ) - 1;

            actualAcceleratorSizes.depthPipe = calculateDistanceBetweenCorners(
                    cornerPositions.get(CornerIndices.FRONT_LEFT),
                    cornerPositions.get(CornerIndices.BACK_LEFT)
            ) - 1;

            if(actualAcceleratorSizes.lengthPipe < ExpectedMinimumAcceleratorSizes.LENGTH_OF_PIPE) {
                throw new InvalidShapeException("Particle Accelerator must have a length of at least 9.");
            }
            else if(actualAcceleratorSizes.depthPipe < ExpectedMinimumAcceleratorSizes.DEPTH_OF_PIPE) {
                throw new InvalidShapeException("Particle Accelerator must have a depth of at least 5.");
            }

            validateAcceleratorSides(cornerPositions, beamSourcePositions);

            BlockPos backMidPoint = new BlockPos(
                    (cornerPositions.get(CornerIndices.BACK_LEFT).getX() + cornerPositions.get(CornerIndices.BACK_RIGHT).getX())/2,
                    (cornerPositions.get(CornerIndices.BACK_LEFT).getY() + cornerPositions.get(CornerIndices.BACK_RIGHT).getY())/2,
                    (cornerPositions.get(CornerIndices.BACK_LEFT).getZ() + cornerPositions.get(CornerIndices.BACK_RIGHT).getZ())/2
            );

            validateTargetBlocks(backMidPoint);
            validateDetectorBlocks(backMidPoint);

            validateExpectedTotalBlocks(cornerPositions);

            // TODO(CM): While this function returns true, we don't do anything with the fact that the machine is now whole.
            FMLLog.warning("[%s] Sides are valid", WORLD.isRemote ? "CLIENT" : "SERVER");

            // Set class variables for Tile Entities
            powerPort = powerPortFound;
            inputPort = inputPortFound;
            outputPort = outputPortFound;
            controllerBlock = controllerFound;

            return true;
        }
        catch(Exception e) {
            // TODO(CM): clean this up so the player can get chat messages and we don't output warnings.
            FMLLog.warning(e.getMessage());
            resetCounts();
            return false;
        }
    }

    private void resetCounts() {
        currentCounts = new CurrentCounts();
    }

    private void validateExpectedTotalBlocks(List<BlockPos> corners) {
        int totalBlocks = 0;

        // Length along the front (controller side) and back (target/detector side).
        int length = calculateDistanceBetweenCorners(
                corners.get(CornerIndices.BACK_LEFT),
                corners.get(CornerIndices.BACK_RIGHT)
        ) - 1;

        // Length along the depth or sides of the PA.
        int depth = calculateDistanceBetweenCorners(
                corners.get(CornerIndices.FRONT_LEFT),
                corners.get(CornerIndices.BACK_LEFT)
        ) - 1;

        // The code below calculates the total blocks that should be present in a correct PA given
        // the size (calculated according to the corner positions).

        // 4 Corners have 12 blocks around the corner.
        totalBlocks += 4*12;

        // Add blocks along front face. Do length-3 because beamsource-controller-beamsource is 3 wide
        // Multiply by 9 because all beam pipe is surrounded by 8 blocks plus itself add 9 for controller section.
        totalBlocks += (length - 3) * 9 + 9;

        // Add blocks along back side which is just length*9.
        totalBlocks += (length) * 9;

        // Add blocks along sides which is just depth*9 and then times 2 because 2 sides.
        totalBlocks += depth * 9 * 2;

        // Subtract 12 because counted 12 blocks twice around corners so we correct it here.
        totalBlocks -= 12;

        if(totalBlocks != connectedParts.size()) {
            throw new InvalidShapeException(
                    "Found too many blocks for current accelerator size, " +
                            "maybe there is an extra block connected somewhere?"
            );
        }
        actualAcceleratorSizes.total = totalBlocks;
    }

    private int calculateDistanceBetweenCorners(BlockPos start, BlockPos end) {
        return (int) start.getDistance(end.getX(), end.getY(), end.getZ());
    }

    private void validateBlockCounts() {
        if(currentCounts.detectorBlock != ExpectedCounts.DETECTOR) {
            throw new InvalidBlockCountException("Incorrect number of Detectors, expected 16.");
        }
        if(currentCounts.targetBlock != ExpectedCounts.TARGET) {
            throw new InvalidBlockCountException("Incorrect number of Targets, expected 8.");
        }
        if(currentCounts.beamSource != ExpectedCounts.BEAM_SOURCE) {
            throw new InvalidBlockCountException("Incorrect number of Beam Sources, expected 2.");
        }
        if(currentCounts.controllerBlock != ExpectedCounts.CONTROLLER) {
            throw new InvalidBlockCountException("Incorrect number of Controllers, expected 1.");
        }
        if(currentCounts.powerPort < ExpectedCounts.POWER_PORT) {
            throw new InvalidBlockCountException("Incorrect number of Power Ports, expected at least 1.");
        }
        if(currentCounts.inputPort < ExpectedCounts.INPUT_PORT) {
            throw new InvalidBlockCountException("Incorrect number of Input Ports, expected at least 1.");
        }
        if(currentCounts.outputPort < ExpectedCounts.OUTPUT_PORT) {
            throw new InvalidBlockCountException("Incorrect number of Output Ports, expected at least 1.");
        }
    }

    private void validateControllerTopBottom() {
        if(!(getBlockAtPosition(controllerBlockPosition.up()) instanceof ParticleAcceleratorBlockBase)) {
            throw new InvalidControllerException("Invalid block found above controller.");
        }
        else if(!(getBlockAtPosition(controllerBlockPosition.down()) instanceof ParticleAcceleratorBlockBase)) {
            throw new InvalidControllerException("Invalid block found below controller.");
        }
    }

    private void validateBeamSourcesTopBottom(BlockPos beamSource) {
        if(!(getBlockAtPosition(beamSource.up()) instanceof ParticleAcceleratorBlockBase)) {
            throw new InvalidBeamSourceException("Invalid block found above beam source.");
        }
        else if(!(getBlockAtPosition(beamSource.down()) instanceof ParticleAcceleratorBlockBase)) {
            throw new InvalidBeamSourceException("Invalid block found below beam source.");
        }
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
            Vec3i xVector = new Vec3i(1, 0, 0);
            validateAroundPipeZDir(midPoint.add(xVector), ParticleAcceleratorBlockDetector.class);
            validateAroundPipeZDir(midPoint.subtract(xVector), ParticleAcceleratorBlockDetector.class);
        }
        else {
            Vec3i zVector = new Vec3i(0, 0, 1);
            validateAroundPipeXDir(midPoint.add(zVector), ParticleAcceleratorBlockDetector.class);
            validateAroundPipeXDir(midPoint.subtract(zVector), ParticleAcceleratorBlockDetector.class);
        }
    }

    private List<BlockPos> getBeamSources(BlockPos controllerPosition) {
        List<BlockPos> sources = new ArrayList<BlockPos>(2);
        boolean sourceXDir = isBeamSourceXDirection(controllerPosition);
        boolean sourceZDir = isBeamSourceZDirection(controllerPosition);
        if(sourceXDir && sourceZDir) {
            throw new InvalidBeamSourceException("Beam sources found in both Z and X directions.");
        }
        else if(sourceXDir) {
            Vec3i xVector = new Vec3i(1, 0, 0);
            sources.add(controllerPosition.add(xVector));
            sources.add(controllerPosition.subtract(xVector));
            return sources;
        }
        else if(sourceZDir) {
            Vec3i zVector = new Vec3i(0, 0, 1);
            sources.add(controllerPosition.add(zVector));
            sources.add(controllerPosition.subtract(zVector));
            return sources;
        }
        throw new InvalidBeamSourceException("Beam Sources not found.");
    }

    private boolean isBeamSourceXDirection(BlockPos position) {
        Vec3i xVector = new Vec3i(1, 0, 0);
        return getBlockAtPosition(position.add(xVector)) instanceof ParticleAcceleratorBlockBeamSource &&
                getBlockAtPosition(position.subtract(xVector)) instanceof ParticleAcceleratorBlockBeamSource;
    }

    private boolean isBeamSourceZDirection(BlockPos position) {
        Vec3i zVector = new Vec3i(0, 0, 1);
        return getBlockAtPosition(position.add(zVector)) instanceof ParticleAcceleratorBlockBeamSource &&
                getBlockAtPosition(position.subtract(zVector)) instanceof ParticleAcceleratorBlockBeamSource;
    }

    private void validateAcceleratorSides(List<BlockPos> corners, List<BlockPos> beamSources) {
        // Check Front Left and Right
        validateSide(beamSources.get(BeamSourceIndices.LEFT), corners.get(CornerIndices.FRONT_LEFT));
        validateSide(beamSources.get(BeamSourceIndices.RIGHT), corners.get(CornerIndices.FRONT_RIGHT));

        // Check the depth-wise sides
        validateSide(corners.get(CornerIndices.FRONT_LEFT), corners.get(CornerIndices.BACK_LEFT));
        validateSide(corners.get(CornerIndices.FRONT_RIGHT), corners.get(CornerIndices.BACK_RIGHT));

        // Check the back side
        validateSide(corners.get(CornerIndices.BACK_LEFT), corners.get(CornerIndices.BACK_RIGHT));
    }

    private void validateSide(BlockPos startCorner, BlockPos endCorner) {
        if(zCoordinatesAreEqual(endCorner, startCorner)) {
            validateSingleSideXDir(startCorner, endCorner);
        }
        else {
            validateSingleSideZDir(startCorner, endCorner);
        }
    }

    private void validateSingleSideXDir(BlockPos startCorner, BlockPos endCorner) {
        int numBlocks = abs(startCorner.getX() - endCorner.getX()) + 1;
        Vec3i xVector;
        if(startCorner.getX() < endCorner.getX()) {
            xVector = new Vec3i(1, 0, 0);
        }
        else {
            xVector = new Vec3i(-1, 0, 0);
        }
        validateSingleSide(
                startCorner, endCorner, xVector, numBlocks,
                this::validateAroundPipeZDirCorner,
                this::validateAroundPipeZDir
        );
    }

    private void validateSingleSideZDir(BlockPos startCorner, BlockPos endCorner) {
        int numBlocks = abs(startCorner.getZ() - endCorner.getZ()) + 1;
        Vec3i zVector;
        if(startCorner.getZ() < endCorner.getZ()) {
            zVector = new Vec3i(0, 0, 1);
        }
        else {
            zVector = new Vec3i(0, 0, -1);
        }
        validateSingleSide(
                startCorner, endCorner, zVector, numBlocks,
                this::validateAroundPipeXDirCorner,
                this::validateAroundPipeXDir
        );
    }

    private void validateSingleSide(BlockPos startCorner,
                                    BlockPos endCorner,
                                    Vec3i translationVector,
                                    int numBlocks,
                                    Consumer<BlockPos> pipeEndValidator,
                                    BiConsumer<BlockPos, Class> wallEndValidator) {
        BlockPos currentPosition = startCorner;
        for(int i = 0; i <= numBlocks; ++i) {
            Block block = getBlockAtPosition(currentPosition);
            Block upperBlock = getBlockAtPosition(currentPosition.up());
            if(i == 0) {
                // DO NOTHING
            }
            else if(i == numBlocks - 1) {
                validatePipeEnd(block, currentPosition, pipeEndValidator);
            }
            else if(i == numBlocks) {
                validateWallEnd(block, upperBlock, currentPosition, wallEndValidator);
            }
            else {
                validatePipeMiddle(block, upperBlock, currentPosition, wallEndValidator);
            }
            currentPosition = currentPosition.add(translationVector);
        }
    }

    private void validatePipeEnd(Block block,
                                 BlockPos position,
                                 Consumer<BlockPos> pipeEndValidator) {
        if(block instanceof ParticleAcceleratorBlockBeamPipe) {
            pipeEndValidator.accept(position);
        }
    }

    private void validateWallEnd(Block block,
                                 Block upperBlock,
                                 BlockPos position,
                                 BiConsumer<BlockPos, Class> wallEndValidator) {
        if(block instanceof ParticleAcceleratorBlockWall &&
                upperBlock instanceof ParticleAcceleratorBlockWall) {
            wallEndValidator.accept(position, upperBlock.getClass());
        }
        else {
            throw new InvalidSurroundBeamPipeException(
                    "Invalid blocks surrounding beam pipe corner."
            );
        }
    }

    private void validatePipeMiddle(Block block,
                                    Block upperBlock,
                                    BlockPos position,
                                    BiConsumer<BlockPos, Class> wallEndValidator) {
        if(upperBlock instanceof BlockAir) {
            throw new InvalidSurroundBeamPipeException(
                    "Air block found around beam pipe(up)."
            );
        }
        else if(!(upperBlock instanceof ParticleAcceleratorBlockBase)) {
            throw new InvalidSurroundBeamPipeException(
                    "Block is not a valid accelerator block"
            );
        }
        else if(!(block instanceof ParticleAcceleratorBlockBeamPipe)) {
            throw new InvalidBeamPipeException("Missing beam pipe");
        }
        else {
            wallEndValidator.accept(position, upperBlock.getClass());
        }
    }

    private boolean validateAroundPipeXDir(BlockPos centerPosition, Class classType) {
        return validateAroundPipe(
                classType, centerPosition, centerPosition.east(), centerPosition.west()
        );
    }

    private boolean validateAroundPipeZDir(BlockPos centerPosition, Class classType) {
        return validateAroundPipe(
                classType, centerPosition, centerPosition.north(), centerPosition.south()
        );
    }

    private boolean validateAroundPipe(Class classType, BlockPos centerPosition,
                                       BlockPos leftPosition, BlockPos rightPosition) {
        List<Block> surroundingBlocks = new ArrayList<Block>(8);
        surroundingBlocks.add(getBlockAtPosition(centerPosition.up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.down()));
        surroundingBlocks.add(getBlockAtPosition(leftPosition));
        surroundingBlocks.add(getBlockAtPosition(rightPosition));
        surroundingBlocks.add(getBlockAtPosition(leftPosition.up()));
        surroundingBlocks.add(getBlockAtPosition(leftPosition.down()));
        surroundingBlocks.add(getBlockAtPosition(rightPosition.up()));
        surroundingBlocks.add(getBlockAtPosition(rightPosition.down()));
        for(int block = 0; block < 8; ++block) {
            if(!(surroundingBlocks.get(block).getClass().isAssignableFrom(classType))) {
                throw new InvalidSurroundBeamPipeException(
                        "Block surrounding beam pipe does not match the expected type."
                );
            }
        }
        return true;
    }

    private void validateAroundPipeXDirCorner(BlockPos centerPosition) {
        validateAroundPipeCorner(
                centerPosition, centerPosition.east(), centerPosition.west()
        );
    }

    private void validateAroundPipeZDirCorner(BlockPos centerPosition) {
        validateAroundPipeCorner(
                centerPosition, centerPosition.north(), centerPosition.south()
        );
    }

    // this is the generic method that will be used for both XDirCorner and zDirCorner
    private void validateAroundPipeCorner(BlockPos centerPosition,
                                          BlockPos leftPosition,
                                          BlockPos rightPosition) {
        List<Block> surroundingBlocks = new ArrayList<Block>(6);
        List<Block> sideBlocks = new ArrayList<Block>(2);
        surroundingBlocks.add(getBlockAtPosition(centerPosition.up()));
        surroundingBlocks.add(getBlockAtPosition(centerPosition.down()));
        surroundingBlocks.add(getBlockAtPosition(leftPosition.up()));
        surroundingBlocks.add(getBlockAtPosition(leftPosition.down()));
        surroundingBlocks.add(getBlockAtPosition(rightPosition.up()));
        surroundingBlocks.add(getBlockAtPosition(rightPosition.down()));
        for(int block = 0; block < surroundingBlocks.size(); ++block) {
            if(!(surroundingBlocks.get(block) instanceof ParticleAcceleratorBlockWall)) {
                throw new InvalidSurroundBeamPipeException(
                        "Block surrounding beam pipe does not match the expected type at corner."
                );
            }
        }
        int beamPipeCount = 0;
        sideBlocks.add(getBlockAtPosition(leftPosition));
        sideBlocks.add(getBlockAtPosition(rightPosition));
        for(int block = 0; block < sideBlocks.size(); ++block) {
            if(sideBlocks.get(block) instanceof ParticleAcceleratorBlockBeamPipe) {
                ++beamPipeCount;
            }
            else if(sideBlocks.get(block) instanceof ParticleAcceleratorBlockWall) {
                continue;
            }
            else {
                throw new InvalidSurroundBeamPipeException(
                        "Invalid block surrounding beam pipe corner."
                );
            }
        }

        if(beamPipeCount != 1) {
            throw new InvalidBeamPipeException("Missing beam pipe at corner turn.");
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

        if(beamPositions.get(BeamSourceIndices.LEFT).getZ() ==
                beamPositions.get(BeamSourceIndices.RIGHT).getZ()) {
            lengthCorners = calculateLengthCorners(beamPositions, positiveXVector, negativeXVector);

            Block frontLeft = getBlockAtPosition(lengthCorners.get(CornerIndices.FRONT_LEFT).add(positiveZVector));
            Block frontRight = getBlockAtPosition(lengthCorners.get(CornerIndices.FRONT_RIGHT).add(positiveZVector));
            if(frontLeft instanceof ParticleAcceleratorBlockBeamPipe &&
                    frontRight instanceof ParticleAcceleratorBlockBeamPipe) {
                depthCorners = calculateDepthCorners(lengthCorners, positiveZVector);
            }
            else {
                depthCorners = calculateDepthCorners(lengthCorners, negativeZVector);
            }
        }
        else if(beamPositions.get(BeamSourceIndices.LEFT).getX() ==
                beamPositions.get(BeamSourceIndices.RIGHT).getX()) {
            lengthCorners = calculateLengthCorners(beamPositions, positiveZVector, negativeZVector);

            Block frontLeft = getBlockAtPosition(lengthCorners.get(CornerIndices.FRONT_LEFT).add(positiveXVector));
            Block frontRight = getBlockAtPosition(lengthCorners.get(CornerIndices.FRONT_RIGHT).add(positiveXVector));
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

        if(!areCornerCoordinatesValid(cornerPositions)) {
            throw new InvalidShapeException("Not a valid rectangle according to the corners.");
        }

        return cornerPositions;
    }

    private List<BlockPos> calculateLengthCorners(List<BlockPos> beamPositions, Vec3i positiveLength, Vec3i negativeLength) {
        int left = 0;
        int right = 1;
        List<BlockPos> lengthCorners = new ArrayList<BlockPos>(2);
        BlockPos leftCorner = getSingleCorner(beamPositions.get(BeamSourceIndices.LEFT), positiveLength);
        BlockPos rightCorner = getSingleCorner(beamPositions.get(BeamSourceIndices.RIGHT), negativeLength);

        lengthCorners.add(CornerIndices.FRONT_LEFT, leftCorner);
        lengthCorners.add(CornerIndices.FRONT_RIGHT, rightCorner);

        if(areBlocksSymmetricalToController(lengthCorners.get(left), lengthCorners.get(right))) {
            return lengthCorners;
        }

        throw new InvalidShapeException("Length corners are not symmetrical around controller.");
    }

    private List<BlockPos> calculateDepthCorners(List<BlockPos> lengthCorners, Vec3i depthVector) {
        int left = 0;
        int right = 1;
        List<BlockPos> depthCorners = new ArrayList<BlockPos>(2);
        BlockPos leftCorner = getSingleCorner(lengthCorners.get(CornerIndices.FRONT_LEFT), depthVector);
        BlockPos rightCorner = getSingleCorner(lengthCorners.get(CornerIndices.FRONT_RIGHT), depthVector);

        depthCorners.add(leftCorner);
        depthCorners.add(rightCorner);

        if(areBlocksSymmetricalToController(depthCorners.get(left), depthCorners.get(right))) {
            return depthCorners;
        }

        throw new InvalidShapeException("Depth corners are not symmetrical around controller.");
    }

    private BlockPos getSingleCorner(BlockPos position, Vec3i addition) {
        Block nextBlock;

        // There must be at least one beam pipe, if not then immediately return null.
        if(!(getBlockAtPosition(position.add(addition)) instanceof ParticleAcceleratorBlockBeamPipe)) {
            throw new InvalidBeamPipeException("Must have beam pipe adjacent to beam source.");
        }
        do {
            position = position.add(addition);
            nextBlock = getBlockAtPosition(position);
        }
        while(nextBlock instanceof ParticleAcceleratorBlockBeamPipe);
        return position.subtract(addition);
    }

    private boolean areCornerCoordinatesValid(List<BlockPos> cornerPositions) {
        int length1 = calculateCoordinateDistance(
                cornerPositions.get(CornerIndices.FRONT_LEFT),
                cornerPositions.get(CornerIndices.FRONT_RIGHT)
        );
        int length2 = calculateCoordinateDistance(
                cornerPositions.get(CornerIndices.BACK_LEFT),
                cornerPositions.get(CornerIndices.BACK_RIGHT)
        );
        int depth1 = calculateCoordinateDistance(
                cornerPositions.get(CornerIndices.FRONT_LEFT),
                cornerPositions.get(CornerIndices.BACK_LEFT)
        );
        int depth2 = calculateCoordinateDistance(
                cornerPositions.get(CornerIndices.FRONT_RIGHT),
                cornerPositions.get(CornerIndices.BACK_RIGHT)
        );
        return length1 - length2 + depth1 - depth2 == 0;
    }

    private Block getBlockAtPosition(BlockPos position) {
        return WORLD.getBlockState(position).getBlock();
    }

    private boolean areBlocksSymmetricalToController(BlockPos block1, BlockPos block2) {
        int distanceToController1 = calculateCoordinateDistance(block1, controllerBlockPosition);
        int distanceToController2 = calculateCoordinateDistance(block2, controllerBlockPosition);
        return distanceToController1 == distanceToController2;
    }

    private int calculateCoordinateDistance(BlockPos block1, BlockPos block2) {
        return (int) abs(block1.getDistance(block2.getX(), block2.getY(), block2.getZ()));
    }

    private boolean zCoordinatesAreEqual(BlockPos start, BlockPos end) {
        return (start.getZ() - end.getZ()) == 0;
    }
    @Override
    protected void onAssimilate(MultiblockControllerBase multiblockControllerBase) {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    protected void onAssimilated(MultiblockControllerBase multiblockControllerBase) {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    protected boolean updateServer() {
        return false;
    }

    @Override
    protected void updateClient() {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
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
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    @Override
    protected void syncDataTo(NBTTagCompound nbtTagCompound, ModTileEntity.SyncReason syncReason) {
        // TODO(CM): Either fix empty method or format to show we aren't using it.
    }

    // -----------------------------------------------------------------------
    // Inner Data classes
    // -----------------------------------------------------------------------

    private class CurrentCounts {
        int targetBlock = 0;
        int detectorBlock = 0;
        int beamSource = 0;
        int powerPort = 0;
        int inputPort = 0;
        int outputPort = 0;
        int controllerBlock = 0;
        int magnetBlock = 0;
    }

    private class ExpectedCounts {
        static final int TARGET = 8;
        static final int DETECTOR = 16;
        static final int BEAM_SOURCE = 2;
        static final int CONTROLLER = 1;
        static final int POWER_PORT = 1;
        static final int INPUT_PORT = 1;
        static final int OUTPUT_PORT = 1;
    }

    private class BeamSourceIndices {
        static final int LEFT = 0;
        static final int RIGHT = 1;
    }

    private class CornerIndices {
        static final int FRONT_LEFT = 0;
        static final int FRONT_RIGHT = 1;
        static final int BACK_LEFT = 2;
        static final int BACK_RIGHT = 3;
    }

    private class ActualAcceleratorSizes {
        int total = 0;
        int lengthPipe = 0;
        int depthPipe = 0;
    }

    private class ExpectedMinimumAcceleratorSizes {
        static final int LENGTH_OF_PIPE = 5;
        static final int DEPTH_OF_PIPE = 3;
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
