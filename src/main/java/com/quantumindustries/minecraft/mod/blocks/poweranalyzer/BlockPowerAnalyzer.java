package com.quantumindustries.minecraft.mod.blocks.poweranalyzer;

import com.quantumindustries.minecraft.mod.tileentities.BlockTileEntity;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPowerAnalyzer extends BlockTileEntity<TileEntityPowerAnalyzer> {

    public BlockPowerAnalyzer() {
        super(Material.ROCK, "powerAnalyzer");
    }

    @Override
    public boolean onBlockActivated(World world,
                                    BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer player,
                                    EnumHand hand,
                                    ItemStack heldItem,
                                    EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            TileEntityPowerAnalyzer tile = getTileEntity(world, pos);
            final ITeslaHolder holder = tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, side);
            player.addChatMessage(new TextComponentString("Power: " + holder.getStoredPower()));
        }
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public Class<TileEntityPowerAnalyzer> getTileEntityClass() {
        return TileEntityPowerAnalyzer.class;
    }

    @Nullable
    @Override
    public TileEntityPowerAnalyzer createTileEntity(World world, IBlockState state) {
        return new TileEntityPowerAnalyzer();
    }
}
