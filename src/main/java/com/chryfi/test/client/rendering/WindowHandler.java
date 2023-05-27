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
import java.util.Optional;

public class WindowHandler {
    public static int width = 1280;
    public static int height = 720;
    public static int x = 0;
    public static int y = 0;
    public static boolean resize = false;
    public static boolean overwrite = false;

    /**
     * Call this after everything has been rendered
     * to accurately capture and manipulate the framebuffer's contents
     */
    public static void handleWindow() {
        /**
         * DEACTIVATED - currently testing in {@link com.chryfi.test.client.gui.UIViewport}
         */
        if (true) return;

        int windowWidth = GLUtils.getGLFWWindowSize()[0];
        int windowHeight = GLUtils.getGLFWWindowSize()[1];

        RenderSystem.viewport(0, 0, windowWidth, windowHeight);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        /*
         * enable depth test so the GUI can render on top.
         * Translate this texture on the z axis in negative direction
         */
        RenderSystem.enableDepthTest();

        RenderSystem.enableTexture();
        Matrix4f matrix4f = (new Matrix4f()).setOrtho(0.0F, windowWidth, windowHeight, 0.0F, 1000.0F, 3000F);
        RenderSystem.setProjectionMatrix(matrix4f);
        /* idk why 0? Is this good or bad method call - simple bindTexture wouldn't do it :( ?*/
        RenderSystem.setShaderTexture(0, Minecraft.getInstance().getMainRenderTarget().getColorTextureId());

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        width = width == 0 ? 1 : width;
        height = height == 0 ? 1 : height;

        buildUVQuad(bufferBuilder, x, y, x + width, y + height);

        BufferUploader.drawWithShader(bufferBuilder.end());

        if (WindowHandler.resize) {
            Object window = Minecraft.getInstance().getWindow();
            ((IMixinWindow) window).resize(width, height);
            WindowHandler.resize = false;
        }

        RenderSystem.disableDepthTest();
    }

    public static void revertToDefault() {
        Object window = Minecraft.getInstance().getWindow();
        ((IMixinWindow) window).resize(GLUtils.getGLFWWindowSize()[0], GLUtils.getGLFWWindowSize()[1]);
    }

    private static void buildUVQuad(BufferBuilder bufferBuilder, int x0, int y0, int x1, int y1) {
        bufferBuilder.vertex(x1, y0, -500).uv(1,1).endVertex();
        bufferBuilder.vertex(x0, y0, -500).uv(0,1).endVertex();
        bufferBuilder.vertex(x0, y1, -500).uv(0,0).endVertex();
        bufferBuilder.vertex(x1, y1, -500).uv(1,0).endVertex();
    }
}
