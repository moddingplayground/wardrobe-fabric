package net.moddingplayground.wardrobe.impl.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CosmeticSlotArgumentType implements ArgumentType<CosmeticSlot> {
    public static final List<String> SLOTS = Arrays.stream(CosmeticSlot.values()).map(Enum::name).toList();
    public static final DynamicCommandExceptionType UNKNOWN_SLOT_EXCEPTION = new DynamicCommandExceptionType(id -> Text.translatable("%s.cosmetic.slot.unknown".formatted(Wardrobe.MOD_ID), id));

    public static CosmeticSlotArgumentType cosmeticSlot() {
        return new CosmeticSlotArgumentType();
    }

    public static CosmeticSlot getCosmeticSlot(CommandContext<?> context, String name) {
        return context.getArgument(name, CosmeticSlot.class);
    }

    @Override
    public CosmeticSlot parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readUnquotedString();
        try {
            return CosmeticSlot.valueOf(name);
        } catch (IllegalArgumentException exception) {
            throw UNKNOWN_SLOT_EXCEPTION.create(name);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(SLOTS, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return SLOTS;
    }
}
