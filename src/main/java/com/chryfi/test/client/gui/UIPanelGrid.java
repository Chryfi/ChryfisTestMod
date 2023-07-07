package com.chryfi.test.client.gui;

import com.chryfi.test.client.gui.utils.UIRendering;
import com.chryfi.test.utils.Color;
import net.minecraft.client.Minecraft;
import org.checkerframework.checker.guieffect.qual.UI;
import org.joml.Math;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private final int borderThickness = 2;
    private boolean awaitDragging = false;
    private boolean isDragging = false;

    public UIPanelGrid(UIElement parent, UIPanel panel) {
        this(panel);

        if (parent instanceof UIPanelGrid) {
            throw new IllegalArgumentException("Parent in the constructor cant be of the type UIPanelGrid. Use the subdivide method to add children.");
        }

        parent.addChildren(this);
    }

    protected UIPanelGrid(UIPanel panel) {
        this.setPanel(panel);
    }

    public UIPanelGrid(UIElement parent, UIPanelGrid grid0, UIPanelGrid grid1) {
        this(grid0, grid1);

        if (parent instanceof UIPanelGrid) {
            throw new IllegalArgumentException("Parent in the constructor cant be of the type UIPanelGrid. Use the subdivide method to add children.");
        }

        parent.addChildren(this);
    }

    protected UIPanelGrid(UIPanelGrid grid0, UIPanelGrid grid1) {
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
        this.panel.parent = null;
        this.grid0 = new UIPanelGrid(this.panel);
        this.grid1 = new UIPanelGrid(new UIPanel(new UIElement(), new UIElement()));
        this.grid0.parent = this;
        this.grid1.parent = this;
        this.panel = null;

        this.paddingTop(0);
        this.paddingBottom(0);
        this.paddingLeft(0);
        this.paddingRight(0);

        if (direction == DIRECTION.HORIZONTAL) {
            this.grid0.width(1F).height(ratio);
            this.grid1.width(1F).height(1 - ratio);
        } else {
            this.grid0.height(1F).width(ratio);
            this.grid1.height(1F).width(1 - ratio);
        }

        this.getRoot().resize(new DocumentFlowRow());
    }

    public Optional<UIPanelGrid> getGrid0() {
        return Optional.ofNullable(this.grid0);
    }

    public Optional<UIPanelGrid> getGrid1() {
        return Optional.ofNullable(this.grid1);
    }

    public void setPanel(UIPanel panel) {
        if (this.panel != null) {
            this.panel.parent = null;
        }

        this.panel = panel;
        this.panel.parent = this;

        this.paddingTop(this.borderThickness);
        this.paddingBottom(this.borderThickness);
        this.paddingLeft(this.borderThickness);
        this.paddingRight(this.borderThickness);

        if (this.grid0 != null) this.grid0.parent = null;
        if (this.grid1 != null) this.grid1.parent = null;

        this.grid0 = null;
        this.grid1 = null;

        this.getRoot().resize(new DocumentFlowRow());
    }

    public Optional<UIPanel> getPanel() {
        return Optional.ofNullable(this.panel);
    }

    @Override
    public List<UIElement> getChildren() {
        if (this.panel != null) {
            return new ArrayList<>(Arrays.asList(this.panel));
        }

        return new ArrayList<>(Arrays.asList(this.grid0, this.grid1));
    }

    @Override
    public void addChildren(UIElement... elements) {
        throw new UnsupportedOperationException("Unsupported call to addChildren. UIPanelGrid does not support this method.");
    }

    @Override
    public void removeChild(UIElement element) {
        if (element == null) return;

        if (element == this.panel) {
            this.parent.removeChild(this);
        }

        if (element == this.grid0) {
            float height = 1F;
            float width = 1F;

            if (this.parent instanceof UIPanelGrid parentGrid) {
                if (parentGrid.direction == DIRECTION.HORIZONTAL) {
                    height = this.getTransformation().getHeight().getValue();
                } else if (parentGrid.direction == DIRECTION.VERTICAL) {
                    width = this.getTransformation().getWidth().getValue();
                }
            }

            this.grid1.width(width);
            this.grid1.height(height);

            this.parent.replaceChild(this, this.grid1);


        } else if (element == this.grid1) {
            float height = 1F;
            float width = 1F;

            if (this.parent instanceof UIPanelGrid parentGrid) {
                if (parentGrid.direction == DIRECTION.HORIZONTAL) {
                    height = this.getTransformation().getHeight().getValue();
                } else if (parentGrid.direction == DIRECTION.VERTICAL) {
                    width = this.getTransformation().getWidth().getValue();
                }
            }

            this.grid0.width(width);
            this.grid0.height(height);

            this.parent.replaceChild(this, this.grid0);
        }
    }

    @Override
    public void replaceChild(UIElement child, UIElement replacement) {
        if (child == null) {
            return;
        }

        if (child == this.panel && replacement instanceof UIPanel) {
            this.setPanel((UIPanel) replacement);
        }

        if (replacement instanceof UIPanelGrid) {
            if (child == this.grid0) {
                this.grid0.parent = null;
                this.grid0 = (UIPanelGrid) replacement;
                this.grid0.parent = this;
                this.getRoot().resize(new DocumentFlowRow());
            } else if (child == this.grid1) {
                this.grid1.parent = null;
                this.grid1 = (UIPanelGrid) replacement;
                this.grid1.parent = this;
                this.getRoot().resize(new DocumentFlowRow());
            }
        }
    }

    protected void setGridDimension(UIPanelGrid grid, float w, float h) {
        if (this.direction == DIRECTION.HORIZONTAL) {
            grid.width(1F);
            grid.height(h);
        } else if (this.direction == DIRECTION.VERTICAL) {
            grid.width(w);
            grid.height(1F);
        }
    }

    @Override
    public void postRender(UIContext context) {
        this.contentArea.renderBorder(this.borderThickness, new Color(0,0,0,1));

        if (this.panel != null) return;

        if (this.isOnEdge(context.getMouseX(), context.getMouseY()) || this.isDragging) {
            if (this.grid0.isOnEdge(context.getMouseX(), context.getMouseY())
                    || this.grid1.isOnEdge(context.getMouseX(), context.getMouseY())) {
                context.prepareCursor(GLFW_RESIZE_ALL_CURSOR);
            } else if (this.direction == DIRECTION.HORIZONTAL) {
                context.prepareCursor(GLFW_VRESIZE_CURSOR);
            } else {
                context.prepareCursor(GLFW_HRESIZE_CURSOR);
            }
        }
    }

    protected boolean isOnEdge(double x, double y) {
        if (this.panel != null) return false;
        Area intersectionLine;
        Area grid0Area = this.grid0.getFlowArea();

        if (this.direction == DIRECTION.HORIZONTAL) {
            intersectionLine = new Area(grid0Area.getX(), grid0Area.getY() + grid0Area.getHeight() - this.grid0.borderThickness - 2,
                    grid0Area.getWidth(), this.grid0.borderThickness + this.grid1.borderThickness + 4);
        } else {
            intersectionLine = new Area(grid0Area.getX() + grid0Area.getWidth() - this.grid0.borderThickness,
                    grid0Area.getY() - 2, this.grid0.borderThickness + this.grid1.borderThickness, grid0Area.getHeight() + 4);
        }

        return intersectionLine.isInside(x, y);
    }

    public boolean mouseClick(UIContext context) {
        if (this.panel == null && this.isOnEdge(context.getMouseX(), context.getMouseY())) {
            this.awaitDragging = true;

            return false;
        }

        return false;
    }

    public boolean mouseRelease(UIContext context) {
        if (this.awaitDragging) {
            this.awaitDragging = false;
            this.isDragging = false;

            return false;
        }

        return false;
    }

    /**
     * Override the default behavior to instead propagate the event from top to bottom.
     * This is needed because when you drag the edge of the child panelGrid, you might actually be dragging the bigger edge
     * of the parent grid which is what we want.
     */
    public boolean mouseDragged(UIContext context, double dragX, double dragY) {
        if (this.mouseDrag(context, dragX, dragY)) return true;

        for (UIElement child : this.getChildren()) {
            if (child.mouseDragged(context, dragX, dragY)) {
                return true;
            }
        }

        return false;
    }

    public boolean mouseDrag(UIContext context, double dragX, double dragY) {
        if (this.awaitDragging) {
            this.isDragging = true;

            if (this.direction == DIRECTION.VERTICAL) {
                int grid0Width = ((int)context.getMouseX()) - this.grid0.contentArea.getX();
                int widthDiff0 = this.grid0.contentArea.getWidth() - grid0Width;
                int minWidth = 50;
                int maxWidth = this.grid0.contentArea.getWidth() + this.grid1.contentArea.getWidth() - 50;
                this.grid0.width(Math.clamp(minWidth, maxWidth, grid0Width));
                this.grid1.width(Math.clamp(minWidth, maxWidth, this.grid1.contentArea.getWidth() + widthDiff0));
            } else {
                this.grid0.height(((int)context.getMouseY()) - this.grid0.contentArea.getY());
                this.grid1.height(this.grid1.contentArea.getHeight() - (((int)context.getMouseY()) - this.grid1.contentArea.getY()));
            }

            this.grid0.mouseDrag(context, dragX, dragY);
            this.grid1.mouseDrag(context, dragX, dragY);

            this.parent.resize(new DocumentFlowRow());

            return true;
        }

        return false;
    }

    public enum DIRECTION {
        VERTICAL,
        HORIZONTAL,
    }
}
