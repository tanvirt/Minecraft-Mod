package com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.gui;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.fluids.ModFluids;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorControllerTileEntity;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorPowerTileEntity;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.containers.ParticleAcceleratorControllerContainer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.PowerBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;

public class ParticleAcceleratorControllerGui extends GuiContainer {

    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;
    public PowerBar controllerPowerBar;
    private ParticleAcceleratorControllerTileEntity controller;
    ParticleAcceleratorPowerTileEntity powerPort;

    private static final ResourceLocation background =
            new ResourceLocation(
                CustomMod.MODID,
                "textures/gui/acceleratorControllerGui.png"
            );

    public ParticleAcceleratorControllerGui(ParticleAcceleratorControllerTileEntity controller,
                                            ParticleAcceleratorPowerTileEntity powerPort,
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
        // TODO(CM): Get actual values from power ports
        long capacity = getPowerCapacity();
        long stored = getPowerStored();
        controllerPowerBar.draw(stored, capacity);
    }

    private long getPowerCapacity() {
        return powerPort.getField(0);
    }

    private long getPowerStored() {
        return powerPort.getField(1);
    }
}
