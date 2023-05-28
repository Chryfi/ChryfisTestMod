package com.chryfi.test.client.gui.unit;

public class Unit extends LengthUnit {
    public Unit(float value) {
        super(value);
    }

    public Unit(int value) {
        super(value);
    }

    public void setAuto() {
        this.type = UnitType.AUTO;
    }
}
