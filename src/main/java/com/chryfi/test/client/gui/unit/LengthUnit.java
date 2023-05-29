package com.chryfi.test.client.gui.unit;

public class LengthUnit {
    protected float value;
    protected UnitType type;

    public LengthUnit(float value) {
        this.setValue(value);
    }

    public LengthUnit(int value) {
        this.setValue(value);
    }

    public float getValue() {
        return this.value;
    }

    public UnitType getType() {
        return this.type;
    }

    public void setValue(int value) {
        this.value = value;
        this.type = UnitType.PIXEL;
    }

    public void setValue(float value) {
        this.value = value;
        this.type = UnitType.PERCENTAGE;
    }
}

