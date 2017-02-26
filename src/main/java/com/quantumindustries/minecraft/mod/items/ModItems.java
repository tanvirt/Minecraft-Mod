package com.quantumindustries.minecraft.mod.items;

import com.quantumindustries.minecraft.mod.ItemModelProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static ItemBase ingotCopper;

    public static void init() {
        ItemBase item = new ItemBase("ingotCopper");
        item.setCreativeTab(CreativeTabs.MATERIALS);
        ingotCopper = register(item);
    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if(item instanceof ItemModelProvider) {
            ((ItemModelProvider) item).registerItemModel(item);
        }

        return item;
    }
}
