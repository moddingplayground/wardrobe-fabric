package net.moddingplayground.wardrobe.impl.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.client.gui.screen.WardrobeScreen;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

@Environment(EnvType.CLIENT)
public interface WardrobeGuiCommand {
    static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal(Wardrobe.MOD_ID + ":gui").executes(WardrobeGuiCommand::execute));
    }

    static int execute(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient client = context.getSource().getClient();
        client.send(() -> client.setScreen(new WardrobeScreen()));
        return 1;
    }
}
