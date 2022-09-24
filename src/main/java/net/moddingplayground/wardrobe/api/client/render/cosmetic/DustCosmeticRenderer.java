package net.moddingplayground.wardrobe.api.client.render.cosmetic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.util.math.Vec3f;
import net.moddingplayground.wardrobe.api.client.particle.CustomEmitterParticle;
import net.moddingplayground.wardrobe.api.client.particle.RisingEmitterParticle;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticRendererFactory;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;

@Environment(EnvType.CLIENT)
public class DustCosmeticRenderer extends FlairCosmeticRenderer {
    public DustCosmeticRenderer(CosmeticRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void onLevelUp(PlayerEntity player, int level, CosmeticInstance cosmetic) {
        if (level % 5 == 0) {
            this.particleManager.addCustomEmitter(player, this.createParticleEmitter(cosmetic, 40));
        }
    }

    @Override
    public void onAddCritParticles(PlayerEntity player, Entity entity, CosmeticInstance cosmetic) {
        this.particleManager.addCustomEmitter(entity, this.createParticleEmitter(cosmetic));
    }

    public DustColorTransitionParticleEffect createParticle(CosmeticInstance cosmetic) {
        return new DustColorTransitionParticleEffect(cosmetic.getRGB(), new Vec3f(1.0F, 1.0F, 1.0F), 1.5F);
    }

    public CustomEmitterParticle.Factory createParticleEmitter(CosmeticInstance cosmetic, int age) {
        return (world, entity) -> new RisingEmitterParticle(world, entity, this.createParticle(cosmetic), age);
    }

    public CustomEmitterParticle.Factory createParticleEmitter(CosmeticInstance cosmetic) {
        return (world, entity) -> new RisingEmitterParticle(world, entity, this.createParticle(cosmetic));
    }
}
