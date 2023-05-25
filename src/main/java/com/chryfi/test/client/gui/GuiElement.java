package com.chryfi.test.client.gui;

import com.chryfi.test.utils.Color;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiElement extends GuiComponent implements GuiEventListener {
    public float x;
    public float y;
    public float w;
    public float h;
    /**
     * Means that width and height are in percentage, acts like in css
     * range between 0 and 1. Width of 0.5 means 50% width of parent element.
     */
    public boolean relativeWidth;
    public boolean relativeHeight;
    public boolean relativePosition;
    /**
     * Calculate the position by multiplying e.g. x with the global width of this element.
     */
    public boolean percentageX;
    public boolean percentageY;
    public Color background;
    private final List<GuiElement> children = new ArrayList<>();

    public GuiElement(boolean relativeWidth, boolean relativeHeight) {
        this(relativeWidth, relativeHeight, true);
    }

    public GuiElement(boolean relativeWidth, boolean relativeHeight, boolean relativePosition) {
        this(relativeWidth, relativeHeight, relativePosition, false, false);
    }

    public GuiElement(boolean relativeWidth, boolean relativeHeight, boolean relativePosition, boolean pX, boolean pY) {
        this.background = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
        this.relativePosition = relativePosition;
        this.percentageX = pX;
        this.percentageY = pY;
    }

    public GuiElement() {
        this(false, false);
    }

    public void addChildren(GuiElement... elements) {
        this.children.addAll(Arrays.asList(elements));
    }

    public void render(GuiContext context) {
        Vector2i globalPos = this.calculateGlobalPos(context);
        Vector2i globalScale = this.calculateGlobalScale(context);

        GuiComponent.fill(new PoseStack(), globalPos.x, globalPos.y,
                globalPos.x + globalScale.x, globalPos.y + globalScale.y, this.background.getRGBAColor());

        context.pushCoordinates(globalPos.x, globalPos.y, globalScale.x, globalScale.y);

        for (GuiElement child : this.children) {
            child.render(context);
        }

        context.popCoordinates();
    }

    protected Vector2i calculateGlobalPos(GuiContext context) {
        int x, y, x0, y0;
        Vector2i scale = context.getScale();

        if (this.percentageX) {
            x0 = Math.round(scale.x * this.x);
        } else {
            x0 = (int) this.x;
        }

        if (this.percentageY) {
            y0 = Math.round(scale.y * this.y);
        } else {
            y0 = (int) this.y;
        }


        if (this.relativePosition) {
            x = context.getPos().x + x0;
            y = context.getPos().y + y0;
        } else {
            x = x0;
            y = y0;
        }

        return new Vector2i(x, y);
    }

    protected Vector2i calculateGlobalScale(GuiContext context) {
        int width, height;

        if (this.relativeWidth) {
            width = Math.round(this.w * context.getScale().x);
        } else {
            width = (int) this.w;
        }

        if (this.relativeHeight) {
            height = Math.round(this.h * context.getScale().y);
        } else {
            height = (int) this.h;
        }

        return new Vector2i(width, height);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseKey) {
        for (int i = this.children.size() - 1; i >= 0; i--) {
            GuiElement element = this.children.get(i);
            if (element.mouseClicked(mouseX, mouseY, mouseKey)) return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseKey) {
        for (int i = this.children.size() - 1; i >= 0; i--) {
            GuiElement element = this.children.get(i);
            if (element.mouseReleased(mouseX, mouseY, mouseKey)) return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseKey, double dragX, double dragY) {
        for (int i = this.children.size() - 1; i >= 0; i--) {
            GuiElement element = this.children.get(i);
            if (element.mouseDragged(mouseX, mouseY, mouseKey, dragX, dragY)) return true;
        }

        return false;
    }
}
