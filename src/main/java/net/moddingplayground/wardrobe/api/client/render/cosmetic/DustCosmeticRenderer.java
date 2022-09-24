package net.moddingplayground.wardrobe.api.client.render.cosmetic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.util.math.Vec3f;
import net.moddingplayground.wardrobe.api.client.particle.SurroundingEmitterParticle;
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
            Vec3f color = cosmetic.getRGB();
            Vec3f toColor = new Vec3f(1.0F, 1.0F, 1.0F);
            this.particleManager.addCustomEmitter(player, (world, entity, maxAge) -> new SurroundingEmitterParticle(world, entity, new DustColorTransitionParticleEffect(color, toColor, 1.5F), maxAge), 40);
        }
    }
}
