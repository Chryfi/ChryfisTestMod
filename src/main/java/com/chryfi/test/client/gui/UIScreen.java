package com.chryfi.test.client.gui;

import com.chryfi.test.client.rendering.WindowHandler;
import com.chryfi.test.utils.rendering.GLUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.checkerframework.checker.guieffect.qual.UI;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

@OnlyIn(Dist.CLIENT)
public class UIScreen extends Screen {
    private UIElement root;
    public static boolean debug;

    public UIScreen(Minecraft minecraft) {
        super(GameNarrator.NO_TITLE);
        this.minecraft = minecraft;

        this.root = new UIElement();

        UIPropertyBuilder.setup(this.root)
                .height(GLUtils.getGLFWWindowSize()[1])
                .width(GLUtils.getGLFWWindowSize()[0])
                .backgroundColor(0.0F,0.0F,0.0F, 0.0F);

        UIViewport viewport = new UIViewport();


        UIElement row1 = TestStuff.createTestRow(new UIElement(), new UIElement(), new UIElement(), new UIElement());
        UIPropertyBuilder.setup(row1).width(0.5F);
        UIElement row2 = TestStuff.createTestRow(new UIElement(), viewport, new UIElement());
        UIPropertyBuilder.setup(row2).width(1F).height(0.25F);
        UIElement row3 = TestStuff.createTestRow(new UIElement(), new UIElement(), new UIElement());
        UIPropertyBuilder.setup(row3).width(0.5F);
        UIElement row4 = TestStuff.createTestRow(new UIElement(), new UIElement(), new UIElement());
        UIPropertyBuilder.setup(row4).width(1F).paddingLeft(0.5F);
        UIElement row5 = TestStuff.createTestRow(new UIElement(), new UIElement(), new UIElement(), new UIElement(), new UIElement());
        UIPropertyBuilder.setup(row5).width(1F).height(0.1F);

        this.root.addChildren(row1, row2, row3, row4, row5);

        viewport.addChildren(row5);
    }

    /**
     * This gets also called when resizing,
     * see {@link Screen#resize(Minecraft, int, int)} and {@link Screen#rebuildWidgets()}
     */
    @Override
    protected void init() {
        UIPropertyBuilder.setup(this.root)
                .height(GLUtils.getGLFWWindowSize()[1])
                .width(GLUtils.getGLFWWindowSize()[0]);

        this.root.resize();
    }

    @Override
    public void onClose() {
        super.onClose();

        this.root.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        int windowWidth = GLUtils.getGLFWWindowSize()[0];
        int windowHeight = GLUtils.getGLFWWindowSize()[1];

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

        this.root.render(context);

        stack.popPose();

        RenderSystem.restoreProjectionMatrix();
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, oldFramebufferID);
    }

    @Override
    public void tick() { }

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
