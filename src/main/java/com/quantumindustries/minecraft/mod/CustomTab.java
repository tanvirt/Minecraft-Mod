package com.quantumindustries.minecraft.mod;

import com.quantumindustries.minecraft.mod.blocks.ModBlocks;
import com.quantumindustries.minecraft.mod.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CustomTab extends CreativeTabs {

    public CustomTab() {
        super(CustomMod.MODID);
    }

    @Override
    public Item getTabIconItem() {
        return ModItems.ingotNeodymium;
    }

    @Override
    public boolean hasSearchBar() {
        return false;
    }

}
