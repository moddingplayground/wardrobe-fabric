package net.moddingplayground.wardrobe.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticPlayerEntity;
import net.moddingplayground.wardrobe.api.cosmetic.data.PlayerCosmeticData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements CosmeticPlayerEntity {
    @Unique
    private final PlayerCosmeticData cosmeticData = new PlayerCosmeticData();

    @Unique
    @Override
    public PlayerCosmeticData getCosmeticData() {
        return this.cosmeticData;
    }
}
