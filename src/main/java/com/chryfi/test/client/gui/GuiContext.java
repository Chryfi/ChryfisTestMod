package com.chryfi.test.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.Stack;

/**
 * This is just a prototype to get the idea across, it is a shitty implementation.
 * It gets the job done.
 *
 * It's there to pass the context of the current rendering space to the GUI elements
 * so they can create global coordinates based on the parent space
 */
public class GuiContext {
    public int mouseX;
    public int mouseY;
    public float partialTicks;

    public GuiContext(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.partialTicks = partialTicks;
    }

    public void setMouse(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
}
