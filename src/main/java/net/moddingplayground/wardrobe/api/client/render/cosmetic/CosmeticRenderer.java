package net.moddingplayground.wardrobe.api.client.render.cosmetic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface CosmeticRenderer {
    void render(CosmeticRendererContext context, CosmeticInstance cosmetic);

    default void setAngles(AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, PlayerEntityModel<PlayerEntity> contextModel) {
    }

    default void onLevelUp(PlayerEntity player, int level, CosmeticInstance cosmetic) {
    }

    default void onAddCritParticles(PlayerEntity player, Entity entity, CosmeticInstance cosmetic) {
    }

    default Identifier getTexture() {
        throw new AssertionError();
    }
}
