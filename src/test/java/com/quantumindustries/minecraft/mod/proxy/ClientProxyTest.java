package com.quantumindustries.minecraft.mod.proxy;

import com.quantumindustries.minecraft.mod.blocks.ModBlocks;
import com.quantumindustries.minecraft.mod.items.ModItems;
import mockit.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientProxyTest {

    CommonProxy proxy;
    @Mocked ModItems items;
    @Mocked ModBlocks blocks;
    @Mocked FMLPreInitializationEvent preInitEvent;
    @Mocked FMLInitializationEvent initEvent;
    @Mocked FMLPostInitializationEvent postInitEvent;

    @BeforeEach
    public void setUp() {
        proxy = new ClientProxy();
    }

    @AfterEach
    public void tearDown() {}

    @Test
    public void testPreInit() {
        proxy.preInit(preInitEvent);

        new FullVerificationsInOrder() {{
           ModItems.init();
           ModBlocks.init();
        }};
    }

    @Test
    public void testInit() {
        proxy.init(initEvent);
        new FullVerificationsInOrder() {{}};
    }

    @Test
    public void testPostInit() {
        proxy.postInit(postInitEvent);
        new FullVerificationsInOrder() {{}};
    }

}