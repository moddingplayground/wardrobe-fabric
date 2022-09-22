package net.moddingplayground.wardrobe.api.cosmetic.data;

import net.minecraft.entity.EquipmentSlot;

import java.util.Arrays;
import java.util.List;

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

    CosmeticSlot(EquipmentSlot... slots) {
        this.slots = Arrays.asList(slots);
    }

    public boolean affectsSlot(EquipmentSlot slot) {
        return this.slots.contains(slot);
    }
}
