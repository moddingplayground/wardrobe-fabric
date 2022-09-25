package net.moddingplayground.wardrobe.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin<T extends LivingEntity, A extends BipedEntityModel<T>> {
    @Inject(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;getContextModel()Lnet/minecraft/client/render/entity/model/EntityModel;",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void onRenderArmor(MatrixStack matrices, VertexConsumerProvider vertices, T entity, EquipmentSlot slot, int light, A model, CallbackInfo ci) {
        if (entity instanceof AbstractClientPlayerEntity player) {
            CosmeticData data = player.getCosmeticData();
            if (data.affectsSlot(slot)) {
                ci.cancel();
            }
        }
    }
}
