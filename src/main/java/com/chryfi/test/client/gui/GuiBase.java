package com.chryfi.test.client.gui;

import com.chryfi.test.utils.rendering.GLUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

@OnlyIn(Dist.CLIENT)
public class GuiBase extends Screen {
    private GuiElement root;

    public GuiBase(Minecraft minecraft) {
        super(GameNarrator.NO_TITLE);
        this.minecraft = minecraft;
    }

    @Override
    protected void init() {
        this.root = new GuiPanelGrid();
        this.root.w = 1F;
        this.root.h = 1F;

        GuiPanelGrid test1 = new GuiPanelGrid();
        test1.h = 0.5F;
        test1.w = 1F;
        test1.y = 0.5F;
        test1.setPanel(new GuiPanel());

        GuiPanelGrid test2 = new GuiPanelGrid();
        test2.h = 1 - test1.h;
        test2.w = 1F;
        test2.setPanel(new GuiPanel());

        this.root.addChildren(test1, test2);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        int windowWidth = Minecraft.getInstance().getWindow().getScreenWidth();
        int windowHeight = Minecraft.getInstance().getWindow().getScreenHeight();

        int oldFramebufferID = GLUtils.getCurrentFramebufferID();
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, 0);
        RenderSystem.viewport(0, 0, windowWidth, windowHeight);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Matrix4f matrix4f = (new Matrix4f()).setOrtho(0.0F, windowWidth, windowHeight, 0.0F, 1000.0F, 3000F);
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(matrix4f);

        RenderSystem.enableDepthTest();
        stack.pushPose();
        /* ensure GUI is rendered on top of Minecraft, as Minecraft viewport will be rendered later */
        stack.translate(0, 0, 1000F);

        GuiContext context = new GuiContext(mouseX, mouseY, partialTicks);
        context.pushCoordinates(0,0, windowWidth, windowHeight);

        this.root.render(context);

        context.popCoordinates();

        stack.popPose();

        RenderSystem.restoreProjectionMatrix();
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, oldFramebufferID);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseKey) {
        return this.root.mouseClicked(mouseX, mouseY, mouseKey);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseKey) {
        return this.root.mouseReleased(mouseX, mouseY, mouseKey);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseKey, double dragX, double dragY) {
        return this.root.mouseDragged(mouseX, mouseY, mouseKey, dragX, dragY);
    }
}
