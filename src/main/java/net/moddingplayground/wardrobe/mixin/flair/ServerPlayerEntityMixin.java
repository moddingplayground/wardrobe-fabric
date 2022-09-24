package net.moddingplayground.wardrobe.mixin.flair;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import net.moddingplayground.wardrobe.api.network.WardrobeNetworking;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    private ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, profile, publicKey);
    }

    @Inject(method = "addExperienceLevels", at = @At("TAIL"))
    private void onAddExperienceLevels(int levels, CallbackInfo ci) {
        ServerPlayerEntity that = (ServerPlayerEntity) (Object) this;
        this.getCosmeticData().run(CosmeticSlot.FLAIR, cosmetic -> {
            sendLevelUp(that, that, cosmetic);
            for (ServerPlayerEntity player : PlayerLookup.tracking(that)) {
                sendLevelUp(player, that, cosmetic);
            }
        });
    }

    @Unique
    private void sendLevelUp(ServerPlayerEntity receiver, PlayerEntity player, CosmeticInstance cosmetic) {
        PacketByteBuf buf = cosmetic.toPacket();
        buf.writeUuid(player.getUuid());
        buf.writeInt(player.experienceLevel);
        ServerPlayNetworking.send(receiver, WardrobeNetworking.LEVEL_UP_PACKET_ID, buf);
    }

    @Inject(method = "addCritParticles", at = @At("TAIL"))
    private void onAddCritParticles(Entity target, CallbackInfo ci) {
        ServerPlayerEntity that = (ServerPlayerEntity) (Object) this;
        this.getCosmeticData().run(CosmeticSlot.FLAIR, cosmetic -> {
            sendCrit(that, that, target, cosmetic);
            for (ServerPlayerEntity player : PlayerLookup.tracking(target)) {
                sendCrit(player, that, target, cosmetic);
            }
        });
    }

    @Unique
    private void sendCrit(ServerPlayerEntity receiver, PlayerEntity player, Entity entity, CosmeticInstance cosmetic) {
        PacketByteBuf buf = cosmetic.toPacket();
        buf.writeUuid(player.getUuid());
        buf.writeInt(entity.getId());
        ServerPlayNetworking.send(receiver, WardrobeNetworking.CRIT_PACKET_ID, buf);
    }
}
