package net.moddingplayground.wardrobe.api.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.client.util.WardrobeConfig;
import net.moddingplayground.wardrobe.api.command.argument.CosmeticArgumentType;
import net.moddingplayground.wardrobe.api.command.argument.CosmeticSlotArgumentType;
import net.moddingplayground.wardrobe.api.cosmetic.Cosmetic;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import net.moddingplayground.wardrobe.api.cosmetic.data.PlayerCosmeticData;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

@Environment(EnvType.CLIENT)
public interface WardrobeCommand {
    String COSMETIC_KEY = "cosmetic";
    String COSMETIC_SLOT_KEY = "cosmetic_slot";
    String COLOR_KEY = "color";

    SimpleCommandExceptionType EQUIP_FAIL_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("command.%s.equip_fail".formatted(Wardrobe.MOD_ID)));

    static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            literal(Wardrobe.MOD_ID)
                .then(
                    literal("reload")
                        .executes(WardrobeCommand::executeReload)
                )
                .then(
                    literal("equip")
                        .then(
                            argument(COSMETIC_KEY, CosmeticArgumentType.cosmetic())
                                .executes(WardrobeCommand::executeEquipSlot)
                                .then(
                                    argument(COLOR_KEY, ColorArgumentType.color())
                                        .executes(WardrobeCommand::executeEquipSlot)
                                )
                        )
                )
                .then(
                    literal("clear")
                        .executes(WardrobeCommand::executeClear)
                        .then(
                            argument(COSMETIC_SLOT_KEY, CosmeticSlotArgumentType.cosmeticSlot())
                                .executes(WardrobeCommand::executeClearSlot)
                    )
                )
        );
    }

    static int executeReload(CommandContext<FabricClientCommandSource> context) {
        WardrobeConfig.loadClientCosmeticData();
        context.getSource().sendFeedback(Text.translatable("command.%s.reload".formatted(Wardrobe.MOD_ID)));
        return 1;
    }

    static int executeEquipSlot(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        Cosmetic cosmetic = CosmeticArgumentType.getCosmetic(context, COSMETIC_KEY);

        int color = Util.make(() -> {
            try {
                Formatting formatting = context.getArgument(COLOR_KEY, Formatting.class);
                return !formatting.isColor() ? 0xFFFFFF : formatting.getColorValue();
            } catch (IllegalArgumentException ignored) {
            }

            return 0xFFFFFF;
        });

        FabricClientCommandSource source = context.getSource();
        ClientPlayerEntity player = source.getPlayer();
        PlayerCosmeticData data = player.getCosmeticData();

        if (data.equip(new CosmeticInstance(cosmetic, color))) {
            WardrobeConfig.flushClientCosmeticData();
        } else {
            throw EQUIP_FAIL_EXCEPTION.create();
        }

        source.sendFeedback(Text.translatable("command.%s.equip".formatted(Wardrobe.MOD_ID), cosmetic.getOrCreateDisplayText(), Text.literal("⬛").setStyle(Style.EMPTY.withColor(color))));
        return 1;
    }

    static int executeClear(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        FabricClientCommandSource source = context.getSource();
        ClientPlayerEntity player = source.getPlayer();
        PlayerCosmeticData data = player.getCosmeticData();

        boolean affected = false;
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            if (data.clear(slot)) {
                affected = true;
            }
        }

        if (affected) {
            WardrobeConfig.flushClientCosmeticData();
        } else {
            throw EQUIP_FAIL_EXCEPTION.create();
        }

        source.sendFeedback(Text.translatable("command.%s.clear".formatted(Wardrobe.MOD_ID)));
        return 1;
    }

    static int executeClearSlot(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        CosmeticSlot slot = CosmeticSlotArgumentType.getCosmeticSlot(context, COSMETIC_SLOT_KEY);

        FabricClientCommandSource source = context.getSource();
        ClientPlayerEntity player = source.getPlayer();
        PlayerCosmeticData data = player.getCosmeticData();

        if (data.clear(slot)) {
            WardrobeConfig.flushClientCosmeticData();
        } else {
            throw EQUIP_FAIL_EXCEPTION.create();
        }

        source.sendFeedback(Text.translatable("command.%s.clear_slot".formatted(Wardrobe.MOD_ID), slot.name()));
        return 1;
    }
}
