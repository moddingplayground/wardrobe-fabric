package net.moddingplayground.wardrobe.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ServerInfo;
import net.moddingplayground.wardrobe.api.client.network.WardrobeServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(ServerInfo.class)
public class ServerInfoMixin implements WardrobeServerInfo {
    @Unique
    private boolean supported;

    @Unique
    @Override
    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    @Unique
    @Override
    public boolean isSupported() {
        return this.supported;
    }
}
