package net.moddingplayground.wardrobe.api.network;

import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking.LoginSynchronizer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.Wardrobe;
import org.slf4j.Logger;

public interface WardrobeNetworking {
    Logger LOGGER = LogUtils.getLogger();

    Identifier
        SERVER_INIT_PACKET_ID = new Identifier(Wardrobe.MOD_ID, "server_initialize"),
        LEVEL_UP_PACKET_ID = new Identifier(Wardrobe.MOD_ID, "action/level_up"),
        CRIT_PACKET_ID = new Identifier(Wardrobe.MOD_ID, "action/crit");

    static void registerReceivers() {
        ServerLoginConnectionEvents.QUERY_START.register(WardrobeNetworking::onQueryStart);
        ServerLoginNetworking.registerGlobalReceiver(SERVER_INIT_PACKET_ID, WardrobeNetworking::onServerInitialize);
    }

    static void onQueryStart(ServerLoginNetworkHandler handler, MinecraftServer server, PacketSender sender, LoginSynchronizer synchronizer) {
        sender.sendPacket(SERVER_INIT_PACKET_ID, PacketByteBufs.empty());
    }

    /**
     * Received on client login when {@link #SERVER_INIT_PACKET_ID} is acknowledged.
     */
    static void onServerInitialize(MinecraftServer server, ServerLoginNetworkHandler handler, boolean understood, PacketByteBuf buf, LoginSynchronizer synchronizer, PacketSender sender) {
    }
}
