package com.chryfi.test.mixins;

import com.chryfi.test.Test;
import com.chryfi.test.client.rendering.WindowHandler;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    /**
     * unbindWrite() binds the framebuffer back to default 0.
     * @param p_91384_
     * @param ci
     */
    @Inject(method = "runTick(Z)V", at = @At(
        value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;unbindWrite()V", shift = At.Shift.AFTER
    ))
    public void afterUndbindWrite(boolean p_91384_, CallbackInfo ci) {
        WindowHandler.handleWindow();
    }

    /**
     * Get rid of drawing the Minecraft framebuffer, so we can draw it onto a texture ourselves.
     * @param instance
     * @param p_83939_
     * @param p_83940_
     */
    @Redirect(method = "runTick(Z)V", at = @At(
            value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;blitToScreen(II)V"
    ))
    public void redirectBlitToScreen(RenderTarget instance, int p_83939_, int p_83940_) { }
}
