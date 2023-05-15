package com.chryfi.test.mixins;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow {
    @Shadow
    private int framebufferWidth;
    @Shadow
    private int framebufferHeight;

    @Inject(method = "onFramebufferResize(JII)V",
            at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/Window;framebufferHeight:I", shift = At.Shift.AFTER))
    public void onFrameBufferResize(long p_85416_, int p_85417_, int p_85418_, CallbackInfo ci) {
        this.framebufferWidth = p_85417_ / 2;
        this.framebufferHeight = p_85418_ / 2;
        System.out.println("hello!");
    }

    @Inject(method = "onFramebufferResize(JII)V",
            at = @At(value = "HEAD"))
    public void test(long p_85416_, int p_85417_, int p_85418_, CallbackInfo ci) {
        System.out.println("HELLO!");
    }
}
