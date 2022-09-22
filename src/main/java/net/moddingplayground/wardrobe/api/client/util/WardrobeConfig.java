package net.moddingplayground.wardrobe.api.client.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.logging.LogUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.cosmetic.data.PlayerCosmeticData;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;

public class WardrobeConfig {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final File FILE = FabricLoader.getInstance().getConfigDir().resolve(Wardrobe.MOD_ID + "/cosmetics.json").toFile();

    public static void flushClientCosmeticData() {
        File parent = FILE.getParentFile();
        if (parent.exists() || parent.mkdirs()) {
            try (PrintWriter out = new PrintWriter(FILE)) {
                StringWriter string = new StringWriter();

                JsonWriter jsonWriter = new JsonWriter(string);
                jsonWriter.setLenient(true);
                jsonWriter.setIndent("  ");

                MinecraftClient client = MinecraftClient.getInstance();
                PlayerCosmeticData data = client.player.getCosmeticData();
                Streams.write(data.toJson(), jsonWriter);
                out.println(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadClientCosmeticData() {
        try {
            String string = new String(Files.readAllBytes(FILE.toPath()));
            if (!string.isEmpty()) {
                MinecraftClient client = MinecraftClient.getInstance();
                PlayerCosmeticData data = client.player.getCosmeticData();
                if (JsonParser.parseString(string) instanceof JsonObject jsonObject) {
                    data.fromJson(jsonObject);
                } else {
                    throw new IOException("File contents was not a JsonObject");
                }
            } else {
                throw new IOException("File text not found");
            }
        } catch (Throwable throwable) {
            LOGGER.warn("Could not load saved cosmetics configuration!", throwable);
            flushClientCosmeticData();
        }
    }
}
