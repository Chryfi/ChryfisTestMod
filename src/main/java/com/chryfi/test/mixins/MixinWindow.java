package com.chryfi.test.mixins;

import com.chryfi.test.Test;
import com.chryfi.test.client.rendering.IMixinWindow;
import com.chryfi.test.client.rendering.WindowHandler;
import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public abstract class MixinWindow implements IMixinWindow {
    @Shadow
    private int framebufferWidth;
    @Shadow
    private int framebufferHeight;
    @Shadow
    private long window;

    @Inject(method = "onFramebufferResize(JII)V",
            at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/Window;framebufferHeight:I", shift = At.Shift.AFTER))
    public void onFrameBufferResize(long p_85416_, int p_85417_, int p_85418_, CallbackInfo ci) {
        this.framebufferWidth = WindowHandler.frameBufferResize(p_85417_, p_85418_)[0];
        this.framebufferHeight = WindowHandler.frameBufferResize(p_85417_, p_85418_)[1];
    }

    @Override
    public void resize(int width, int height) {
        //this.onResize(this.window, width, height);
        this.onFramebufferResize(this.window, width, height);
    }

    @Shadow
    private void onFramebufferResize(long p_85416_, int p_85417_, int p_85418_) {}

    @Shadow
    private void onResize(long p_85428_, int p_85429_, int p_85430_) {}
}
