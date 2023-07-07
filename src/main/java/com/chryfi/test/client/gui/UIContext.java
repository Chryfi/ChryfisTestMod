package com.chryfi.test.client.gui;

import com.mojang.blaze3d.platform.Window;

import static org.lwjgl.glfw.GLFW.*;

public class UIContext {
    private boolean debug;
    private Window window;
    private double mouseX;
    private double mouseY;
    private int mouseKey;
    private int keyboardKey;
    private float partialTicks;
    /**
     * This cursor might not always be the cursor that is actually rendering.
     */
    private int glfwCursor;
    private final int defaultGlfwCursor = GLFW_ARROW_CURSOR;
    /**
     * The currently rendering cursor.
     */
    private int renderingCursor;

    public UIContext(Window window) {
        this.window = window;
        this.applyDefaultCursor();
    }

    public void setMouse(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public int getKeyboardKey() {
        return this.keyboardKey;
    }

    public void setKeyboardKey(int keyboardKey) {
        this.keyboardKey = keyboardKey;
    }

    public double getMouseX() {
        return this.mouseX;
    }

    public double getMouseY() {
        return this.mouseY;
    }

    public int getMouseKey() {
        return this.mouseKey;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public void setMouseKey(int mouseKey) {
        this.mouseKey = mouseKey;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    /**
     * This does not change the appearance of the cursor rendered.
     * After this method you need to still apply the cursor using {@link #applyCurrentCursor()}
     */
    public void resetCursor() {
        this.glfwCursor = this.defaultGlfwCursor;
    }

    public void applyDefaultCursor() {
        this.resetCursor();
        this.applyCurrentCursor();
    }

    public boolean cursorChanged() {
        return this.glfwCursor != this.defaultGlfwCursor;
    }

    /**
     * Applies the current cursor to GLFW using {@link org.lwjgl.glfw.GLFW#glfwSetCursor(long, long)}
     * This will change the appearance of the cursor on screen.
     */
    public void applyCurrentCursor() {
        if (this.renderingCursor != this.glfwCursor) {
            glfwSetCursor(this.window.getWindow(), glfwCreateStandardCursor(this.glfwCursor));
            this.renderingCursor = this.glfwCursor;
        }
    }

    /**
     * This does not change the rendering of the cursor. After this call the apply method needs to be called.
     * @param GLFW_CURSOR
     */
    public void prepareCursor(int GLFW_CURSOR) {
        this.glfwCursor = GLFW_CURSOR;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
