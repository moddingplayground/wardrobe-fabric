package net.moddingplayground.wardrobe.api.cosmetic.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.EquipmentSlot;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class PlayerCosmeticData {
    private final EnumMap<CosmeticSlot, CosmeticInstance> cosmetics;

    public PlayerCosmeticData() {
        this.cosmetics = new EnumMap<>(CosmeticSlot.class);
    }

    public boolean equip(CosmeticInstance cosmetic) {
        return !cosmetic.equals(this.cosmetics.put(cosmetic.getCosmeticSlot(), cosmetic));
    }

    public boolean clear(CosmeticSlot slot) {
        if (this.cosmetics.containsKey(slot)) {
            this.cosmetics.remove(slot);
            return true;
        }

        return false;
    }

    public Optional<CosmeticInstance> get(CosmeticSlot slot) {
        return Optional.ofNullable(this.cosmetics.get(slot));
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        this.cosmetics.forEach((slot, cosmetic) -> json.add(slot.name(), cosmetic.toJson()));
        return json;
    }

    public void fromJson(JsonObject json) {
        this.cosmetics.clear();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String slotId = entry.getKey();
            JsonElement data = entry.getValue();
            if (data instanceof JsonObject jsonObject) {
                try {
                    CosmeticSlot slot = CosmeticSlot.valueOf(slotId);
                    CosmeticInstance cosmeticInstance = CosmeticInstance.fromJson(jsonObject);
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
