package com.quantumindustries.minecraft.mod;

import com.quantumindustries.minecraft.mod.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CustomTab extends CreativeTabs {

    public CustomTab() {
        super(CustomMod.MODID);
        setBackgroundImageName("item_search.png");
    }

    @Override
    public Item getTabIconItem() {
        return ModItems.ingotCopper;
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

}
