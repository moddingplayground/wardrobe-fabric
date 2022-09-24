package net.moddingplayground.wardrobe.api.network;

import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.Wardrobe;

public interface WardrobeNetworking {
    Identifier LEVEL_UP_PACKET_ID = new Identifier(Wardrobe.MOD_ID, "level_up");

    static void registerReceivers() {
    }
}
