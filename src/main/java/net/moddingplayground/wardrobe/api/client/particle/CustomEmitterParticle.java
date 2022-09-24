package net.moddingplayground.wardrobe.api.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.EmitterParticle;

/**
 * An abstracted {@link EmitterParticle}.
 */
@Environment(EnvType.CLIENT)
public interface CustomEmitterParticle {
    void tick();
    boolean isAlive();
}
