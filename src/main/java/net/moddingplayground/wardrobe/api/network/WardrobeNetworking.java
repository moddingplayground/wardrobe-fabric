package net.moddingplayground.wardrobe.api.network;

import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.Wardrobe;

public interface WardrobeNetworking {
    Identifier
        LEVEL_UP_PACKET_ID = new Identifier(Wardrobe.MOD_ID, "action/level_up"),
        CRIT_PACKET_ID = new Identifier(Wardrobe.MOD_ID, "action/crit");

    static void registerReceivers() {
    }
}
