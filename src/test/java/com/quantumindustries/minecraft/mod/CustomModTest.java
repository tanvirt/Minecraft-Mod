package com.quantumindustries.minecraft.mod;

import com.quantumindustries.minecraft.mod.items.ItemBase;
import com.quantumindustries.minecraft.mod.proxy.CommonProxy;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mockit.*;

class CustomModTest {

    CustomMod mod;
    @Mocked CommonProxy proxy;
    @Mocked FMLPreInitializationEvent preInitEvent;
    @Mocked FMLInitializationEvent initEvent;
    @Mocked FMLPostInitializationEvent postInitEvent;

    @BeforeEach
    public void setUp() {
        mod = new CustomMod();
        CustomMod.proxy = proxy;
    }

    @AfterEach
    public void tearDown() {
        CustomMod.proxy = null;
    }

    @Test
    public void testPreInit() {
        mod.preInit(preInitEvent);

        new FullVerificationsInOrder() {{
            proxy.preInit(preInitEvent);
        }};
    }

    @Test
    public void testInit() {
        mod.init(initEvent);

        new FullVerificationsInOrder() {{
            proxy.init(initEvent);
        }};
    }

    @Test
    public void testPostInit() {
        mod.postInit(postInitEvent);

        new FullVerificationsInOrder() {{
            proxy.postInit(postInitEvent);
        }};
    }

    @Test
    public void testRegisterItemRenderer() {
        final int meta = 0;
        final String name = "item_name";
        final Item item = new ItemBase(name);
        CustomMod.registerItemRenderer(item, meta, name);

        new FullVerificationsInOrder() {{
            proxy.registerItemRenderer(item, meta, name);
        }};
    }

}
