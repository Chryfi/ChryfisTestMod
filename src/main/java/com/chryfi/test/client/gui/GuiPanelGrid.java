package com.chryfi.test.client.gui;

import com.chryfi.test.utils.Color;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import org.joml.Vector2i;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiPanelGrid extends GuiElement {
    @Nullable
    private GuiPanel panel;
    public DIRECTION direction;
    private final List<GuiPanelGrid> grids = new ArrayList<>();

    public GuiPanelGrid() {
        this(null, DIRECTION.HORIZONTAL);
    }

    public GuiPanelGrid(GuiPanel panel, DIRECTION direction) {
        super(true, true, true, true, true);

        this.direction = direction;
        this.panel = panel;
    }

    public void setPanel(GuiPanel panel) {
        this.panel = panel;
        this.grids.clear();
    }

    @Override
    public void addChildren(GuiElement... elements) {
        for (int i = 0; i < elements.length; i++) {
            if (!(elements[i] instanceof GuiPanelGrid)) {
                continue;
            }

            GuiPanelGrid element = (GuiPanelGrid) elements[i];

            if (this.grids.size() == 0) {
                this.direction = element.direction;
            }

            if (element.direction != this.direction) {
                throw new UnsupportedOperationException("not good :(");
            }

            this.grids.add(element);
        }
    }

    public void render(GuiContext context) {
        Vector2i scale = this.calculateGlobalScale(context);
        Vector2i pos = this.calculateGlobalPos(context);
        Vector2i posEnd = new Vector2i(pos).add(scale);

        context.pushCoordinates(pos.x, pos.y, scale.x, scale.y);

        if (this.panel != null) {
            this.panel.render(context);
        }

        if (pos.x != 0) {
            this.vLine(new PoseStack(), pos.x + 1, pos.y - 1, posEnd.y, new Color(0,0,0, 1).getRGBAColor());
            this.vLine(new PoseStack(), pos.x + 2, pos.y - 1, posEnd.y, new Color(0,0,0, 1).getRGBAColor());
        }

        if (posEnd.x != 0) {
            this.vLine(new PoseStack(), posEnd.x - 1, pos.y - 1, posEnd.y, new Color(0,0,0, 1).getRGBAColor());
            this.vLine(new PoseStack(), posEnd.x - 2, pos.y - 1, posEnd.y, new Color(0,0,0, 1).getRGBAColor());
        }

        if (pos.y != 0) {
            this.hLine(new PoseStack(), pos.x - 1, posEnd.x - 1, pos.y, new Color(0,0,0, 1).getRGBAColor());
            this.hLine(new PoseStack(), pos.x - 1, posEnd.x - 1, pos.y + 1, new Color(0,0,0, 1).getRGBAColor());
        }

        if (posEnd.y != 0) {
            this.hLine(new PoseStack(), pos.x - 1, posEnd.x - 1, posEnd.y - 1, new Color(0,0,0, 1).getRGBAColor());
            this.hLine(new PoseStack(), pos.x - 1, posEnd.x - 1, posEnd.y - 2, new Color(0,0,0, 1).getRGBAColor());
        }

        for (int i = this.grids.size() - 1; i >= 0; i--) {
            this.grids.get(i).render(context);
        }

        context.popCoordinates();
    }

    public enum DIRECTION {
        VERTICAL,
        HORIZONTAL,
    }
}
