package net.moddingplayground.wardrobe.impl;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.moddingplayground.frame.api.util.InitializationLogger;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.cosmetic.Cosmetics;
import net.moddingplayground.wardrobe.api.registry.WardrobeRegistry;

public final class WardrobeImpl implements Wardrobe, ModInitializer {
    private final InitializationLogger initializer;

    public WardrobeImpl() {
        this.initializer = new InitializationLogger(LOGGER, MOD_NAME);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitialize() {
        this.initializer.start();
        Reflection.initialize(WardrobeRegistry.class, Cosmetics.class);
        this.initializer.finish();
    }
}
