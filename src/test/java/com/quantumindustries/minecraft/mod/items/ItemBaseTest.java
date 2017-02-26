package com.quantumindustries.minecraft.mod.items;

import com.quantumindustries.minecraft.mod.CustomMod;
import mockit.FullVerificationsInOrder;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import net.minecraft.creativetab.CreativeTabs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemBaseTest {

    ItemBase item;
    String name;
    @Mocked CustomMod mod;
    @Mocked CreativeTabs tab;

    @BeforeEach
    public void setUp() {
        name = "item_name";
        item = new ItemBase(name);
    }

    @AfterEach
    public void tearDown() {}

    @Test
    public void testConstructor() {
        new MockUp<ItemBase>() {
            @Mock public void setUnlocalizedName(String name) {}
            @Mock public void setRegistryName(String name) {}
        };
        new FullVerificationsInOrder() {{
            item.setUnlocalizedName(name);
            item.setRegistryName(name);
        }};
    }

    @Test
    public void testRegisterItemModel() {
        item.registerItemModel();

        new FullVerificationsInOrder() {{
            mod.registerItemRenderer(item, 0, name);
        }};
    }

    @Test
    public void testSetCreativeTab() {
        ItemBase returnedItem = item.setCreativeTab(tab);
        assertEquals(returnedItem, item);
    }

}
