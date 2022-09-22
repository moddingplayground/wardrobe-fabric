package net.moddingplayground.wardrobe.api.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.cosmetic.Cosmetic;

public interface WardrobeRegistry {
    DefaultedRegistry<Cosmetic> COSMETIC = register(Cosmetic.class, "cosmetic", new Identifier(Wardrobe.MOD_ID, "sunhat"));

    private static <T> DefaultedRegistry<T> register(Class<T> clazz, String id, Identifier defaultId) {
        return FabricRegistryBuilder.createDefaulted(clazz, new Identifier(Wardrobe.MOD_ID, id), defaultId).buildAndRegister();
    }
}
