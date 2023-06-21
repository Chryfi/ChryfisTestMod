package com.chryfi.test.client.gui;

public class UIPanel extends UIElement {
    private UIElement navbar;
    private UIElement body;

    public UIPanel() {
        this.width(1F).height(1F);
        this.navbar = new UIElement().width(1F).height(50).absolute();
        this.body = new UIElement().width(1F).height(1F);
        this.addChildren(this.body, this.navbar);
    }
}
