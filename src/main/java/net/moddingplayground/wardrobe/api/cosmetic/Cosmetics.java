package net.moddingplayground.wardrobe.api.cosmetic;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import net.moddingplayground.wardrobe.api.registry.WardrobeRegistry;

public interface Cosmetics {
    Cosmetic SUNHAT = register("sunhat", new DefaultCosmetic(CosmeticSlot.HEAD));

    private static <T extends Cosmetic> T register(String id, T cosmetic) {
        return Registry.register(WardrobeRegistry.COSMETIC, new Identifier(Wardrobe.MOD_ID, id), cosmetic);
    }
}
