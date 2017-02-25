package com.example.examplemod;

import com.example.examplemod.proxy.CommonProxy;
import com.example.examplemod.tab.CustomTab;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import mockit.*;

class CustomModTest {

    @Tested CustomMod mod;
    @Mocked CommonProxy proxy;
    @Mocked FMLPreInitializationEvent preInit;
    @Mocked FMLInitializationEvent init;
    @Mocked FMLPostInitializationEvent postInit;

    @BeforeEach
    public void setUp() {
        CustomMod.proxy = proxy;
    }

    @AfterEach
    public void tearDown() {
        CustomMod.proxy = null;
    }

    @Test
    public void testToString() {
        assertEquals("CustomMod class", mod.toString());
    }

    @Test
    public void testInitializationEvents(@Mocked CustomTab tab) {
        mod.preInit(preInit);
        mod.init(init);
        mod.postInit(postInit);

        new VerificationsInOrder() {{
            new CustomTab(anyInt, "custom_tab");
            proxy.preInit(preInit);
            proxy.init(init);
            proxy.postInit(postInit);
        }};
    }

}
