package net.moddingplayground.wardrobe.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.CosmeticRenderer;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.CosmeticRendererContext;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticsRendererManager;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import net.moddingplayground.wardrobe.api.cosmetic.data.PlayerCosmeticData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @Inject(
        method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V",
            shift = At.Shift.BEFORE
        )
    )
    private void onRender(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light, CallbackInfo ci) {
        if (entity instanceof AbstractClientPlayerEntity player) {
            PlayerCosmeticData data = player.getCosmeticData();
            CosmeticRendererContext context = new CosmeticRendererContext(player, yaw, tickDelta, matrices, vertices, light, (PlayerEntityRenderer) (Object) this);
            for (CosmeticSlot slot : CosmeticSlot.values()) {
                data.get(slot).ifPresent(cosmetic -> {
                    CosmeticRenderer renderer = CosmeticsRendererManager.INSTANCE.getRenderer(cosmetic.cosmetic());
                    renderer.render(context, cosmetic);
                });
            }
        }
    }
}
