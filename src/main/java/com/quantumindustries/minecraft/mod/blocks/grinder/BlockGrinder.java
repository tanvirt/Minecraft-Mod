package com.quantumindustries.minecraft.mod.blocks.grinder;

import java.util.Random;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.blocks.ModBlocks;
import com.quantumindustries.minecraft.mod.tileentities.BlockContainerTileEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockGrinder extends BlockContainerTileEntity<TileEntityGrinder> {

    public static final PropertyDirection FACING = PropertyDirection.create(
            "facing", EnumFacing.Plane.HORIZONTAL
    );
    private final boolean isGrinding;
    private boolean hasTileEntity;

    public BlockGrinder() {
        super(Material.ROCK, "blockGrinder");
        setDefaultState(blockState.getBaseState().withProperty(
                FACING,
                EnumFacing.NORTH
        ));
        isGrinding = true;
        hasTileEntity = false;
        blockSoundType = SoundType.SNOW;
        blockParticleGravity = 1.0f;
        slipperiness = 0.6f;
        lightOpacity = 20;
        setTickRandomly(false);
        useNeighborBrightness = false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.blockGrinder);
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState blockState) {
        if(!world.isRemote) {
            EnumFacing enumFacing = getUnblockedFace(
                    world,
                    blockPos,
                    blockState
            );

            world.setBlockState(
                    blockPos,
                    blockState.withProperty(FACING, enumFacing),
                    2
            );
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos,
                                    IBlockState blockState, EntityPlayer player,
                                    EnumHand hand, ItemStack stack, EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            player.openGui(
                    CustomMod.instance,
                    CustomMod.GUI.GRINDER.ordinal(),
                    world,
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ()
            );
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityGrinder();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta,
                                            EntityLivingBase placer, ItemStack stack) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
                                EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(
                FACING,
                placer.getHorizontalFacing().getOpposite()),
                2
        );

        if(stack.hasDisplayName()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if(tileEntity instanceof TileEntityGrinder) {
                ((TileEntityGrinder) tileEntity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(!hasTileEntity) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if(tileEntity instanceof TileEntityGrinder) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityGrinder) tileEntity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world,
                                  BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.blockGrinder));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState blockState) {
        return EnumBlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumFacing = EnumFacing.getFront(meta);

        if(enumFacing.getAxis() == EnumFacing.Axis.Y) {
            enumFacing = EnumFacing.NORTH;
        }

        return getDefaultState().withProperty(FACING, enumFacing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public Class<TileEntityGrinder> getTileEntityClass() {
        return TileEntityGrinder.class;
    }

    @Nullable
    @Override
    public TileEntityGrinder createTileEntity(World world, IBlockState state) {
        return new TileEntityGrinder();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }

    private EnumFacing getUnblockedFace(World world, BlockPos blockPos, IBlockState blockState) {
        IBlockState blockToNorth = world.getBlockState(blockPos.north());
        IBlockState blockToSouth = world.getBlockState(blockPos.south());
        IBlockState blockToWest = world.getBlockState(blockPos.west());
        IBlockState blockToEast = world.getBlockState(blockPos.east());

        EnumFacing enumFacing = blockState.getValue(FACING);

        if(shouldFaceSouth(blockToNorth, blockToSouth, enumFacing)) {
            enumFacing = EnumFacing.SOUTH;
        }
        else if(shouldFaceNorth(blockToNorth, blockToSouth, enumFacing)) {
            enumFacing = EnumFacing.NORTH;
        }
        else if(shouldFaceEast(blockToWest, blockToEast, enumFacing)) {
            enumFacing = EnumFacing.EAST;
        }
        else if(shouldFaceWest(blockToWest, blockToEast, enumFacing)) {
            enumFacing = EnumFacing.WEST;
        }

        return enumFacing;
    }

    private boolean shouldFaceSouth(IBlockState blockToNorth, IBlockState blockToSouth, EnumFacing enumFacing) {
        return enumFacing == EnumFacing.NORTH &&
                blockToNorth.isFullBlock() &&
                !blockToSouth.isFullBlock();
    }

    private boolean shouldFaceNorth(IBlockState blockToNorth, IBlockState blockToSouth, EnumFacing enumFacing) {
        return enumFacing == EnumFacing.SOUTH &&
                blockToSouth.isFullBlock() &&
                !blockToNorth.isFullBlock();
    }

    private boolean shouldFaceEast(IBlockState blockToWest, IBlockState blockToEast, EnumFacing enumFacing) {
        return enumFacing == EnumFacing.WEST &&
                blockToWest.isFullBlock() &&
                !blockToEast.isFullBlock();
    }

    private boolean shouldFaceWest(IBlockState blockToWest, IBlockState blockToEast, EnumFacing enumFacing) {
        return enumFacing == EnumFacing.EAST &&
                blockToEast.isFullBlock() &&
                !blockToWest.isFullBlock();
    }

}
