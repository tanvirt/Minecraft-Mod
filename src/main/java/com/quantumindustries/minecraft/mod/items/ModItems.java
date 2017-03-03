package com.quantumindustries.minecraft.mod.items;

import com.quantumindustries.minecraft.mod.ItemModelProvider;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    // TODO: Remove this. This is a test mod item.
    public static ItemBase ingotCopper;

    public static void init() {
        // TODO: Remove this. This is a test mod item.
        ItemOre item = new ItemOre("ingotCopper", "ingotCopper");
        ingotCopper = register(item);
    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if(item instanceof ItemModelProvider) {
            ((ItemModelProvider) item).registerItemModel(item);
        }
        if(item instanceof ItemOreDict) {
            ((ItemOreDict) item).initOreDict();
        }

        return item;
    }

}
