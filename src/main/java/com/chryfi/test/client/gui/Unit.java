package com.chryfi.test.client.gui;

public class Unit {
    private float value;
    private TYPE type;

    public Unit(float value) {
        this.setValue(value);
    }

    public Unit(int value) {
        this.setValue(value);
    }

    public float getValue() {
        return this.value;
    }

    public TYPE getType() {
        return this.type;
    }

    public void setValue(int value) {
        this.value = value;
        this.type = TYPE.PIXEL;
    }

    public void setValue(float value) {
        this.value = value;
        this.type = TYPE.PERCENTAGE;
    }

    public enum TYPE {
        PIXEL,
        PERCENTAGE
    }
}

