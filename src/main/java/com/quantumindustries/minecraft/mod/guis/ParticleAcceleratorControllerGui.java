package com.quantumindustries.minecraft.mod.guis;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.containers.ParticleAcceleratorControllerContainer;
import com.quantumindustries.minecraft.mod.fluids.ModFluids;
import com.quantumindustries.minecraft.mod.guis.util.FluidBar;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorBeamType;
import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorControllerTileEntity;
import com.quantumindustries.minecraft.mod.util.BaseMachineContainer;
import net.darkhax.tesla.lib.PowerBar;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParticleAcceleratorControllerGui extends GuiContainer {

    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private PowerBar controllerPowerBar;
    private FluidBar controllerFluidBar;
    private GuiButton leftBeamSource;
    private GuiButton rightBeamSource;
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
        leftBeamSource = new GuiButton(0, guiLeft + 100, guiTop + 50, 70, 20, controller.getLeftBeam().toString());
        rightBeamSource = new GuiButton(1, guiLeft + 100, guiTop + 30, 70, 20, controller.getRightBeam().toString());
        buttonList.add(leftBeamSource);
        buttonList.add(rightBeamSource);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 0) {
            controller.cycleLeftBeam();
        }
        if(button.id == 1) {
            controller.cycleRightBeam();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        long powerCapacity = getPowerCapacity();
        long powerStored = getPowerStored();

        int fluidCapacity = getFluidCapacity();
        int fluidStored = getFluidStored();

        ParticleAcceleratorBeamType leftBeam = getLeftBeam();
        ParticleAcceleratorBeamType rightBeam = getRightBeam();

        for(GuiButton guiButton : this.buttonList) {
            if(guiButton == leftBeamSource) {
                guiButton.displayString = leftBeam.toString();
            }
            if(guiButton == rightBeamSource) {
                guiButton.displayString = rightBeam.toString();
            }
        }

        controllerPowerBar.draw(powerStored, powerCapacity);
        controllerFluidBar.draw(fluidStored, fluidCapacity);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        long capacity = getPowerCapacity();
        long stored = getPowerStored();

        int fluidCapacity = getFluidCapacity();
        int fluidStored = getFluidStored();

        ParticleAcceleratorBeamType leftBeam = getLeftBeam();
        ParticleAcceleratorBeamType rightBeam = getRightBeam();

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
        for(GuiButton guiButton : this.buttonList) {
            if(guiButton.isMouseOver()) {
                guiButton.drawButtonForegroundLayer(mouseX - guiLeft, mouseY - guiTop);
                break;
            }
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

    private ParticleAcceleratorBeamType getLeftBeam() {
        ParticleAcceleratorBeamType tempBeam = ParticleAcceleratorBeamType.PROTON;
        int beam = (int) controller.getField(4);
        return tempBeam.setOrdinalType(beam);
    }

    private ParticleAcceleratorBeamType getRightBeam() {
        ParticleAcceleratorBeamType tempBeam = ParticleAcceleratorBeamType.PROTON;
        int beam = (int) controller.getField(5);
        return tempBeam.setOrdinalType(beam);
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
