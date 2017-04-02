package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator;

import com.quantumindustries.minecraft.mod.CustomMod;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ParticleAcceleratorBlockController extends ParticleAcceleratorBlockBase {

    public ParticleAcceleratorBlockController(String name, ParticleAcceleratorBlockType type) {
        super(name, type);
    }

    public static final int GUI_ID = 1;

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory")
        );
    }

//    @Override
//    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
//                                    EntityPlayer player, EnumHand hand,
//                                    ItemStack heldItem, EnumFacing side,
//                                    float hitX, float hitY, float hitZ) {
//        // Only execute on the server
//        if (world.isRemote) {
//            return true;
//        }
//        TileEntity te = world.getTileEntity(pos);
//        if (!(te instanceof ParticleAcceleratorControllerTileEntity)) {
//            return false;
//        }
//        player.openGui(CustomMod.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
//        return true;
//    }
}
