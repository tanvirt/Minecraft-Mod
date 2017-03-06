package com.quantumindustries.minecraft.mod.items;

import com.quantumindustries.minecraft.mod.ItemModelProvider;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static void init() {
        initAirFilter();
    }

    private static void initAirFilter() {
        ItemBase airFilter = new ItemBase("airFilter");
        register(airFilter);
    }

    private static void register(Item item) {
        GameRegistry.register(item);

        if(item instanceof ItemModelProvider) {
            ((ItemModelProvider) item).registerItemModel(item);
        }
        if(item instanceof ItemOreDict) {
            ((ItemOreDict) item).initOreDict();
        }
    }

}
