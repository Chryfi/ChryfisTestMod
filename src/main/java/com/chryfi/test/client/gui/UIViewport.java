package com.chryfi.test.client.gui;

import com.chryfi.test.client.rendering.IMixinWindow;
import com.chryfi.test.client.rendering.WindowHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Math;

@OnlyIn(Dist.CLIENT)
public class UIViewport extends UIElement {
    @Override
    public void onAreasCalculated() {
        WindowHandler.x = this.contentArea.getX();
        WindowHandler.y = this.contentArea.getY();
        WindowHandler.width = Math.clamp(1, 8000, this.contentArea.getWidth());
        WindowHandler.height = Math.clamp(1, 8000, this.contentArea.getHeight());
        WindowHandler.resize = true;
        WindowHandler.overwrite = true;

        WindowHandler.queueResize(WindowHandler.width, WindowHandler.height);
    }

    @Override
    protected void _onClose() {
        WindowHandler.overwrite = false;
        WindowHandler.revertToDefault();
    }

    @Override
    public void render(UIContext context) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableTexture();
        RenderSystem.setShaderTexture(0, Minecraft.getInstance().getMainRenderTarget().getColorTextureId());

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        this.buildUVQuad(bufferBuilder, this.contentArea.getX(), this.contentArea.getY(),
                this.contentArea.getX() + this.contentArea.getWidth(), this.contentArea.getY() + this.contentArea.getHeight());

        BufferUploader.drawWithShader(bufferBuilder.end());

        if (context.isDebug()) {
            this.drawMargins();
            this.drawPaddings();
        }

        for (UIElement child : this.getChildren()) {
            child.render(context);
        }
    }

    private void buildUVQuad(BufferBuilder bufferBuilder, int x0, int y0, int x1, int y1) {
        bufferBuilder.vertex(x1, y0, 0).uv(1,1).endVertex();
        bufferBuilder.vertex(x0, y0, 0).uv(0,1).endVertex();
        bufferBuilder.vertex(x0, y1, 0).uv(0,0).endVertex();
        bufferBuilder.vertex(x1, y1, 0).uv(1,0).endVertex();
    }
}
