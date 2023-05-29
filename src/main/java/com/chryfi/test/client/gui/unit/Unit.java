package com.chryfi.test.client.gui.unit;

public class Unit extends LengthUnit {
    public Unit(float value) {
        super(value);
    }

    public Unit(int value) {
        super(value);
    }

    public float getValue() {
        return this.type == UnitType.AUTO ? 0F : this.value;
    }

    public void setAuto() {
        this.type = UnitType.AUTO;
    }
}
