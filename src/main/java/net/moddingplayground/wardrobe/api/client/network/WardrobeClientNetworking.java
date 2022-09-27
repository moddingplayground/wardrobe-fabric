package net.moddingplayground.wardrobe.api.client.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.client.cosmetic.LocalServerCosmeticsManager;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticsRendererManager;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;
import net.moddingplayground.wardrobe.api.network.WardrobeNetworking;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public interface WardrobeClientNetworking extends WardrobeNetworking {
    static void registerReceivers() {
        ClientPlayConnectionEvents.JOIN.register(WardrobeClientNetworking::onPlayInitialize);
        ClientLoginNetworking.registerGlobalReceiver(SERVER_INIT_PACKET_ID, WardrobeClientNetworking::onServerInitialize);
        ClientPlayNetworking.registerGlobalReceiver(LEVEL_UP_PACKET_ID, WardrobeClientNetworking::onLevelUp);
        ClientPlayNetworking.registerGlobalReceiver(CRIT_PACKET_ID, WardrobeClientNetworking::onCrit);
    }

    static boolean isUnsupported(MinecraftClient client) {
        if (client.isIntegratedServerRunning()) {
            return false;
        }

        ServerInfo server = client.getCurrentServerEntry();
        return server == null || !server.isSupported();
    }

    static boolean isUnsupported(FabricClientCommandSource source) {
        return isUnsupported(source.getClient());
    }

    /**
     * Invoked when the client joins a server.
     */
    static void onPlayInitialize(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        ServerInfo server = client.getCurrentServerEntry();
        if (server != null && !server.isSupported()) {
            LOGGER.info("The current server does not appear to support {}. Preparing client cosmetics functionality.", Wardrobe.MOD_NAME);
            LocalServerCosmeticsManager.INSTANCE.loadFile();
        }
    }

    /**
     * Received by the client on server login.
     */
    static CompletableFuture<PacketByteBuf> onServerInitialize(MinecraftClient client, ClientLoginNetworkHandler handler, PacketByteBuf buf, Consumer<GenericFutureListener<? extends Future<? super Void>>> consumer) {
        if (!client.isIntegratedServerRunning()) {
            LOGGER.info("The current server supports {}! Furloughing client cosmetics functionality.", Wardrobe.MOD_NAME);

            ServerInfo server = client.getCurrentServerEntry();
            server.setSupported(true);
        }

        return CompletableFuture.completedFuture(PacketByteBufs.empty());
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
