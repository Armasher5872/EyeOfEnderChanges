package net.phazoganon.eyeofenderrework.mixin.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EyeOfEnder.class)
public abstract class EyeOfEnderMixin {
    @Shadow
    private boolean surviveAfterDeath;
    @Inject(method = "signalTo", at = @At(value = "TAIL"))
    private void signalTo(BlockPos pos, CallbackInfo ci) {
        this.surviveAfterDeath = true;
    }
}