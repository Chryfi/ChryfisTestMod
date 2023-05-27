package com.chryfi.test.client.gui;

import com.chryfi.test.client.rendering.IMixinWindow;
import com.chryfi.test.client.rendering.WindowHandler;
import com.chryfi.test.utils.Color;
import com.chryfi.test.utils.rendering.GLUtils;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Math;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class UIViewport extends UIElement {
    @Override
    public void onAreasCalculated() {
        WindowHandler.x = this.innerArea.getX();
        WindowHandler.y = this.innerArea.getY();
        WindowHandler.width = this.innerArea.getWidth();
        WindowHandler.height = this.innerArea.getHeight();
        WindowHandler.resize = true;
        WindowHandler.overwrite = true;

        Object window = Minecraft.getInstance().getWindow();
        ((IMixinWindow) window).resize(Math.clamp(1, Integer.MAX_VALUE, this.innerArea.getWidth()),
                Math.clamp(1, Integer.MAX_VALUE, this.innerArea.getHeight()));
    }

    @Override
    protected void _onClose() {
        WindowHandler.overwrite = false;
        WindowHandler.revertToDefault();
    }

    @Override
    public void render(GuiContext context) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableTexture();
        RenderSystem.setShaderTexture(0, Minecraft.getInstance().getMainRenderTarget().getColorTextureId());

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        this.buildUVQuad(bufferBuilder, this.innerArea.getX(), this.innerArea.getY(),
                this.innerArea.getX() + this.innerArea.getWidth(), this.innerArea.getY() + this.innerArea.getHeight());

        BufferUploader.drawWithShader(bufferBuilder.end());

        for (UIElement child : this.getChildren()) {
            child.render(context);
        }
    }

    private void buildUVQuad(BufferBuilder bufferBuilder, int x0, int y0, int x1, int y1) {
        bufferBuilder.vertex(x1, y0, -500).uv(1,1).endVertex();
        bufferBuilder.vertex(x0, y0, -500).uv(0,1).endVertex();
        bufferBuilder.vertex(x0, y1, -500).uv(0,0).endVertex();
        bufferBuilder.vertex(x1, y1, -500).uv(1,0).endVertex();
    }
}
