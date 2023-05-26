package com.chryfi.test.utils.rendering;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class GLUtils {

    /**
     * @return array of {width, height}
     */
    public static int[] getGLFWWindowSize() {
        int[] width = new int[1];
        int[] height = new int[1];

        GLFW.glfwGetWindowSize(Minecraft.getInstance().getWindow().getWindow(), width, height);

        return new int[]{width[0], height[0]};
    }

    public static int getCurrentFramebufferID() {
        int[] oldFramebufferId = new int[1];
        GL20.glGetIntegerv(GL30.GL_DRAW_FRAMEBUFFER_BINDING, oldFramebufferId);

        return oldFramebufferId[0];
    }
}
