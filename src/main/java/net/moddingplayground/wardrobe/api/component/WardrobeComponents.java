package net.moddingplayground.wardrobe.api.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.Wardrobe;

public interface WardrobeComponents {
    ComponentKey<WardrobePlayerComponent> PLAYER = register("player", WardrobePlayerComponent.class);

    private static <C extends Component> ComponentKey<C> register(String id, Class<C> clazz) {
        return ComponentRegistry.getOrCreate(new Identifier(Wardrobe.MOD_ID, id), clazz);
    }
}
