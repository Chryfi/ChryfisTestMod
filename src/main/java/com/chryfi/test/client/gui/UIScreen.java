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

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursor;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

@OnlyIn(Dist.CLIENT)
public class UIScreen extends Screen {
    private UIElement root;
    private final UIContext context = new UIContext();
    public static boolean debug;
    private double lastMouseX;
    private double lastMouseY;

    public UIScreen(Minecraft minecraft) {
        super(GameNarrator.NO_TITLE);
        this.minecraft = minecraft;
        WindowHandler.overwrite = true;
        debug = false;
        this.root = new UIElement();

        this.root.height(GLUtils.getGLFWWindowSize()[1])
                .width(GLUtils.getGLFWWindowSize()[0])
                .backgroundColor(0.0F,0.0F,0.0F, 0.0F);

        UIViewport viewport = new UIViewport();


        UIElement row1 = TestStuff.createTestRow(new UIElement(), new UIElement(), new UIElement(), new UIElement());
        row1.width(0.5F);
        UIElement row2 = TestStuff.createTestRow(new UIElement(), viewport, new UIElement());
        row2.width(1F).height(0.25F);
        UIElement row3 = TestStuff.createTestRow(new UIElement(), new UIElement(), new UIElement());
        row3.width(0.5F);
        UIElement row4 = TestStuff.createTestRow(new UIElement(), new UIElement(), new UIElement());
        row4.width(1F).paddingLeft(0.5F);
        UIElement row5 = TestStuff.createTestRow(new UIElement(), new UIElement(), new UIElement(), new UIElement(), new UIElement());
        row5.width(1F).height(0.1F);

        UIPanelGrid rootGrid = new UIPanelGrid(new UIPanel(new UIElement(), new UIViewport()));
        rootGrid.width(1F).height(1F);
        rootGrid.subdivide(UIPanelGrid.DIRECTION.HORIZONTAL, 0.5F);
        rootGrid.getGrid0().subdivide(UIPanelGrid.DIRECTION.VERTICAL, 0.25F);

        this.root.addChildren(rootGrid);

        viewport.addChildren(row5);
    }

    /**
     * This gets also called when resizing,
     * see {@link Screen#resize(Minecraft, int, int)} and {@link Screen#rebuildWidgets()}
     */
    @Override
    protected void init() {
        this.root.height(GLUtils.getGLFWWindowSize()[1])
                .width(GLUtils.getGLFWWindowSize()[0]);

        this.root.resize(new DocumentFlowRow());
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
        if (this.minecraft == null) return;

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

        this.context.setMouse((int) this.minecraft.mouseHandler.xpos(), (int) this.minecraft.mouseHandler.ypos());
        this.context.partialTicks = partialTicks;
        if (this.context.cursorChanged()) {
            if (this.context.queueCursorReset) {
                this.context.resetCursor();
            } else {
                this.context.queueCursorReset = true;
            }
        }

        this.root.render(context);

        stack.popPose();

        RenderSystem.restoreProjectionMatrix();
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, oldFramebufferID);
    }

    @Override
    public void tick() { }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        double[] mouseXPoint = new double[1];
        double[] mouseYPoint = new double[1];
        glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(),
                mouseXPoint, mouseYPoint);

        mouseX = mouseXPoint[0];
        mouseY = mouseYPoint[0];

        this.root.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void afterMouseMove() {
        double[] mouseXPoint = new double[1];
        double[] mouseYPoint = new double[1];
        glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(),
                mouseXPoint, mouseYPoint);

        this.lastMouseX = mouseXPoint[0];
        this.lastMouseY = mouseYPoint[0];
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseKey) {
        mouseX = this.minecraft.mouseHandler.xpos();
        mouseY = this.minecraft.mouseHandler.ypos();
        return this.root.mouseClicked(mouseX, mouseY, mouseKey);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseKey) {
        mouseX = this.minecraft.mouseHandler.xpos();
        mouseY = this.minecraft.mouseHandler.ypos();
        return this.root.mouseReleased(mouseX, mouseY, mouseKey);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseKey, double dragX, double dragY) {
        double[] mouseXPoint = new double[1];
        double[] mouseYPoint = new double[1];
        glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(),
                mouseXPoint, mouseYPoint);

        mouseX = mouseXPoint[0];
        mouseY = mouseYPoint[0];
        dragX = mouseX - this.lastMouseX;
        dragY = mouseY - this.lastMouseY;

        return this.root.mouseDragged(mouseX, mouseY, mouseKey, dragX, dragY);
    }
}
