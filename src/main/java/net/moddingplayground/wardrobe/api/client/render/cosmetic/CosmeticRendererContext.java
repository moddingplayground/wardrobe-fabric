package net.moddingplayground.wardrobe.api.client.render.cosmetic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public record CosmeticRendererContext(
    AbstractClientPlayerEntity player, float yaw, float tickDelta,
    MatrixStack matrices, VertexConsumerProvider vertices, int light,
    PlayerEntityRenderer contextRenderer) {
}
