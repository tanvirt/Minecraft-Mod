package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.gui;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorControllerTileEntity;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.containers.ParticleAcceleratorControllerContainer;
import com.quantumindustries.minecraft.mod.util.BaseMachineContainer;
import net.darkhax.tesla.lib.PowerBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ParticleAcceleratorControllerGui extends GuiContainer {

    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;
    public PowerBar controllerPowerBar;
    private ParticleAcceleratorControllerTileEntity controller;
    BaseMachineContainer powerPort;

    private static final ResourceLocation background =
            new ResourceLocation(
                CustomMod.MODID,
                "textures/gui/acceleratorControllerGui.png"
            );

    public ParticleAcceleratorControllerGui(ParticleAcceleratorControllerTileEntity controller,
                                            BaseMachineContainer powerPort,
                                            ParticleAcceleratorControllerContainer container) {
        super(container);
        this.controller = controller;
        this.powerPort = powerPort;
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void initGui() {
        super.initGui();
        controllerPowerBar = new PowerBar(this, guiLeft + 10, guiTop + 20, PowerBar.BackgroundType.DARK);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        long capacity = getPowerCapacity();
        long stored = getPowerStored();

        String powerString = stored + "/" + capacity;
        List<String> powerRatio = new ArrayList<>();
        powerRatio.add(powerString);
        if(checkPowerBarHover(mouseX, mouseY)) {
            drawHoveringText(powerRatio, mouseX, mouseY);
        }

        controllerPowerBar.draw(stored, capacity);
    }

    private long getPowerCapacity() {
        return powerPort.getField(0);
    }

    private long getPowerStored() {
        return powerPort.getField(1);
    }

    private boolean checkPowerBarHover(int mouseX, int mouseY) {
        return controllerPowerBar.getX() <= mouseX && mouseX <= controllerPowerBar.getX() + PowerBar.WIDTH &&
                controllerPowerBar.getY() <= mouseY && mouseY <= controllerPowerBar.getY() + PowerBar.HEIGHT;
    }

    @Override
    protected void drawHoveringText(List<String> textLines, int x, int y) {
        super.drawHoveringText(textLines, x, y);
    }
}
