package net.moddingplayground.wardrobe.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyArg(method = "onResourcePackSend", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;execute(Ljava/lang/Runnable;)V"), index = 0)
    private Runnable replaceResourcePackSendReply(Runnable runnable) {
        ServerInfo server = this.client.getCurrentServerEntry();
        if (server.getResourcePackPolicy() == ServerInfo.ResourcePackPolicy.DISABLED) {
            return () -> {
                this.client.setScreen(null);
                this.client.getNetworkHandler().getConnection().send(new ResourcePackStatusC2SPacket(ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
            };
        }

        return runnable;
    }
}
