package net.moddingplayground.wardrobe.api.client.render.cosmetic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticRendererFactory;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;

@Environment(EnvType.CLIENT)
public class CritCosmeticRenderer extends FlairCosmeticRenderer {
    public CritCosmeticRenderer(CosmeticRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void onLevelUp(CosmeticInstance cosmetic, PlayerEntity player) {
        if (player.experienceLevel % 5 == 0) {
            this.particleManager.addEmitter(player, ParticleTypes.CRIT, 30);
        }
    }
}
