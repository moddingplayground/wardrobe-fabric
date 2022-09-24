package net.moddingplayground.wardrobe.api.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.EmitterParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

/**
 * An abstracted {@link EmitterParticle}.
 */
@Environment(EnvType.CLIENT)
public interface CustomEmitterParticle {
    void tick();
    boolean isAlive();

    @FunctionalInterface
    interface Factory {
        CustomEmitterParticle create(ClientWorld world, Entity entity);
    }
}
