package com.quantumindustries.minecraft.mod.guis.util;

import com.quantumindustries.minecraft.mod.fluids.ModFluids;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidBar {

    public static final int WIDTH = 14;
    public static final int HEIGHT = 50;

    private static final ResourceLocation TEXTURE_SHEET = new ResourceLocation("custommod", "textures/gui/elements/FluidTank.png");
    private static final ResourceLocation FLUID = new ResourceLocation("custommod", "textures/block/plasma_flow.png");

    private final int x;
    private final int y;
    private final boolean isLeft;
    private Fluid storedFluid;

    private final GuiScreen screen;

    public FluidBar(GuiScreen screen, int x, int y, boolean isLeft, Fluid storedFluid) {
        this.screen = screen;
        this.x = x;
        this.y = y;
        this.isLeft = isLeft;
        this.storedFluid = storedFluid;
    }

    public void draw(int current, int capacity) {
        screen.mc.getTextureManager().bindTexture(TEXTURE_SHEET);
        screen.drawTexturedModalRect(x, y, 3, 1, WIDTH, HEIGHT);

        drawPlasma(current, capacity);

        if(isLeft) {
            screen.drawTexturedModalRect(x, y, 33, 1, WIDTH, HEIGHT);
        } else {
            screen.drawTexturedModalRect(x, y, 18, 1, WIDTH, HEIGHT);
        }
    }

    private void drawFluid(int current, int capacity) {

    }

    private void drawPlasma(int current, int capacity) {
        int fluidOffset = (current * (HEIGHT + 1)) / capacity;
        screen.drawTexturedModalRect(x, y + HEIGHT - fluidOffset, 46, ((HEIGHT + 1) - fluidOffset), WIDTH, fluidOffset + 2);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
