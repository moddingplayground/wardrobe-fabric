package net.moddingplayground.wardrobe.api.cosmetic.data;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public interface CosmeticData {
    String
        SLOT_KEY = "Slot",
        COSMETICS_KEY = "Cosmetics",
        COSMETIC_KEY = "Cosmetic";

    boolean equip(CosmeticInstance cosmetic, @Nullable PlayerEntity player);

    default boolean equip(CosmeticInstance cosmetic) {
        return this.equip(cosmetic, null);
    }

    boolean clear(CosmeticSlot slot, @Nullable PlayerEntity player);

    default boolean clear(CosmeticSlot slot) {
        return this.clear(slot, null);
    }

    Optional<CosmeticInstance> get(CosmeticSlot slot);

    default void run(CosmeticSlot slot, Consumer<CosmeticInstance> action) {
        this.get(slot).ifPresent(action);
    }

    NbtCompound toNbt();

    void fromNbt(NbtCompound nbt);

    boolean affectsSlot(EquipmentSlot slot);
}
