package net.moddingplayground.wardrobe.api.cosmetic;

import net.minecraft.text.Text;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;

public interface Cosmetic {
    CosmeticSlot getCosmeticSlot();
    Text getOrCreateDisplayText();
}
