package com.chryfi.test.client.gui;

public class UIRootElement extends UIElement {
    private final UIContext context;

    public UIRootElement(UIContext context) {
        this.context = context;
    }

    public UIContext getContext() {
        return this.context;
    }
}
