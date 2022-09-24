package net.moddingplayground.wardrobe.api.cosmetic;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import net.moddingplayground.wardrobe.api.registry.WardrobeRegistry;

public record CosmeticInstance(Cosmetic cosmetic, int color) {
    public static final String ID_KEY = "id";
    public static final String COLOR_KEY = "color";

    public CosmeticSlot getCosmeticSlot() {
        return this.cosmetic.getCosmeticSlot();
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString(ID_KEY, WardrobeRegistry.COSMETIC.getId(this.cosmetic).toString());
        nbt.putInt(COLOR_KEY, this.color);
        return nbt;
    }

    public static CosmeticInstance fromNbt(NbtCompound nbt) {
        String id = nbt.getString(ID_KEY);
        int color = nbt.getInt(COLOR_KEY);
        Cosmetic cosmetic = WardrobeRegistry.COSMETIC.get(Identifier.tryParse(id));
        return new CosmeticInstance(cosmetic, color);
    }

    public PacketByteBuf toPacket() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(this.toNbt());
        return buf;
    }

    public static CosmeticInstance fromPacket(PacketByteBuf buf) {
        return fromNbt(buf.readNbt());
    }
}
