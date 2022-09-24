package net.moddingplayground.wardrobe.api.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.moddingplayground.wardrobe.mixin.client.ParticleManagerMixin;

/**
 * @see ParticleManagerMixin
 */
@Environment(EnvType.CLIENT)
public interface WardrobeParticleManagerExtensions {
    default void addCustomEmitter(Entity entity, CustomEmitterParticle.Factory factory) {
        throw new AssertionError();
    }
}
