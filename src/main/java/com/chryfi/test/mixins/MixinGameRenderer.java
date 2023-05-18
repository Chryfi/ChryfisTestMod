package com.chryfi.test.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * DEACTIVATED
 */
@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow
    private float zoom = 1.0F;
    @Shadow
    private float zoomX;
    @Shadow
    private float zoomY;
    @Shadow
    final Minecraft minecraft = Minecraft.getInstance();


    /**
     * @author
     * @reason
     */
    @Overwrite
    public Matrix4f getProjectionMatrix(double p_254507_) {
        PoseStack posestack = new PoseStack();
        posestack.last().pose().identity();
        if (this.zoom != 1.0F) {
            posestack.translate(this.zoomX, -this.zoomY, 0.0F);
            posestack.scale(this.zoom, this.zoom, 1.0F);
        }

        /* adjusted perspective matrix */
        posestack.last().pose().mul((new Matrix4f()).setPerspective((float)(p_254507_ * (double)((float)Math.PI / 180F)), (float)854 / (float)480, 0.05F, this.getDepthFar()));
        return posestack.last().pose();
    }

    @Inject(method = "render(FJZ)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;viewport(IIII)V", shift = At.Shift.AFTER))
    public void render(float p_109094_, long p_109095_, boolean p_109096_, CallbackInfo ci) {
        int x = 200;
        int y = 200;
        int width = this.minecraft.getWindow().getWidth() + x;
        int height = this.minecraft.getWindow().getHeight() + y;
        RenderSystem.viewport(x, y, width, height);
    }

    @Shadow
    public float getDepthFar() {
        throw new IllegalStateException("Mixin failed to shadow getItem()");
    }
}
