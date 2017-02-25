package com.example.examplemod.tab;

import net.minecraft.creativetab.CreativeTabs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomTabTest {

    CreativeTabs tab;

    @BeforeEach
    public void setUp() {
        // TODO
        tab = new CustomTab(CreativeTabs.getNextID(), "custom_tab");
    }

    @AfterEach
    public void tearDown() {
        // TODO
    }

    @Test
    public void testGetTabIconItem() {
        // TODO
    }

}
