package net.moddingplayground.wardrobe.api.client.render.cosmetic.manager;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.CosmeticRenderer;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.DustCosmeticRenderer;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.SunhatCosmeticRenderer;
import net.moddingplayground.wardrobe.api.cosmetic.Cosmetic;
import net.moddingplayground.wardrobe.api.cosmetic.Cosmetics;
import net.moddingplayground.wardrobe.api.registry.WardrobeRegistry;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class CosmeticRenderers {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<Cosmetic, CosmeticRendererFactory> FACTORIES = new HashMap<>();

    public static void register(Cosmetic cosmetic, CosmeticRendererFactory factory) {
        FACTORIES.put(cosmetic, factory);
    }

    public static Map<Cosmetic, CosmeticRenderer> reloadCosmeticRenderers(CosmeticRendererFactory.Context context) {
        ImmutableMap.Builder<Cosmetic, CosmeticRenderer> builder = ImmutableMap.builder();
        FACTORIES.forEach((cosmetic, factory) -> {
            try {
                builder.put(cosmetic, factory.create(context));
            } catch (Exception exception) {
                throw new IllegalArgumentException("Failed to create model for %s".formatted(WardrobeRegistry.COSMETIC.getId(cosmetic)), exception);
            }
        });
        return builder.build();
    }

    public static boolean isMissingRendererFactories() {
        for (Cosmetic cosmetic : WardrobeRegistry.COSMETIC) {
            if (FACTORIES.containsKey(cosmetic)) {
                continue;
            }

            LOGGER.warn("No renderer registered for {}", WardrobeRegistry.COSMETIC.getId(cosmetic));
            return true;
        }

        return false;
    }

    static {
        CosmeticRenderers.register(Cosmetics.SUNHAT, SunhatCosmeticRenderer::new);
        CosmeticRenderers.register(Cosmetics.DUST, DustCosmeticRenderer::new);
    }
}
