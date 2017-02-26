package com.quantumindustries.minecraft.mod.blocks;

import com.quantumindustries.minecraft.mod.CustomMod;
import mockit.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockBaseTest {

    BlockBase block;
    String name;
    @Mocked CustomMod mod;
    @Mocked ItemBlock itemBlock;
    @Mocked CreativeTabs tab;

    @BeforeEach
    public void setUp() {
        name = "Air block";

        new MockUp<Block>() {
            @Mock public void $init(Material material) {}
        };

        block = new BlockBase(Material.AIR, name);
    }

    @AfterEach
    public void tearDown() {}

    @Test
    public void testConstructor() {
        new MockUp<BlockBase>() {
            @Mock public void setUnlocalizedName(String name) {}
            @Mock public void setRegistryName(String name) {}
        };

        new FullVerificationsInOrder() {{
            block.setUnlocalizedName(name);
            block.setRegistryName(name);
            block.setCreativeTab(CustomMod.tab);
        }};
    }

    @Test
    public void testRegisterItemModel() {
        block.registerItemModel(itemBlock);

        new FullVerificationsInOrder() {{
            mod.registerItemRenderer(itemBlock, 0, name);
        }};
    }

    @Test
    public void testSetCreativeTab() {
        BlockBase returnedBlock = block.setCreativeTab(tab);
        assertEquals(returnedBlock, block);
    }

}
