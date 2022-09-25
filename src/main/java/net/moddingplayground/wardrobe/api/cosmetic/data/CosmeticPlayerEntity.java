package net.moddingplayground.wardrobe.api.cosmetic.data;

public interface CosmeticPlayerEntity {
    default CosmeticData getCosmeticData() {
        throw new AssertionError();
    }
}
