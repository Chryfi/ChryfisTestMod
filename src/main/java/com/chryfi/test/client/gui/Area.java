package com.chryfi.test.client.gui;

import com.chryfi.test.client.gui.utils.UIRendering;
import com.chryfi.test.utils.Color;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import org.joml.Math;

public class Area {
    private int x;
    private int y;
    private int width;
    private int height;

    public Area() {

    }

    public Area(int x, int y, int width, int height) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = Math.clamp(0, Integer.MAX_VALUE, x);
    }

    public void addX(int x) {
        this.x = Math.clamp(0, Integer.MAX_VALUE, this.x + x);
    }

    public int getY() {
        return Math.clamp(0, Integer.MAX_VALUE, this.y);
    }

    public void setY(int y) {
        this.y = Math.clamp(0, Integer.MAX_VALUE, y);
    }

    public void addY(int y) {
        this.y = Math.clamp(0, Integer.MAX_VALUE, this.y + y);
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = Math.clamp(0, Integer.MAX_VALUE, width);
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = Math.clamp(0, Integer.MAX_VALUE, height);
    }

    public boolean isInside(double x, double y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    public void render(Color color) {
        UIRendering.renderArea(this.x, this.y, this.x + this.width, this.y + this.height, color);
    }

    public void renderBorder(int thickness, Color color) {
        int x00 = this.x;
        int x01 = x00 + thickness;

        int x10 = this.x + this.width;
        int x11 = x10 - thickness;

        int y00 = this.y;
        int y01 = y00 + thickness;

        int y10 = this.y + this.height;
        int y11 = y10 - thickness;

        /* vertical border */
        UIRendering.renderArea(x00, y00, x01, y10, color);
        UIRendering.renderArea(x10, y00, x11, y10, color);

        /* horizontal border */
        UIRendering.renderArea(x00, y00, x10, y01, color);
        UIRendering.renderArea(x00, y10, x10, y11, color);
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }
}
