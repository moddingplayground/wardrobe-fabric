package net.moddingplayground.wardrobe.impl.component;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.moddingplayground.wardrobe.api.component.WardrobeComponents;
import net.moddingplayground.wardrobe.api.component.WardrobePlayerComponent;

public final class WardrobeComponentsImpl implements WardrobeComponents, EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PLAYER, WardrobePlayerComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
