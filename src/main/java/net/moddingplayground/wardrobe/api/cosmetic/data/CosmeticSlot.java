package net.moddingplayground.wardrobe.api.cosmetic.data;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import net.moddingplayground.wardrobe.api.Wardrobe;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum CosmeticSlot {
    /**
     * Cosmetics that go on the player's head.
     */
    HEAD(EquipmentSlot.HEAD),

    /**
     * Cosmetics that cover the player's chest and legs.
     */
    ROBE(EquipmentSlot.CHEST, EquipmentSlot.LEGS),

    /**
     * Cosmetics that go on the player's feet.
     */
    FEET(EquipmentSlot.FEET),

    /**
     * Cosmetics that display under certain conditions (killing a mob, opening a loot chest, etc).
     */
    FLAIR;

    private final List<EquipmentSlot> slots;
    private Text displayText;

    CosmeticSlot(EquipmentSlot... slots) {
        this.slots = Arrays.asList(slots);
    }

    public boolean affectsSlot(EquipmentSlot slot) {
        return this.slots.contains(slot);
    }

    public Text getDisplayText() {
        if (this.displayText == null) {
            this.displayText = Text.translatable("%s.cosmetic_slot.%s".formatted(Wardrobe.MOD_ID, this.name().toLowerCase(Locale.ROOT)));
        }

        return this.displayText;
    }
}
