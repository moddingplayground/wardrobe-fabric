package net.moddingplayground.wardrobe.api.client.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticsRendererManager;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;
import net.moddingplayground.wardrobe.api.network.WardrobeNetworking;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public interface WardrobeClientNetworking extends WardrobeNetworking {
    static void registerReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(LEVEL_UP_PACKET_ID, WardrobeClientNetworking::onLevelUp);
        ClientPlayNetworking.registerGlobalReceiver(CRIT_PACKET_ID, WardrobeClientNetworking::onCrit);
    }

    /**
     * Received by the client player on level-up.
     */
    static void onLevelUp(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        CosmeticInstance cosmetic = CosmeticInstance.fromPacket(buf);
        UUID uuid = buf.readUuid();
        int level = buf.readInt();
        client.execute(() -> {
            PlayerEntity player = client.world.getPlayerByUuid(uuid);
            CosmeticsRendererManager.INSTANCE.getRenderer(cosmetic).onLevelUp(player, level, cosmetic);
        });
    }

    /**
     * Received by the client player on entity crit.
     */
    static void onCrit(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        CosmeticInstance cosmetic = CosmeticInstance.fromPacket(buf);
        UUID uuid = buf.readUuid();
        int entityId = buf.readInt();
        client.execute(() -> {
            PlayerEntity player = client.world.getPlayerByUuid(uuid);
            Entity entity = client.world.getEntityById(entityId);
            CosmeticsRendererManager.INSTANCE.getRenderer(cosmetic).onAddCritParticles(player, entity, cosmetic);
        });
    }
}
