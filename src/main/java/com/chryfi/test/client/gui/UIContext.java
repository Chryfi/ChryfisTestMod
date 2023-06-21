package com.chryfi.test.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.vehicle.Minecart;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_VRESIZE_CURSOR;

public class UIContext {
    public int mouseX;
    public int mouseY;
    public float partialTicks;
    private int glfwCursor;
    public boolean queueCursorReset;
    private final int defaltGlfwCursor = GLFW_ARROW_CURSOR;

    public UIContext() {
        this.resetCursor();
    }

    public void setMouse(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public void resetCursor() {
        if (this.cursorChanged()) {
            glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), glfwCreateStandardCursor(this.defaltGlfwCursor));
        }

        this.glfwCursor = this.defaltGlfwCursor;
        this.queueCursorReset = false;
    }

    public boolean cursorChanged() {
        return this.glfwCursor != this.defaltGlfwCursor;
    }

    public void setCursor(int GLFW_CURSOR) {
        this.glfwCursor = GLFW_CURSOR;

        if (this.cursorChanged()) {
            glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), glfwCreateStandardCursor(this.glfwCursor));

            this.queueCursorReset = false;
        }
    }
}
