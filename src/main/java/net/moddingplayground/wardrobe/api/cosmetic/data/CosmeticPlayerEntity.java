package net.moddingplayground.wardrobe.api.cosmetic.data;

public interface CosmeticPlayerEntity {
    default PlayerCosmeticData getCosmeticData() {
        throw new AssertionError();
    }
}
