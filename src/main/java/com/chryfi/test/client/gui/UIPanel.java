package com.chryfi.test.client.gui;

import org.lwjgl.glfw.GLFW;

public class UIPanel extends UIElement {
    private UIElement navbar;
    private UIElement body;

    public UIPanel(UIElement navbar, UIElement body) {
        this.width(1F).height(1F);
        this.navbar = navbar.width(1F).height(10).absolute();
        this.body = body.width(1F).height(1F);
        this.addChildren(this.body, this.navbar);
    }

    @Override
    public boolean keyPress(UIContext context) {
        if (this.isMouseOver(context) && context.getKeyboardKey() == GLFW.GLFW_KEY_4) {
            this.remove();
            return true;
        }

        return false;
    }
}
