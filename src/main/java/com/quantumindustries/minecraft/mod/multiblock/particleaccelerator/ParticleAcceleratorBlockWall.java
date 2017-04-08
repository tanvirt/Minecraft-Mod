package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import com.google.common.collect.Lists;
import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import it.zerono.mods.zerocore.lib.BlockFacings;
import it.zerono.mods.zerocore.lib.PropertyBlockFacings;

import java.util.ArrayList;

public class ParticleAcceleratorBlockWall extends ParticleAcceleratorBlockBase {

    private final static PropertyEnum FACES;
    private final static PropertyBool ROTATE = PropertyBool.create("rotate");

    public ParticleAcceleratorBlockWall(String name) {
        super(name, ParticleAcceleratorBlockType.Wall);

        setDefaultState(blockState.getBaseState()
                .withProperty(FACES, PropertyBlockFacings.All)
                .withProperty(ROTATE, false)
        );
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess,
                                        BlockPos pos, EnumFacing side) {
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    // -------------------------------------------------------------------------
    // BlockState directional code
    // -------------------------------------------------------------------------

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos position) {

        IMultiblockPart part = this.getMultiblockPartAt(world, position);

        if(part instanceof ParticleAcceleratorTileEntity) {

            ParticleAcceleratorTileEntity wallTile = (ParticleAcceleratorTileEntity) part;
            boolean assembled = wallTile.isConnected() && wallTile.getMultiblockController().isAssembled();
            BlockFacings facings = assembled ? wallTile.getOutwardsDir() : BlockFacings.ALL;

            state = state.withProperty(ROTATE, wallTile.getRotateTop());
            state = state.withProperty(FACES, facings.toProperty());

            // active icon hack

            if (wallTile.getPartPosition().isFace()) {

                ParticleAcceleratorController controller = (ParticleAcceleratorController) wallTile.getMultiblockController();
                boolean active = controller.isAssembled() && controller.isActive();

                if (active)
                    state = state.withProperty(FACES, PropertyBlockFacings.Opposite_EW);
            }
        }

        return state;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACES, ROTATE);
    }

    static {

        ArrayList<PropertyBlockFacings> values = Lists.newArrayList();

        values.addAll(PropertyBlockFacings.ALL_AND_NONE);
        values.addAll(PropertyBlockFacings.FACES);
        values.addAll(PropertyBlockFacings.ANGLES);
        values.addAll(PropertyBlockFacings.CORNERS);
        values.add(PropertyBlockFacings.Opposite_EW);

        FACES = PropertyEnum.create("faces", PropertyBlockFacings.class, values);
    }
}
