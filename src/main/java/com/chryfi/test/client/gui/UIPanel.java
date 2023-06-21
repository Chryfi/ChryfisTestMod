package com.chryfi.test.client.gui;

public class UIPanel extends UIElement {
    private UIElement navbar;
    private UIElement body;

    public UIPanel(UIElement navbar, UIElement body) {
        this.width(1F).height(1F);
        this.navbar = navbar.width(1F).height(50).absolute();
        this.body = body.width(1F).height(1F);
        this.addChildren(this.body, this.navbar);
    }
}
