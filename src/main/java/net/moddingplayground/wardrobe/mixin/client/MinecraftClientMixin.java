package net.moddingplayground.wardrobe.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.Session;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticRenderers;
import net.moddingplayground.wardrobe.api.client.util.SessionWatcher;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Final private Session session;
    @Shadow @Nullable public abstract ServerInfo getCurrentServerEntry();

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(boolean tick, CallbackInfo ci) {
        SessionWatcher.update(this.session);
    }

    @Inject(method = "checkGameData", at = @At("TAIL"))
    private void onCheckGameData(CallbackInfo ci) {
        CosmeticRenderers.isMissingRendererFactories();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    private void onDisconnect(Screen screen, CallbackInfo ci) {
        ServerInfo server = this.getCurrentServerEntry();
        if (server != null) {
            server.setSupported(false);
        }
    }
}
