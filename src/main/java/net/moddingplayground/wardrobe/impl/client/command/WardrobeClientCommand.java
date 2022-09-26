package net.moddingplayground.wardrobe.impl.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.client.cosmetic.LocalServerCosmeticsManager;
import net.moddingplayground.wardrobe.api.cosmetic.Cosmetic;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticData;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;
import net.moddingplayground.wardrobe.impl.command.WardrobeCommand;
import net.moddingplayground.wardrobe.impl.command.argument.CosmeticArgumentType;
import net.moddingplayground.wardrobe.impl.command.argument.CosmeticSlotArgumentType;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

@Environment(EnvType.CLIENT)
public interface WardrobeClientCommand extends WardrobeCommand {
    static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal(Wardrobe.MOD_ID + "-client:cosmetics").requires(WardrobeClientCommand::isUnsupported).redirect(dispatcher.register(
            literal(Wardrobe.MOD_ID + "-client")
                .requires(WardrobeClientCommand::isUnsupported)
                .then(
                    literal("equip")
                        .then(
                            argument(COSMETIC_KEY, CosmeticArgumentType.cosmetic())
                                .executes(WardrobeClientCommand::executeEquipSlot)
                                .then(
                                    argument(COLOR_KEY, ColorArgumentType.color())
                                        .executes(WardrobeClientCommand::executeEquipSlot)
                                )
                        )
                )
                .then(
                    literal("clear")
                        .executes(WardrobeClientCommand::executeClear)
                        .then(
                            argument(COSMETIC_SLOT_KEY, CosmeticSlotArgumentType.cosmeticSlot())
                                .executes(WardrobeClientCommand::executeClearSlot)
                        )
                )
        )));
    }

    static boolean isUnsupported(FabricClientCommandSource source) {
        MinecraftClient client = source.getClient();
        if (client.isIntegratedServerRunning()) {
            return false;
        }

        ServerInfo server = client.getCurrentServerEntry();
        return server == null || !server.isSupported();
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
        CosmeticData data = player.getCosmeticData();

        if (data.equip(new CosmeticInstance(cosmetic, color), player)) {
            LocalServerCosmeticsManager.INSTANCE.saveFile();
        } else {
            throw EQUIP_FAIL_EXCEPTION.create();
        }

        source.sendFeedback(Text.translatable("command.%s.equip".formatted(Wardrobe.MOD_ID), cosmetic.getOrCreateDisplayText(), Text.literal("⬛").setStyle(Style.EMPTY.withColor(color))));
        return 1;
    }

    static int executeClear(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        FabricClientCommandSource source = context.getSource();
        ClientPlayerEntity player = source.getPlayer();
        CosmeticData data = player.getCosmeticData();

        boolean affected = false;
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            if (data.clear(slot, player)) {
                affected = true;
            }
        }

        if (affected) {
            LocalServerCosmeticsManager.INSTANCE.saveFile();
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
        CosmeticData data = player.getCosmeticData();

        if (data.clear(slot)) {
            LocalServerCosmeticsManager.INSTANCE.saveFile();
        } else {
            throw EQUIP_FAIL_EXCEPTION.create();
        }

        source.sendFeedback(Text.translatable("command.%s.clear_slot".formatted(Wardrobe.MOD_ID), slot.name()));
        return 1;
    }
}
