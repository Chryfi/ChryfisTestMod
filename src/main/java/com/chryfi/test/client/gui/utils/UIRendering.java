package com.chryfi.test.client.gui.utils;

import com.chryfi.test.client.gui.Area;
import com.chryfi.test.utils.Color;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

public class UIRendering {
    public static void renderBorder(Area area, int thickness) {
        int color = new Color(1,1,1, 1).getRGBAColor();

        int x00 = area.getX();
        int x01 = x00 + thickness;

        int x10 = area.getX() + area.getWidth();
        int x11 = x10 - thickness;

        int y00 = area.getY();
        int y01 = y00 + thickness;

        int y10 = area.getY() + area.getHeight();
        int y11 = y10 - thickness;

        GuiComponent.fill(new PoseStack(), x00, area.getY(), x01, area.getY() + area.getHeight(), color);
        GuiComponent.fill(new PoseStack(), x10, area.getY(), x11, area.getY() + area.getHeight(), color);

        GuiComponent.fill(new PoseStack(), area.getX(), y00, area.getX() + area.getWidth(), y01, color);
        GuiComponent.fill(new PoseStack(), area.getX(), y10, area.getX() + area.getWidth(), y11, color);
    }

    public static void hLine(int x0, int x1, int y, int color) {
        if (x1 < x0) {
            int i = x0;
            x0 = x1;
            x1 = i;
        }

        GuiComponent.fill(new PoseStack(), x0, y, x1, y + 1, color);
    }

    public static void vLine(int x, int y0, int y1, int color) {
        if (y1 < y0) {
            int i = y0;
            y0 = y1;
            y1 = i;
        }

        GuiComponent.fill(new PoseStack(), x, y0, x + 1, y1, color);
    }
}
