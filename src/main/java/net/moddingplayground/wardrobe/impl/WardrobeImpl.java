package net.moddingplayground.wardrobe.impl;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.moddingplayground.frame.api.util.InitializationLogger;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.cosmetic.Cosmetics;
import net.moddingplayground.wardrobe.api.network.WardrobeNetworking;
import net.moddingplayground.wardrobe.api.registry.WardrobeRegistry;
import net.moddingplayground.wardrobe.impl.command.WardrobeCommand;

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

        WardrobeNetworking.registerReceivers();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            WardrobeCommand.register(dispatcher);
        });

        this.initializer.finish();
    }
}
