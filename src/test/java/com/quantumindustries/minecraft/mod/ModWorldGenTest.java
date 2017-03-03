package com.quantumindustries.minecraft.mod;

import net.minecraftforge.fml.common.IWorldGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModWorldGenTest {

    IWorldGenerator worldGenerator;

    @BeforeEach
    public void setUp() {
        worldGenerator = new ModWorldGen();
    }

    @AfterEach
    public void tearDown() {}

    @Test
    public void testGenerate() {
        // TODO
        fail("generate() has not been tested");
    }

}
