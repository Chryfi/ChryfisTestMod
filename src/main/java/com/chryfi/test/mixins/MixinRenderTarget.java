package com.chryfi.test.mixins;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * DEACTIVATED
 */
@Mixin(RenderTarget.class)
public abstract class MixinRenderTarget {
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    public int viewWidth;
    @Shadow
    public int viewHeight;

    @Inject(method = "createBuffers(IIZ)V",
            at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;height:I",
                    opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER))
    public void onCreateBuffers(int p_83951_, int p_83952_, boolean p_83953_, CallbackInfo ci) {
        int x = 854;
        int y = 480;
        this.viewWidth = x;
        this.viewHeight = y;
        this.width = x;
        this.height = y;
    }

    @Inject(method = "_bindWrite(Z)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_viewport(IIII)V", ordinal = 0, shift = At.Shift.AFTER))
    public void on_bindWrite(boolean p_83962_, CallbackInfo ci) {
        GlStateManager._viewport(0, 0, 854, 480);
    }
}
