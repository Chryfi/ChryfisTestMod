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
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

@OnlyIn(Dist.CLIENT)
public class UIScreen extends Screen {
    private final UIRootElement root;

    public UIScreen(Minecraft minecraft) {
        super(GameNarrator.NO_TITLE);
        this.minecraft = minecraft;
        WindowHandler.overwrite = true;
        this.root = new UIRootElement(new UIContext(this.minecraft.getWindow()));

        this.root.getContext().setDebug(true);

        this.root.height(GLUtils.getGLFWWindowSize()[1])
                .width(GLUtils.getGLFWWindowSize()[0])
                .backgroundColor(0.0F,0.0F,0.0F, 0.0F);

        UIPanelGrid rootGrid = new UIPanelGrid(this.root, new UIPanel(new UIElement(), new UIViewport()));
        rootGrid.width(1F).height(1F);
        rootGrid.subdivide(UIPanelGrid.DIRECTION.HORIZONTAL, 0.5F);
        rootGrid.getGrid0().ifPresent(e -> e.subdivide(UIPanelGrid.DIRECTION.VERTICAL, 0.25F));
        rootGrid.getGrid0().flatMap(UIPanelGrid::getGrid0).ifPresent(x -> x.subdivide(UIPanelGrid.DIRECTION.VERTICAL, 0.25F));
        rootGrid.getGrid1().ifPresent(e -> e.subdivide(UIPanelGrid.DIRECTION.VERTICAL, 0.25F));
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

        this.updateMouseContext();
        this.root.getContext().setPartialTicks(partialTicks);
        this.root.getContext().resetCursor();

        this.root.render(this.root.getContext());

        if (!this.root.getContext().cursorChanged()) {
            this.root.getContext().applyDefaultCursor();
        } else {
            this.root.getContext().applyCurrentCursor();
        }

        RenderSystem.restoreProjectionMatrix();
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, oldFramebufferID);
    }

    @Override
    public void tick() { }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        this.updateMouseContext();
        return this.root.isMouseOver(this.root.getContext());
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        this.updateMouseContext();
        this.root.mouseMoved(this.root.getContext());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseKey) {
        this.updateMouseContext(mouseX, mouseY, mouseKey);
        return this.root.mouseClicked(this.root.getContext());
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseKey) {
        this.updateMouseContext(mouseX, mouseY, mouseKey);
        return this.root.mouseReleased(this.root.getContext());
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseKey, double dragX, double dragY) {
        this.updateMouseContext(mouseX, mouseY, mouseKey);
        dragX = dragX / (double) this.minecraft.getWindow().getGuiScaledWidth() * (double) this.minecraft.getWindow().getScreenWidth();
        dragY = dragY / (double) this.minecraft.getWindow().getGuiScaledHeight() * (double) this.minecraft.getWindow().getScreenHeight();
        return this.root.mouseDragged(this.root.getContext(), dragX, dragY);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        this.root.getContext().setKeyboardKey(key);
        return this.root.keyPressed(this.root.getContext());
    }

    private void updateMouseContext(double mouseX, double mouseY, int mouseKey) {
        this.root.getContext().setMouseKey(mouseKey);
        this.updateMouseContext();
    }

    private void updateMouseContext() {
        /* we cant use the position passed down by minecraft, because in render method it's cast to int already
        while everywhere else it's double. This causes inconsistencies. */
        double[] mouseXPoint = new double[1];
        double[] mouseYPoint = new double[1];
        glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(),
                mouseXPoint, mouseYPoint);

        this.root.getContext().setMouse(mouseXPoint[0], mouseYPoint[0]);
    }
}
