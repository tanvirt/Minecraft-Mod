package com.quantumindustries.minecraft.mod;

import com.quantumindustries.minecraft.mod.proxy.CommonProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void testInitializationEvents() {
        mod.preInit(preInit);
        mod.init(init);
        mod.postInit(postInit);

        new VerificationsInOrder() {{
            proxy.preInit(preInit);
            proxy.init(init);
            proxy.postInit(postInit);
        }};
    }

}
