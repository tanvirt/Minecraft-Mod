package com.example.examplemod;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomModTest {

    private CustomMod mod;

    @BeforeEach
    public void setUp() {
        mod = new CustomMod();
    }

    @AfterEach
    public void tearDown() {
        // TODO
    }

    @Test
    public void testToString() {
        assertEquals("CustomMod class", mod.toString());
    }

}
