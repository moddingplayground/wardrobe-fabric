package net.moddingplayground.wardrobe.api.cosmetic;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import net.moddingplayground.wardrobe.api.registry.WardrobeRegistry;
import org.jetbrains.annotations.Nullable;

public record CosmeticInstance(Cosmetic cosmetic, int color) {
    public static final String ID_KEY = "id";
    public static final String COLOR_KEY = "color";

    public CosmeticSlot getCosmeticSlot() {
        return this.cosmetic.getCosmeticSlot();
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty(ID_KEY, WardrobeRegistry.COSMETIC.getId(this.cosmetic).toString());
        json.addProperty(COLOR_KEY, this.color);
        return json;
    }

    @Nullable
    public static CosmeticInstance fromJson(JsonObject json) {
        String id = json.getAsJsonPrimitive(ID_KEY).getAsString();
        int color = json.getAsJsonPrimitive(COLOR_KEY).getAsInt();
        Cosmetic cosmetic = WardrobeRegistry.COSMETIC.get(Identifier.tryParse(id));
        return cosmetic == null ? null : new CosmeticInstance(cosmetic, color);
    }
}
