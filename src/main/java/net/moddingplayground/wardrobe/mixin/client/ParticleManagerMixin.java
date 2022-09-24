package net.moddingplayground.wardrobe.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.moddingplayground.wardrobe.api.client.particle.CustomEmitterParticle;
import net.moddingplayground.wardrobe.api.client.particle.WardrobeParticleManagerExtensions;
import org.apache.commons.lang3.function.TriFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public class ParticleManagerMixin implements WardrobeParticleManagerExtensions {
    @Shadow protected ClientWorld world;

    @Unique
    private final Queue<CustomEmitterParticle> newCustomEmitterParticles = new ArrayDeque<>();

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Queue;isEmpty()Z",
            ordinal = 1,
            shift = At.Shift.BEFORE
        )
    )
    private void onTick(CallbackInfo ci) {
        if (!this.newCustomEmitterParticles.isEmpty()) {
            ArrayList<CustomEmitterParticle> removed = new ArrayList<>();
            for (CustomEmitterParticle particle : this.newCustomEmitterParticles) {
                particle.tick();
                if (!particle.isAlive()) {
                    removed.add(particle);
                }
            }
            this.newCustomEmitterParticles.removeAll(removed);
        }
    }

    @Inject(method = "setWorld", at = @At("TAIL"))
    private void onSetWorld(ClientWorld world, CallbackInfo ci) {
        this.newCustomEmitterParticles.clear();
    }

    @Unique
    @Override
    public void addCustomEmitter(Entity entity, BiFunction<ClientWorld, Entity, CustomEmitterParticle> factory) {
        this.newCustomEmitterParticles.add(factory.apply(this.world, entity));
    }

    @Unique
    @Override
    public void addCustomEmitter(Entity entity, TriFunction<ClientWorld, Entity, Integer, CustomEmitterParticle> factory, int maxAge) {
        this.addCustomEmitter(entity, (world, entityx) -> factory.apply(world, entityx, maxAge));
    }
}
