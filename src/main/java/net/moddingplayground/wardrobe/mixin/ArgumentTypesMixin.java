package net.moddingplayground.wardrobe.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.registry.Registry;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.impl.command.argument.CosmeticArgumentType;
import net.moddingplayground.wardrobe.impl.command.argument.CosmeticSlotArgumentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArgumentTypes.class)
public abstract class ArgumentTypesMixin {
    @Shadow
    private static <A extends ArgumentType<?>, T extends ArgumentSerializer.ArgumentTypeProperties<A>> ArgumentSerializer<A, T> register(Registry<ArgumentSerializer<?, ?>> registry, String id, Class<? extends A> clazz, ArgumentSerializer<A, T> serializer) {
        throw new AssertionError();
    }

    @Inject(method = "register(Lnet/minecraft/util/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At("TAIL"))
    private static void onRegister(Registry<ArgumentSerializer<?, ?>> registry, CallbackInfoReturnable<ArgumentSerializer<?, ?>> cir) {
        register(registry, Wardrobe.MOD_ID + ":cosmetic", CosmeticArgumentType.class, ConstantArgumentSerializer.of(CosmeticArgumentType::cosmetic));
        register(registry, Wardrobe.MOD_ID + ":cosmetic_slot", CosmeticSlotArgumentType.class, ConstantArgumentSerializer.of(CosmeticSlotArgumentType::cosmeticSlot));
    }
}
