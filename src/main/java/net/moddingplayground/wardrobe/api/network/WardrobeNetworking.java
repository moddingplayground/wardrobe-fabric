package net.moddingplayground.wardrobe.api.network;

import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.Wardrobe;

public interface WardrobeNetworking {
    Identifier
        LEVELUP_PACKET_ID = new Identifier(Wardrobe.MOD_ID, "levelup");

    static void registerReceivers() {
    }
}
