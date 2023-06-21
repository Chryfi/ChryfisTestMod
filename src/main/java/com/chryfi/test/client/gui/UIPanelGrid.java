package com.chryfi.test.client.gui;

import com.chryfi.test.client.gui.utils.UIRendering;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class UIPanelGrid extends UIElement {
    @Nullable
    private UIPanel panel;
    @Nullable
    public DIRECTION direction;
    /**
     * For vertical layout this should be the left element.
     * For horizontal layout this should be the top element.
     */
    private UIPanelGrid grid0;
    /**
     * For vertical layout this should be the right element.
     * For horizontal layout this should be the bottom element.
     */
    private UIPanelGrid grid1;
    private final int borderThickness = 3;

    public UIPanelGrid(UIPanel panel) {
        this.setPanel(panel);
    }

    public UIPanelGrid(UIPanelGrid grid0, UIPanelGrid grid1) {
        this.grid0 = grid0;
        this.grid1 = grid1;
        this.grid0.parent = this;
        this.grid1.parent = this;
    }

    public void subdivide(DIRECTION direction, float ratio) {
        if (this.panel == null) {
            throw new UnsupportedOperationException("Unsupported call to subdivide. UIPanelGrid cant subdivide an already subdivided panelgrid.");
        }

        this.direction = direction;
        this.grid0 = new UIPanelGrid(this.panel);
        this.grid1 = new UIPanelGrid(new UIPanel());
        this.grid0.parent = this;
        this.grid1.parent = this;
        this.panel = null;

        if (direction == DIRECTION.HORIZONTAL) {
            this.grid0.width(1F).height(ratio);
            this.grid1.width(1F).height(1 - ratio);
        } else {
            this.grid0.height(1F).width(ratio);
            this.grid1.height(1F).width(1 - ratio);
        }
    }

    public UIPanelGrid getGrid0() {
        return this.grid0;
    }

    public UIPanelGrid getGrid1() {
        return this.grid1;
    }

    public void setPanel(UIPanel panel) {
        this.panel = panel;
        this.panel.parent = this;

        if (this.grid0 != null) this.grid0.parent = null;
        if (this.grid1 != null) this.grid1.parent = null;

        this.grid0 = null;
        this.grid1 = null;
    }

    @Override
    public List<UIElement> getChildren() {
        if (this.panel != null) {
            return new ArrayList<>(List.of(this.panel));
        }

        return new ArrayList<>(Arrays.asList(this.grid0, this.grid1));
    }

    @Override
    public void addChildren(UIElement... elements) {
        throw new UnsupportedOperationException("Unsupported call to addChildren. UIPanelGrid does not support this method.");
    }

    public void render(UIContext context) {
        for (UIElement child : this.getChildren()) {
            child.render(context);
        }

        this.renderThis(context);
    }

    @Override
    public void renderThis(UIContext context) {
        UIRendering.renderBorder(this.contentArea, this.borderThickness);

        if (this.panel != null) return;
        Area intersectionLine;
        Area grid0Area = this.grid0.getFlowArea();

        if (this.direction == DIRECTION.HORIZONTAL) {
            intersectionLine = new Area(grid0Area.getX(), grid0Area.getY() + grid0Area.getHeight() - this.grid0.borderThickness,
                    grid0Area.getWidth(), this.grid0.borderThickness + this.grid1.borderThickness);
        } else {
            intersectionLine = new Area(grid0Area.getX() + grid0Area.getWidth() - this.grid0.borderThickness,
                    grid0Area.getY(), this.grid0.borderThickness + this.grid1.borderThickness, grid0Area.getHeight());
        }

        if (intersectionLine.isInside(context.mouseX, context.mouseY)) {
            if (this.direction == DIRECTION.HORIZONTAL) {
                glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR));
            } else {
                glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR));
            }
        }
    }

    /**
     * Override the default behavior to instead propagate the event from top to bottom.
     * This is needed because when you drag the edge of the child panelGrid, you might actually be dragging the bigger edge
     * of the parent grid which is what we want.
     */
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseKey, double dragX, double dragY) {
        for (UIElement child : this.getChildren()) {
            if (child.mouseDragged(mouseX, mouseY, mouseKey, dragX, dragY)) return true;
        }

        return this.mouseDrag(mouseX, mouseY, mouseKey, dragX, dragY);
    }

    public boolean mouseDrag(double mouseX, double mouseY, int mouseKey, double dragX, double dragY) {
        return false;
    }

    public enum DIRECTION {
        VERTICAL,
        HORIZONTAL,
    }
}
