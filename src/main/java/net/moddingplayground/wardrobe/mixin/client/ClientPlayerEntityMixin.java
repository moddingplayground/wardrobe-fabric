package net.moddingplayground.wardrobe.mixin.client;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.moddingplayground.wardrobe.api.client.cosmetic.LocalServerCosmeticsManager;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Client overrides for cosmetic data management.
 */
@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    private ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Override
    public CosmeticData getCosmeticData() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.isIntegratedServerRunning()) return super.getCosmeticData();

        ServerInfo server = client.getCurrentServerEntry();
        if (server != null && !server.isSupported()) {
            return LocalServerCosmeticsManager.INSTANCE.getOrCreateCosmeticData(server);
        }

        return super.getCosmeticData();
    }
}
