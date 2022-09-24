package net.moddingplayground.wardrobe.impl.client;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.moddingplayground.frame.api.util.InitializationLogger;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.client.model.WardrobeEntityModelLayers;
import net.moddingplayground.wardrobe.api.client.network.WardrobeClientNetworking;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticsRendererManager;

@Environment(EnvType.CLIENT)
public final class WardrobeClientImpl implements Wardrobe, ClientModInitializer {
    private final InitializationLogger initializer;

    public WardrobeClientImpl() {
        this.initializer = new InitializationLogger(LOGGER, MOD_NAME, EnvType.CLIENT);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        this.initializer.start();

        Reflection.initialize(WardrobeEntityModelLayers.class);

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(CosmeticsRendererManager.INSTANCE);

        WardrobeClientNetworking.registerReceivers();

        this.initializer.finish();
    }
}
