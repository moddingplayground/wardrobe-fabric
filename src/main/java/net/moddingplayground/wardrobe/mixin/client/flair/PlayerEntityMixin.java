package net.moddingplayground.wardrobe.mixin.client.flair;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticPlayerEntity;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import net.moddingplayground.wardrobe.api.cosmetic.data.PlayerCosmeticData;
import net.moddingplayground.wardrobe.api.network.WardrobeNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements CosmeticPlayerEntity {
    private PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "addExperienceLevels", at = @At("TAIL"))
    private void onAddExperienceLevels(int levels, CallbackInfo ci) {
        PlayerEntity that = (PlayerEntity) (Object) this;
        if (that instanceof ServerPlayerEntity serverPlayer) {
            PlayerCosmeticData data = this.getCosmeticData();
            data.get(CosmeticSlot.FLAIR).ifPresent(cosmetic -> {
                sendLevelUp(serverPlayer, that, cosmetic);
                for (ServerPlayerEntity player : PlayerLookup.tracking(that)) {
                    sendLevelUp(player, that, cosmetic);
                }
            });
        }
    }

    @Unique
    private void sendLevelUp(ServerPlayerEntity receiver, PlayerEntity player, CosmeticInstance cosmetic) {
        PacketByteBuf buf = cosmetic.toPacket();
        buf.writeUuid(player.getUuid());
        buf.writeInt(player.experienceLevel);
        ServerPlayNetworking.send(receiver, WardrobeNetworking.LEVEL_UP_PACKET_ID, buf);
    }
}
