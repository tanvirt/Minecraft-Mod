package com.example.examplemod;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTestTest {

    private ExampleMod mod;

    @BeforeEach
    public void setUp() {
        mod = new ExampleMod();
    }

    @AfterEach
    public void tearDown() {
        // TODO
    }

    @Test
    public void testToString() {
        assertEquals("ExampleMod class", mod.toString());
    }

}
