package com.quantumindustries.minecraft.mod.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

public class MultiOptionButton extends GuiButton {

    enum BEAM_TYPE {
        PROTON, NEUTRON, ION, ELECTRON
    }

    private BEAM_TYPE beam;

    public MultiOptionButton(int buttonId, int x, int y, int width, int height, String buttonText) {
        super(buttonId, x, y, width, height, buttonText);
        beam = BEAM_TYPE.PROTON;
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        super.playPressSound(soundHandlerIn);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return super.mousePressed(mc, mouseX, mouseY);
    }
}
