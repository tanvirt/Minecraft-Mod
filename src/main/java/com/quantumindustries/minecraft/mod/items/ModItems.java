package com.quantumindustries.minecraft.mod.items;

import com.quantumindustries.minecraft.mod.ItemModelProvider;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    // ItemBases
    public static ItemBase airFilter;

    // ItemOres
    public static ItemOre ingotCobalt;
    public static ItemOre ingotNeodymium;
    public static ItemOre ingotRhodium;
    public static ItemOre dustCobalt;
    public static ItemOre dustNeodymium;
    public static ItemOre dustRhodium;

    public static void init() {
        initIngots();
        initDusts();
        airFilter = register(new ItemBase("airFilter"));
    }

    private static void initIngots() {
        ingotCobalt = register(new ItemOre("ingotCobalt", "ingotCobalt"));
        ingotNeodymium = register(new ItemOre("ingotNeodymium", "ingotNeodymium"));
        ingotRhodium = register(new ItemOre("ingotRhodium", "ingotRhodium"));
    }

    private static void initDusts() {
        dustCobalt = register(new ItemOre("dustCobalt", "dustCobalt"));
        dustNeodymium = register(new ItemOre("dustNeodymium", "dustNeodymium"));
        dustRhodium = register(new ItemOre("dustRhodium", "dustRhodium"));
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
