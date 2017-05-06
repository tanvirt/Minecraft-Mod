package com.quantumindustries.minecraft.mod.guis;

import com.quantumindustries.minecraft.mod.multiblock.particleaccelerator.ParticleAcceleratorBeamType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

public class MultiOptionButton extends GuiButton {

//    private ParticleAcceleratorBeamType beam;

    public MultiOptionButton(int buttonId, int x, int y, int width, int height, String buttonText) {
        super(buttonId, x, y, width, height, buttonText);
//        beam = ParticleAcceleratorBeamType.PROTON;
//        displayString = beam.toString();
    }

    public void setDisplayString(String beam) {
        displayString = beam.toString();
    }

//    public ParticleAcceleratorBeamType getBeam() {
//        return beam;
//    }

//    @Override
//    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
//        boolean pressed = enabled && visible && mouseX >= xPosition && mouseY >= yPosition &&
//                mouseX < xPosition + width && mouseY < yPosition + height;
//        if(pressed) {
////            playPressSound();
//        }
//        return pressed;
//    }

//    public void cycleOptions() {
//        beam = beam.cycleBeam();
//        displayString = beam.toString();
//    }

}
