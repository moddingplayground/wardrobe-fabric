package net.moddingplayground.wardrobe.impl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.cosmetic.Cosmetic;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticData;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import net.moddingplayground.wardrobe.impl.command.argument.CosmeticArgumentType;
import net.moddingplayground.wardrobe.impl.command.argument.CosmeticSlotArgumentType;

import static net.minecraft.server.command.CommandManager.*;

public interface WardrobeCommand {
    String COSMETIC_KEY = "cosmetic";
    String COSMETIC_SLOT_KEY = "cosmetic_slot";
    String COLOR_KEY = "color";

    SimpleCommandExceptionType EQUIP_FAIL_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("command.%s.equip_fail".formatted(Wardrobe.MOD_ID)));

    static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> root = dispatcher.register(
            literal(Wardrobe.MOD_ID)
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
        dispatcher.register(literal(Wardrobe.MOD_ID + ":cosmetics").redirect(root));
    }

    static int executeEquipSlot(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Cosmetic cosmetic = CosmeticArgumentType.getCosmetic(context, COSMETIC_KEY);

        int color = Util.make(() -> {
            try {
                Formatting formatting = context.getArgument(COLOR_KEY, Formatting.class);
                return !formatting.isColor() ? 0xFFFFFF : formatting.getColorValue();
            } catch (IllegalArgumentException ignored) {
            }

            return 0xFFFFFF;
        });

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        CosmeticData data = player.getCosmeticData();

        if (!data.equip(new CosmeticInstance(cosmetic, color), player)) {
            throw EQUIP_FAIL_EXCEPTION.create();
        }

        source.sendFeedback(Text.translatable("command.%s.equip".formatted(Wardrobe.MOD_ID), cosmetic.getOrCreateDisplayText(), Text.literal("⬛").setStyle(Style.EMPTY.withColor(color))), false);
        return 1;
    }

    static int executeClear(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        CosmeticData data = player.getCosmeticData();

        boolean affected = false;
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            if (data.clear(slot, player)) {
                affected = true;
            }
        }

        if (!affected) {
            throw EQUIP_FAIL_EXCEPTION.create();
        }

        source.sendFeedback(Text.translatable("command.%s.clear".formatted(Wardrobe.MOD_ID)), false);
        return 1;
    }

    static int executeClearSlot(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        CosmeticSlot slot = CosmeticSlotArgumentType.getCosmeticSlot(context, COSMETIC_SLOT_KEY);

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        CosmeticData data = player.getCosmeticData();

        if (!data.clear(slot)) {
            throw EQUIP_FAIL_EXCEPTION.create();
        }

        source.sendFeedback(Text.translatable("command.%s.clear_slot".formatted(Wardrobe.MOD_ID), slot.name()), false);
        return 1;
    }
}
