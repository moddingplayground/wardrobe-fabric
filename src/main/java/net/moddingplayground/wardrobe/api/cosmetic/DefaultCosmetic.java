package net.moddingplayground.wardrobe.api.cosmetic;

import net.minecraft.text.Text;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import net.moddingplayground.wardrobe.api.registry.WardrobeRegistry;

public class DefaultCosmetic implements Cosmetic {
    private final CosmeticSlot slot;
    private Text displayText = null;

    public DefaultCosmetic(CosmeticSlot slot) {
        this.slot = slot;
    }

    @Override
    public CosmeticSlot getCosmeticSlot() {
        return this.slot;
    }

    @Override
    public Text getOrCreateDisplayText() {
        if (this.displayText == null) {
            this.displayText = Text.translatable("%s.cosmetic.%s".formatted(Wardrobe.MOD_ID, WardrobeRegistry.COSMETIC.getId(this)));
        }

        return this.displayText;
    }

    @Override
    public String toString() {
        return WardrobeRegistry.COSMETIC.getId(this).toString();
    }
}
