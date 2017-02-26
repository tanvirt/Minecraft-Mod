package com.quantumindustries.minecraft.mod.proxy;

import com.quantumindustries.minecraft.mod.CustomMod;
import mockit.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommonProxyTest {

    CommonProxy proxy;
    @Mocked ModelLoader loader;
    @Mocked Item item;
    @Mocked ModelResourceLocation location;
    @Mocked FMLPreInitializationEvent preInitEvent;
    @Mocked FMLInitializationEvent initEvent;
    @Mocked FMLPostInitializationEvent postInitEvent;

    @BeforeEach
    public void setUp() {
        proxy = new CommonProxy();
    }

    @AfterEach
    public void tearDown() {}

    @Test
    public void testRegisterItemRenderer() {
        final int meta = 0;
        final String id = "item_name";

        new StrictExpectations() {{
            new ModelResourceLocation(
                    CustomMod.MODID + ":" + id,
                    "inventory"
            ); result = location;
        }};

        proxy.registerItemRenderer(item, meta, id);

        new FullVerificationsInOrder() {{
            ModelLoader.setCustomModelResourceLocation(item, meta, location);
        }};
    }

    @Test
    public void testPreInit() {
        proxy.preInit(preInitEvent);
        new FullVerificationsInOrder() {{}};
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
