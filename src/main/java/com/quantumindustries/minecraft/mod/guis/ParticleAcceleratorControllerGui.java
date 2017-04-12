package com.quantumindustries.minecraft.mod.guis;

import com.quantumindustries.minecraft.mod.CustomMod;
import com.quantumindustries.minecraft.mod.containers.ParticleAcceleratorControllerContainer;
import com.quantumindustries.minecraft.mod.items.ModItems;
import com.quantumindustries.minecraft.mod.util.BaseMachineContainer;
import net.darkhax.tesla.lib.PowerBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ParticleAcceleratorControllerGui extends GuiContainer {

    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private PowerBar controllerPowerBar;
    private BaseMachineContainer powerPort;

    private static final ResourceLocation background =
            new ResourceLocation(
                CustomMod.MODID,
                "textures/gui/acceleratorControllerGui.png"
            );

    public ParticleAcceleratorControllerGui(BaseMachineContainer powerPort,
                                            ParticleAcceleratorControllerContainer container) {
        super(container);

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

        controllerPowerBar.draw(stored, capacity);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        long capacity = getPowerCapacity();
        long stored = getPowerStored();

        if(checkPowerBarHover(mouseX, mouseY)) {
            drawHoveringText(new ArrayList<String>() {{add(getPowerHoverString(stored, capacity));}}, mouseX - guiLeft, mouseY - guiTop);
        }
    }

    private String getPowerHoverString(long stored, long capacity) {
        return stored + "/" + capacity;
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
