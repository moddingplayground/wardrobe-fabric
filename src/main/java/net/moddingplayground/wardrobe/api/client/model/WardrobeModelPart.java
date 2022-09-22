package net.moddingplayground.wardrobe.api.client.model;

import net.minecraft.client.model.ModelPart;

public interface WardrobeModelPart {
    default ModelPart setTinted() {
        throw new AssertionError();
    }

    default boolean isTinted() {
        return false;
    }
}
