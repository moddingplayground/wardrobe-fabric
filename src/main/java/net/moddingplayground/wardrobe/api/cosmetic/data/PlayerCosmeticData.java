package net.moddingplayground.wardrobe.api.cosmetic.data;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.moddingplayground.wardrobe.api.component.WardrobeComponents;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Optional;
import java.util.function.Consumer;

public class PlayerCosmeticData {
    private static final String
        SLOT_KEY = "Slot",
        COSMETICS_KEY = "Cosmetics",
        COSMETIC_KEY = "Cosmetic";

    private final EnumMap<CosmeticSlot, CosmeticInstance> cosmetics;

    public PlayerCosmeticData() {
        this.cosmetics = new EnumMap<>(CosmeticSlot.class);
    }

    public boolean equip(CosmeticInstance cosmetic, @Nullable PlayerEntity player) {
        boolean changed = !cosmetic.equals(this.cosmetics.put(cosmetic.getCosmeticSlot(), cosmetic));
        if (changed && player != null) {
            WardrobeComponents.PLAYER.sync(player);
        }
        return changed;
    }

    public boolean equip(CosmeticInstance cosmetic) {
        return this.equip(cosmetic, null);
    }

    public boolean clear(CosmeticSlot slot, @Nullable PlayerEntity player) {
        if (this.cosmetics.containsKey(slot)) {
            this.cosmetics.remove(slot);
            if (player != null) {
                WardrobeComponents.PLAYER.sync(player);
            }
            return true;
        }

        return false;
    }

    public boolean clear(CosmeticSlot slot) {
        return this.clear(slot, null);
    }

    public Optional<CosmeticInstance> get(CosmeticSlot slot) {
        return Optional.ofNullable(this.cosmetics.get(slot));
    }

    public void run(CosmeticSlot slot, Consumer<CosmeticInstance> action) {
        this.get(slot).ifPresent(action);
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        NbtList nbtCosmetics = new NbtList();
        this.cosmetics.forEach((slot, cosmetic) -> {
            NbtCompound nbtCosmetic = new NbtCompound();
            nbtCosmetic.putString(SLOT_KEY, slot.name());
            nbtCosmetic.put(COSMETIC_KEY, cosmetic.toNbt());
            nbtCosmetics.add(nbtCosmetic);
        });
        nbt.put(COSMETICS_KEY, nbtCosmetics);

        return nbt;
    }

    public void fromNbt(NbtCompound nbt) {
        this.cosmetics.clear();

        NbtList nbtCosmetics = nbt.getList(COSMETICS_KEY, NbtElement.COMPOUND_TYPE);
        for (NbtElement nbtCosmeticElement : nbtCosmetics) {
            if (nbtCosmeticElement instanceof NbtCompound nbtCosmetic) {
                String slotId = nbtCosmetic.getString(SLOT_KEY);
                try {
                    CosmeticSlot slot = CosmeticSlot.valueOf(slotId);
                    CosmeticInstance cosmeticInstance = CosmeticInstance.fromNbt(nbtCosmetic.getCompound(COSMETIC_KEY));
                    if (cosmeticInstance != null) {
                        this.cosmetics.put(slot, cosmeticInstance);
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }

    public boolean affectsSlot(EquipmentSlot slot) {
        return this.cosmetics.keySet().stream().anyMatch(slotx -> slotx.affectsSlot(slot));
    }
}
