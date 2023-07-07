package com.chryfi.test.client.gui.utils;

import com.chryfi.test.client.gui.Area;
import com.chryfi.test.utils.Color;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

/**
 * Abstraction layer for Minecraft with utility methods for UI rendering of simple shapes.
 */
public class UIRendering {
    public static void renderArea(int x0, int y0, int x1, int y1, Color color) {
        GuiComponent.fill(new PoseStack(), x0, y0, x1, y1, color.getRGBAColor());
    }

    public static void horizontalLine(int x0, int x1, int y, Color color) {
        if (x1 < x0) {
            int i = x0;
            x0 = x1;
            x1 = i;
        }

        renderArea(x0, y, x1, y + 1, color);
    }

    public static void verticalLine(int x, int y0, int y1, Color color) {
        if (y1 < y0) {
            int i = y0;
            y0 = y1;
            y1 = i;
        }

        renderArea(x, y0, x + 1, y1, color);
    }
}
