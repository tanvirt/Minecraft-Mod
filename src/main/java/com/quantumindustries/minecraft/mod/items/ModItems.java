package com.quantumindustries.minecraft.mod.items;

import com.quantumindustries.minecraft.mod.ItemModelProvider;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    // ItemBases
    public static ItemBase airFilter;

    // ItemOres
    public static ItemOre ingotCobalt;
    public static ItemOre ingotRhodium;
    public static ItemOre dustCobalt;
    public static ItemOre dustRhodium;

    public static void init() {
        ingotCobalt = register(new ItemOre("ingotCobalt", "ingotCobalt"));
        ingotRhodium = register(new ItemOre("ingotRhodium", "ingotRhodium"));
        dustCobalt = register(new ItemOre("dustCobalt", "dustCobalt"));
        dustRhodium = register(new ItemOre("dustRhodium", "dustRhodium"));
        airFilter = register(new ItemBase("airFilter"));
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
