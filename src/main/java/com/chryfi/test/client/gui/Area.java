package com.chryfi.test.client.gui;

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

    public boolean isInside(int x, int y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }
}
