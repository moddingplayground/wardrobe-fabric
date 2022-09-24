package net.moddingplayground.wardrobe.api.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.moddingplayground.wardrobe.mixin.client.ParticleManagerMixin;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;

/**
 * @see ParticleManagerMixin
 */
@Environment(EnvType.CLIENT)
public interface WardrobeParticleManagerExtensions {
    default void addCustomEmitter(Entity entity, BiFunction<ClientWorld, Entity, CustomEmitterParticle> factory) {
        throw new AssertionError();
    }

    default void addCustomEmitter(Entity entity, TriFunction<ClientWorld, Entity, Integer, CustomEmitterParticle> factory, int maxAge) {
        throw new AssertionError();
    }
}
