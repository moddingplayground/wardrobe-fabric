package net.moddingplayground.wardrobe.api.client.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface WardrobeServerInfo {
    default void setSupported(boolean supported) {
        throw new AssertionError();
    }

    default boolean isSupported() {
        throw new AssertionError();
    }
}
