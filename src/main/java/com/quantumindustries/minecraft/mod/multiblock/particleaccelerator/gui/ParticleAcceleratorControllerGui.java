package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.gui;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorControllerTileEntity;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.containers.ParticleAcceleratorControllerContainer;
import net.darkhax.tesla.lib.PowerBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class ParticleAcceleratorControllerGui extends GuiContainer {

    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;
    public PowerBar controllerPowerBar;

    private static final ResourceLocation background =
            new ResourceLocation(
                CustomMod.MODID,
                "textures/gui/acceleratorControllerGui.png"
            );

    public ParticleAcceleratorControllerGui(ParticleAcceleratorControllerTileEntity tileEntity,
                                            ParticleAcceleratorControllerContainer container) {
        super(container);
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void initGui() {
        super.initGui();
        controllerPowerBar = new PowerBar(this, guiLeft, guiTop, PowerBar.BackgroundType.DARK);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        // TODO(CM): Get actual values from power ports
        controllerPowerBar.draw(5000, 10000);
    }
}
