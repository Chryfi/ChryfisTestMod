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
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

@OnlyIn(Dist.CLIENT)
public class UIScreen extends Screen {
    private UIElement root;
    public static boolean debug;

    public UIScreen(Minecraft minecraft) {
        super(GameNarrator.NO_TITLE);
        this.minecraft = minecraft;

        this.root = new UIViewport();

        UIPropertyBuilder.setup(this.root)
                .height(Math.round(GLUtils.getGLFWWindowSize()[1] * 1F))
                .width(Math.round(GLUtils.getGLFWWindowSize()[0] * 0.5F))
                .backgroundColor(0.0F,0.0F,0.0F, 0.0F);



        UIElement row1 = createTestRow(new UIElement(), new UIElement(), new UIElement(), new UIElement());
        UIPropertyBuilder.setup(row1).width(0.51F);
        UIElement row2 = createTestRow(new UIElement(), new UIElement(), new UIElement());
        UIPropertyBuilder.setup(row2).width(0.51F);
        UIElement row3 = createTestRow(new UIElement(), new UIElement(), new UIElement());
        UIPropertyBuilder.setup(row3).width(0.51F);
        UIElement row4 = createTestRow(new UIElement(), new UIElement(), new UIElement());
        UIPropertyBuilder.setup(row4).width(1F).paddingLeft(0.5F);

        this.root.addChildren(row1, row2, row3, row4);
    }

    private void bigChungusTest() {
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
                .marginTop(40)
                .paddingLeft(100)
                .paddingRight(100);

        UIElement test8 = new UIElement();
        UIPropertyBuilder.setup(test8)
                .width(700)
                .height(0.1F);

        UIElement test9 = new UIElement();
        UIPropertyBuilder.setup(test9)
                .width(600)
                .height(0.1F);

        test7.addChildren(test9);

        this.root.addChildren(test1, test2, test3, test4, test5, test6, test7, test8);
    }

    /**
     * This gets also called when resizing,
     * see {@link Screen#resize(Minecraft, int, int)} and {@link Screen#rebuildWidgets()}
     */
    @Override
    protected void init() {
        UIPropertyBuilder.setup(this.root)
                .height(Math.round(GLUtils.getGLFWWindowSize()[1] * 1F))
                .width(Math.round(GLUtils.getGLFWWindowSize()[0] * 0.5F));

        this.root.resize();
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    private UIElement createTestRow(UIElement... elements) {
        UIElement row = new UIElement();
        UIPropertyBuilder.setup(row)
                .width(1F)
                .height(64)
                .marginTop(10)
                .backgroundColor(0,0,0,0);

        float w = 1F / elements.length;
        for (UIElement element : elements) {
            UIPropertyBuilder.setup(element).height(1F);

            UIElement colContainer = this.createTestContainerColumn(w);
            colContainer.addChildren(element);
            row.addChildren(colContainer);
        }

        return row;
    }

    private UIElement createTestContainerColumn(float width) {
        UIElement column = new UIElement();
        UIPropertyBuilder.setup(column)
                .width(width)
                .height(1F)
                .paddingLeft(16)
                .paddingRight(16)
                .backgroundColor(0,0,0,0);

        return column;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        /*int windowWidth = GLUtils.getGLFWWindowSize()[0];
        int windowHeight = GLUtils.getGLFWWindowSize()[1];

        int oldFramebufferID = GLUtils.getCurrentFramebufferID();
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, 0);
        RenderSystem.viewport(0, 0, windowWidth, windowHeight);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Matrix4f matrix4f = (new Matrix4f()).setOrtho(0.0F, windowWidth, windowHeight, 0.0F, 1000.0F, 3000F);
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(matrix4f);

        RenderSystem.enableDepthTest();
        stack.pushPose();*/
        /* ensure GUI is rendered on top of Minecraft, as Minecraft viewport will be rendered later */
        /*stack.translate(0, 0, 1000F);

        GuiContext context = new GuiContext(mouseX, mouseY, partialTicks);

        this.root.render(context);

        stack.popPose();

        RenderSystem.restoreProjectionMatrix();
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, oldFramebufferID);*/
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
