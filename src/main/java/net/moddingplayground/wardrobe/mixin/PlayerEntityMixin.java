package net.moddingplayground.wardrobe.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.moddingplayground.wardrobe.api.component.WardrobeComponents;
import net.moddingplayground.wardrobe.api.component.WardrobePlayerComponent;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticPlayerEntity;
import net.moddingplayground.wardrobe.api.cosmetic.data.PlayerCosmeticData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements CosmeticPlayerEntity {
    @Unique
    @Override
    public PlayerCosmeticData getCosmeticData() {
        WardrobePlayerComponent component = WardrobeComponents.PLAYER.get(this);
        return component.getCosmeticData();
    }
}
