package com.chryfi.test.client.rendering;

import com.chryfi.test.Test;
import com.chryfi.test.utils.rendering.GLUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WindowHandler {
    public static int width = 1280;
    public static int height = 720;
    public static int x = 0;
    public static int y = 0;
    public static boolean resize = false;
    public static boolean overwrite = false;
    private static Runnable resizingQueued;

    /**
     * Call this after everything has been rendered
     * to accurately capture and manipulate the framebuffer's contents
     */
    public static void handleWindow() {
        if (resizingQueued != null) {
            resizingQueued.run();
            resizingQueued = null;
        }
    }

    /**
     * This queues resizing the Minecraft {@link com.mojang.blaze3d.platform.Window} instance.
     * The call is not immediately processed, it will be processed after Minecraft rendering calls have finished.
     * This helps with stuttering when constantly resizing every frame.
     */
    public static void queueResize(int width, int height) {
        resizingQueued = () -> {
            Object window = Minecraft.getInstance().getWindow();
            ((IMixinWindow) window).resize(Math.clamp(1, Integer.MAX_VALUE, width), Math.clamp(1, Integer.MAX_VALUE, height));
        };
    }

    public static void revertToDefault() {
        resizingQueued = () -> {
            Object window = Minecraft.getInstance().getWindow();
            ((IMixinWindow) window).resize(Math.clamp(1, Integer.MAX_VALUE, width), Math.clamp(1, Integer.MAX_VALUE, height));
            overwrite = false; //TODO why mc weird when closing sus amogus sus
        };
    }

    private static void buildUVQuad(BufferBuilder bufferBuilder, int x0, int y0, int x1, int y1) {
        bufferBuilder.vertex(x1, y0, -500).uv(1,1).endVertex();
        bufferBuilder.vertex(x0, y0, -500).uv(0,1).endVertex();
        bufferBuilder.vertex(x0, y1, -500).uv(0,0).endVertex();
        bufferBuilder.vertex(x1, y1, -500).uv(1,0).endVertex();
    }
}
