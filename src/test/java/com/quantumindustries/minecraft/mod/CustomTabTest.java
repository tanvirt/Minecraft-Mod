package com.quantumindustries.minecraft.mod;

import com.quantumindustries.minecraft.mod.items.ModItems;
import mockit.FullVerificationsInOrder;
import mockit.Mock;
import mockit.MockUp;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomTabTest {

    CustomTab tab;

    @BeforeEach
    public void setUp() {
        tab = new CustomTab();
    }

    @AfterEach
    public void tearDown() {}

    @Test
    public void testConstructor() {
        new FullVerificationsInOrder() {{
            tab.setBackgroundImageName("item_search.png");
            tab.hasSearchBar();
        }};
    }

    @Test
    public void testGetTabIconItem() {
        Item item = tab.getTabIconItem();
        assertEquals(ModItems.ingotCopper, item);
    }

    @Test
    public void testHasSearchBar() {
        assertTrue(tab.hasSearchBar());
    }

}
