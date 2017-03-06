package com.quantumindustries.minecraft.mod.fluids;

import com.quantumindustries.minecraft.mod.CustomMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBucket;

public class ItemBucketBase extends ItemBucket {

    public ItemBucketBase(String name, Block containedBlock) {
        super(containedBlock);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CustomMod.tab);
    }

}
