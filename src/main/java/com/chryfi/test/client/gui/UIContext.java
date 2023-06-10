package com.chryfi.test.client.gui;

public class UIContext {
    public int mouseX;
    public int mouseY;
    public float partialTicks;

    public UIContext(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.partialTicks = partialTicks;
    }

    public void setMouse(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
}
