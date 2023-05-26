package com.chryfi.test.events;

import com.chryfi.test.client.gui.UIBase;
import com.mojang.blaze3d.platform.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

@Mod.EventBusSubscriber
public class KeyboardEventHandler {
    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        if (event.getAction() != InputConstants.PRESS) {
            return;
        }
        switch (event.getKey()) {
            case GLFW.GLFW_KEY_0:
                Minecraft.getInstance().setScreen(new UIBase(Minecraft.getInstance()));
                break;
            case GLFW.GLFW_KEY_1:
                long cursor = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
                glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), cursor);
                break;
        }
    }
}
