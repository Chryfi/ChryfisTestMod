package com.chryfi.test.client.gui;

import com.chryfi.test.utils.Color;
import org.checkerframework.checker.units.qual.C;

public class GuiPanel extends GuiElement {
    private GuiElement navbar;
    private GuiElement body;

    public GuiPanel() {
        super(true, true);

        this.w = 1F;
        this.h = 1F;

        this.navbar = new GuiElement(true, false);
        this.navbar.h = 25;
        this.navbar.w = 1F;

        this.body = new GuiElement(true, true);
        this.body.w = 1F;
        this.body.h = 1F;
        this.body.y = 0;

        this.addChildren(this.navbar);
    }
}
