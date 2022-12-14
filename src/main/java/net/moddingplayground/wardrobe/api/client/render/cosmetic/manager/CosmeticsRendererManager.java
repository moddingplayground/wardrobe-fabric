package net.moddingplayground.wardrobe.api.client.render.cosmetic.manager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.CosmeticRenderer;
import net.moddingplayground.wardrobe.api.cosmetic.Cosmetic;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public class CosmeticsRendererManager implements SimpleSynchronousResourceReloadListener {
    public static final CosmeticsRendererManager INSTANCE = new CosmeticsRendererManager();

    private Map<Cosmetic, CosmeticRenderer> renderers = new HashMap<>();

    public CosmeticsRendererManager() {
    }

    public CosmeticRenderer getRenderer(Cosmetic cosmetic) {
        return this.renderers.get(cosmetic);
    }

    public CosmeticRenderer getRenderer(CosmeticInstance cosmetic) {
        return this.getRenderer(cosmetic.cosmetic());
    }

    public void run(PlayerEntity player, CosmeticSlot slot, BiConsumer<CosmeticInstance, CosmeticRenderer> action) {
        player.getCosmeticData().get(slot).ifPresent(instance -> {
            CosmeticRenderer renderer = this.getRenderer(instance);
            action.accept(instance, renderer);
        });
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        MinecraftClient client = MinecraftClient.getInstance();
        EntityRenderDispatcher entityRenderDispatcher = client.getEntityRenderDispatcher();
        CosmeticRendererFactory.Context context = new CosmeticRendererFactory.Context(
            entityRenderDispatcher, client.getItemRenderer(), client.getBlockRenderManager(),
            entityRenderDispatcher.getHeldItemRenderer(), resourceManager,
            client.getEntityModelLoader(), client.textRenderer
        );
        this.renderers = CosmeticRenderers.reloadCosmeticRenderers(context);
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(Wardrobe.MOD_ID, "cosmetics");
    }
}
