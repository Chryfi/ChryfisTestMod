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

    private Stack<Vector2i> posStack = new Stack<>();
    private Stack<Vector2i> scaleStack = new Stack<>();
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

    public void pushCoordinates(int posX, int posY, int width, int height) {
        this.pushPos(posX, posY);
        this.pushScale(width, height);
    }

    public void popCoordinates() {
        this.popPos();
        this.popScale();
    }

    private void pushPos(int posX, int posY) {
        this.posStack.push(new Vector2i(posX, posY));
    }

    public Vector2i getPos() {
        return new Vector2i(this.posStack.peek());
    }

    private void popPos() {
        this.posStack.pop();
    }

    private void pushScale(int width, int height) {
        this.scaleStack.push(new Vector2i(width, height));
    }

    public Vector2i getScale() {
        return new Vector2i(this.scaleStack.peek());
    }

    private void popScale() {
        this.scaleStack.pop();
    }
}
