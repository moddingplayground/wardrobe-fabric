package net.moddingplayground.wardrobe.api.client.model.cosmetic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public abstract class CosmeticModel extends CompositeEntityModel<AbstractClientPlayerEntity> {
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.getParts().forEach(part -> {
            if (part.isTinted()) {
                part.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            } else {
                part.render(matrices, vertices, light, overlay);
            }
        });
    }

    public void setAngles(AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, PlayerEntityModel<PlayerEntity> contextModel) {
    }

    @Override
    public void setAngles(AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        throw new AssertionError();
    }
}
