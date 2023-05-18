package com.chryfi.test.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class WindowHandler {
    private static int tick = 0;
    private static Vector2d pos;
    private static Vector2d velocity;
    private static int width = 1280;
    private static int height = 720;

    /**
     * Call this after everything has been rendered
     * to accurately capture and manipulate the framebuffer's contents
     */
    public static void handleWindow() {
        int windowWidth = Minecraft.getInstance().getWindow().getScreenWidth();
        int windowHeight = Minecraft.getInstance().getWindow().getScreenHeight();

        RenderSystem.viewport(0, 0, windowWidth, windowHeight);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        RenderSystem.enableTexture();
        Matrix4f matrix4f = (new Matrix4f()).setOrtho(0.0F, windowWidth, windowHeight, 0.0F, 1000.0F, 3000F);
        RenderSystem.setProjectionMatrix(matrix4f);
        /* idk why 0? Is this good or bad method call - simple bindTexture wouldn't do it :( ?*/
        RenderSystem.setShaderTexture(0, Minecraft.getInstance().getMainRenderTarget().getColorTextureId());

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        compute();

        int x0 = (int) Math.round(pos.x);
        int y0 = (int) Math.round(pos.y);

        buildUVQuad(bufferBuilder, x0, y0, x0 + width, y0 + height);

        BufferUploader.drawWithShader(bufferBuilder.end());

        /* resize after drawing, as resizing destroys the old framebuffers */
        Object window = Minecraft.getInstance().getWindow();
        ((IMixinWindow) window).resize(width, height);

        tick++;
    }

    /**
     * Update position, collision testing and resolve step.
     */
    public static void compute() {
        int windowWidth = getGLFWWindowSize()[0];
        int windowHeight = getGLFWWindowSize()[1];

        if (pos == null) {
            pos = new Vector2d();
            pos.x = windowWidth/2D - width/2D;
            pos.y = windowHeight/2D - height/2D;
        }

        if (velocity == null) {
            velocity = new Vector2d(2, 2);
        }

        width = (int) Math.round(((((Math.sin(tick * 0.01) + 1) * (1 - 0.25)) / 2) + 0.25) * 854);
        width = Math.clamp(0, (int) Math.round(windowWidth - pos.x), width);
        height = (int) Math.round(((((Math.cos(tick * 0.01) + 1) * (1 - 0.25)) / 2) + 0.25) * 480);
        height = Math.clamp(0, (int) Math.round(windowHeight - pos.y), height);

        pos.x += velocity.x;
        pos.y += velocity.y;

        if (pos.x <= 0) {
            pos.x = 0;
            velocity.x *= -1;
        }

        if (pos.y <= 0) {
            pos.y = 0;
            velocity.y *= -1;
        }

        if (pos.y + height >= windowHeight) {
            pos.y = windowHeight - height;
            velocity.y *= -1;
        }

        if (pos.x + width >= windowWidth) {
            pos.x = windowWidth - width;
            velocity.x *= -1;
        }
    }

    public static int[] frameBufferResize(int x, int y) {
        return new int[]{x, y};
    }

    private static int[] getGLFWWindowSize() {
        int[] width = new int[1];
        int[] height = new int[1];

        GLFW.glfwGetWindowSize(Minecraft.getInstance().getWindow().getWindow(), width, height);

        return new int[]{width[0], height[0]};
    }

    private static int getCurrentFramebufferID() {
        int[] oldFramebufferId = new int[1];
        GL20.glGetIntegerv(GL30.GL_DRAW_FRAMEBUFFER_BINDING, oldFramebufferId);

        return oldFramebufferId[0];
    }

    private static void buildUVQuad(BufferBuilder bufferBuilder, int x0, int y0, int x1, int y1) {
        bufferBuilder.vertex(x1, y0, 0).uv(1,1).endVertex();
        bufferBuilder.vertex(x0, y0, 0).uv(0,1).endVertex();
        bufferBuilder.vertex(x0, y1, 0).uv(0,0).endVertex();
        bufferBuilder.vertex(x1, y1, 0).uv(1,0).endVertex();
    }

}
