package net.moddingplayground.wardrobe.api.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.cosmetic.Cosmetic;
import net.moddingplayground.wardrobe.api.registry.WardrobeRegistry;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CosmeticArgumentType implements ArgumentType<Cosmetic> {
    public static final List<String> EXAMPLES = List.of("sunhat");
    public static final DynamicCommandExceptionType UNKNOWN_COSMETIC_EXCEPTION = new DynamicCommandExceptionType(id -> Text.translatable("%s.cosmetic.unknown".formatted(Wardrobe.MOD_ID), id));

    public static CosmeticArgumentType cosmetic() {
        return new CosmeticArgumentType();
    }

    public static Cosmetic getCosmetic(CommandContext<?> context, String name) {
        return context.getArgument(name, Cosmetic.class);
    }

    @Override
    public Cosmetic parse(StringReader reader) throws CommandSyntaxException {
        Identifier identifier = Identifier.fromCommandInput(reader);
        return WardrobeRegistry.COSMETIC.getOrEmpty(identifier).orElseThrow(() -> UNKNOWN_COSMETIC_EXCEPTION.create(identifier));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestIdentifiers(WardrobeRegistry.COSMETIC.getIds(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
