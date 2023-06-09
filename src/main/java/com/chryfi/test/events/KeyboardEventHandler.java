package com.chryfi.test.events;

import com.chryfi.test.client.gui.UIScreen;
import com.mojang.blaze3d.platform.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

@Mod.EventBusSubscriber
public class KeyboardEventHandler {
    private static UIScreen screenTest = new UIScreen(Minecraft.getInstance());

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        if (event.getAction() != InputConstants.PRESS) {
            return;
        }
        switch (event.getKey()) {
            case GLFW.GLFW_KEY_0:
                Minecraft.getInstance().setScreen(screenTest);
                break;
            case GLFW.GLFW_KEY_1:
                screenTest = new UIScreen(Minecraft.getInstance());
                Minecraft.getInstance().setScreen(screenTest);
                break;
            case GLFW.GLFW_KEY_2:
                long cursor = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
                glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), cursor);
                break;
            case GLFW.GLFW_KEY_3:
                cursor = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
                glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), cursor);
                break;
        }
    }
}
