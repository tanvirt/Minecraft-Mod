package com.quantumindustries.minecraft.mod.guis;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.containers.ParticleAcceleratorControllerContainer;
import com.quantumindustries.minecraft.mod.fluids.ModFluids;
import com.quantumindustries.minecraft.mod.guis.util.FluidBar;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorControllerTileEntity;
import com.quantumindustries.minecraft.mod.util.BaseMachineContainer;
import net.darkhax.tesla.lib.PowerBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;

import java.util.ArrayList;
import java.util.List;

public class ParticleAcceleratorControllerGui extends GuiContainer {

    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private PowerBar controllerPowerBar;
    private FluidBar controllerFluidBar;
    private MultiOptionButton leftBeamSource;
    private MultiOptionButton rightBeamSource;
    private BaseMachineContainer powerPort;
    private ParticleAcceleratorControllerTileEntity controller;

    private static final ResourceLocation background =
            new ResourceLocation(
                CustomMod.MODID,
                "textures/gui/acceleratorControllerGui.png"
            );

    public ParticleAcceleratorControllerGui(BaseMachineContainer powerPort, ParticleAcceleratorControllerTileEntity controller,
                                            ParticleAcceleratorControllerContainer container) {
        super(container);

        this.powerPort = powerPort;
        this.controller = controller;
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void initGui() {
        super.initGui();
        controllerPowerBar = new PowerBar(this, guiLeft + 10, guiTop + 20, PowerBar.BackgroundType.DARK);
        controllerFluidBar = new FluidBar(this, guiLeft + 30, guiTop + 20, true, ModFluids.plasma);
//        leftBeam = new CycleButton(this, 0, guiLeft, guiTop, ParticleAcceleratorBeamType.class);
//        leftBeamSource = new MultiOptionButton(0, guiLeft + 100, guiTop + 50, 70, 20, "Left Button");
//        rightBeamSource = new MultiOptionButton(2, guiLeft + 100, guiTop + 30, 70, 20, "Right Button");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        long powerCapacity = getPowerCapacity();
        long powerStored = getPowerStored();

        int fluidCapacity = getFluidCapacity();
        int fluidStored = getFluidStored();
        FMLLog.warning("Capacity: %d    Stored: %d", fluidCapacity, fluidStored);
//
//        leftBeamSource.drawButton(mc, mouseX, mouseY);
//        rightBeamSource.drawButton(mc, mouseX, mouseY);
        controllerPowerBar.draw(powerStored, powerCapacity);
        controllerFluidBar.draw(fluidStored, fluidCapacity);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        long capacity = getPowerCapacity();
        long stored = getPowerStored();

        int fluidCapacity = getFluidCapacity();
        int fluidStored = getFluidStored();

        if(checkPowerBarHover(mouseX, mouseY)) {
            ArrayList<String> powerBarString = new ArrayList<String>() {{
                add(getPowerHoverString(stored, capacity));
            }};
            drawHoveringText(powerBarString, mouseX - guiLeft, mouseY - guiTop);
        }
        if(checkFluidBarHover(mouseX, mouseY)) {
            ArrayList<String> fluidBarString = new ArrayList<String>() {{
                add(getFluidHoverString(fluidStored, fluidCapacity));
            }};
            drawHoveringText(fluidBarString, mouseX - guiLeft, mouseY - guiTop);
        }
    }

    private String getPowerHoverString(long stored, long capacity) {
        return stored + "/" + capacity + " RF";
    }

    private String getFluidHoverString(int stored, int capacity) {
        return stored + "/" + capacity + " mB";
    }

    private long getPowerCapacity() {
        return controller.getField(0);
    }

    private long getPowerStored() {
        return controller.getField(1);
    }

    private int getFluidCapacity() {
        return (int) controller.getField(2);
    }

    private int getFluidStored() {
        return (int) controller.getField(3);
    }

    private boolean checkPowerBarHover(int mouseX, int mouseY) {
        return controllerPowerBar.getX() <= mouseX && mouseX <= controllerPowerBar.getX() + PowerBar.WIDTH &&
                controllerPowerBar.getY() <= mouseY && mouseY <= controllerPowerBar.getY() + PowerBar.HEIGHT;
    }

    private boolean checkFluidBarHover(int mouseX, int mouseY) {
        return controllerFluidBar.getX() <= mouseX && mouseX <= controllerFluidBar.getX() + FluidBar.WIDTH &&
                controllerFluidBar.getY() <= mouseY && mouseY <= controllerFluidBar.getY() + FluidBar.HEIGHT;
    }

    @Override
    protected void drawHoveringText(List<String> textLines, int x, int y) {
        super.drawHoveringText(textLines, x, y);
    }
}
