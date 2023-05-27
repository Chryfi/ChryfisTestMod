package com.chryfi.test.client.gui;

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
public class UIBase extends Screen {
    private UIElement root;

    public UIBase(Minecraft minecraft) {
        super(GameNarrator.NO_TITLE);
        this.minecraft = minecraft;

        this.root = new UIElement();

        UIPropertyBuilder.setup(this.root)
                .height(GLUtils.getGLFWWindowSize()[1])
                .width(GLUtils.getGLFWWindowSize()[0])
                .marginTop(50)
                .backgroundColor(0.5F,0.5F,0.5F);

        UIElement test1 = new UIElement();
        UIPropertyBuilder.setup(test1)
                .width(0.35F)
                .height(0.15F)
                .marginBottom(100)
                .marginTop(50)
                .marginLeft(0.25F);

        UIElement test2 = new UIElement();
        UIPropertyBuilder.setup(test2)
                .width(200)
                .height(0.15F)
                .marginLeft(20)
                .marginTop(20);

        UIElement test3 = new UIElement();
        UIPropertyBuilder.setup(test3)
                .width(300)
                .height(0.35F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test4 = new UIElement();
        UIPropertyBuilder.setup(test4)
                .width(250)
                .height(0.05F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test5 = new UIElement();
        UIPropertyBuilder.setup(test5)
                .width(300)
                .height(0.1F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test6 = new UIElement();
        UIPropertyBuilder.setup(test6)
                .width(300)
                .height(0.2F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test7 = new UIElement();
        UIPropertyBuilder.setup(test7)
                .width(700)
                .height(0.35F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test8 = new UIElement();
        UIPropertyBuilder.setup(test8)
                .width(700)
                .height(0.1F);

        this.root.addChildren(test1, test2, test3, test4, test5, test6, test7, test8);


        /*UIElement column = new UIElement();
        UIPropertyBuilder.setup(column)
                .width(0.2F)
                .height(0.5F)
                .marginLeft(30)
                .backgroundColor(0,0,0,0);

        UIElement row1 = createTestRow();

        UIElement column2 = new UIElement();
        UIPropertyBuilder.setup(column2)
                .width(0.5F)
                .height(1F);

        UIElement column3 = new UIElement();
        UIPropertyBuilder.setup(column3)
                .width(0.5F)
                .height(1F);
        UIElement row2 = createTestRow();
        UIPropertyBuilder.setup(row2)
                .width(0.75F)
                .height(0.75F)
                .anchorY(0.5F)
                .y(0.5F)
                .anchorX(0.5F)
                .x(0.5F)
                .marginTop(0);

        UIElement row3 = createTestRow();
        UIPropertyBuilder.setup(row3)
                .width(0.5F)
                .anchorX(0.5F)
                .x(0.5F)
                .marginTop(0);
        column2.addChildren(row2);
        column3.addChildren(row3);

        row1.addChildren(column2, column3);

        column.addChildren(row1);

        this.root.addChildren(createTestFilledColumn(), createTestFilledColumn(), column);*/
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

    private UIElement createTestRow() {
        UIElement row = new UIElement();
        UIPropertyBuilder.setup(row)
                .width(1F)
                .height(64)
                .marginTop(10);

        return row;
    }

    private UIElement createTestFilledColumn() {
        UIElement column = new UIElement();
        UIPropertyBuilder.setup(column)
                .width(0.3F)
                .height(1F)
                .marginLeft(30)
                .backgroundColor(0,0,0,0);

        column.addChildren(createTestRow(), createTestRow(), createTestRow(), createTestRow(), createTestRow());

        return column;
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