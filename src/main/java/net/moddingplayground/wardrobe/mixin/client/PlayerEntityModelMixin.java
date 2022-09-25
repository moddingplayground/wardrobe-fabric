package net.moddingplayground.wardrobe.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.CosmeticRenderer;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticsRendererManager;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticData;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> {
    @SuppressWarnings("unchecked")
    @Inject(
        method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
        at = @At("TAIL")
    )
    private void onRender(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof AbstractClientPlayerEntity player) {
            CosmeticData data = player.getCosmeticData();
            for (CosmeticSlot slot : CosmeticSlot.values()) {
                data.get(slot).ifPresent(cosmetic -> {
                    CosmeticRenderer renderer = CosmeticsRendererManager.INSTANCE.getRenderer(cosmetic);
                    renderer.setAngles(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch, (PlayerEntityModel<PlayerEntity>) (Object) this);
                });
            }
        }
    }
}
