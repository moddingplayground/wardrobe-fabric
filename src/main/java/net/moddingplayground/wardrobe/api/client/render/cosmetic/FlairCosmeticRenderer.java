package net.moddingplayground.wardrobe.api.client.render.cosmetic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleManager;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticRendererFactory;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;

@Environment(EnvType.CLIENT)
public class FlairCosmeticRenderer implements CosmeticRenderer {
    protected final MinecraftClient client;
    protected final ParticleManager particleManager;

    public FlairCosmeticRenderer(CosmeticRendererFactory.Context context) {
        this.client = MinecraftClient.getInstance();
        this.particleManager = this.client.particleManager;
    }

    @Override
    public void render(CosmeticRendererContext context, CosmeticInstance cosmetic) {
    }
}
