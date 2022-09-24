package net.moddingplayground.wardrobe.api.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.moddingplayground.wardrobe.api.cosmetic.data.PlayerCosmeticData;

public class WardrobePlayerComponent implements Component, AutoSyncedComponent {
    private static final String COSMETIC_DATA_KEY = "CosmeticData";

    private final PlayerEntity player;

    /**
     * A player's stored cosmetic data.
     */
    private final PlayerCosmeticData cosmeticData = new PlayerCosmeticData();

    public WardrobePlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public PlayerCosmeticData getCosmeticData() {
        return this.cosmeticData;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.put(COSMETIC_DATA_KEY, this.cosmeticData.toNbt());
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        this.cosmeticData.fromNbt(nbt.getCompound(COSMETIC_DATA_KEY));
    }
}
