package net.moddingplayground.wardrobe.api.client.cosmetic;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Util;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticData;
import net.moddingplayground.wardrobe.api.cosmetic.data.PlayerCosmeticData;
import org.slf4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores cosmetic data saved per server IP. This provides cosmetics
 * compatibility with servers that do not have Wardrobe installed.
 */
@Environment(EnvType.CLIENT)
public class LocalServerCosmeticsManager {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final LocalServerCosmeticsManager INSTANCE = new LocalServerCosmeticsManager(MinecraftClient.getInstance());

    public static final String FILENAME = "server_cosmetics.dat";

    private final Map<String, CosmeticData> serverData;
    private final File directory;

    public LocalServerCosmeticsManager(MinecraftClient client) {
        this.serverData = new HashMap<>();

        this.directory = new File(client.runDirectory.getPath() + "/" + Wardrobe.MOD_ID);
        //noinspection ResultOfMethodCallIgnored
        this.directory.mkdirs();

        this.loadFile();
    }

    public CosmeticData getOrCreateCosmeticData(String ip) {
        return serverData.computeIfAbsent(ip, ipx -> new PlayerCosmeticData());
    }

    public CosmeticData getOrCreateCosmeticData(ServerInfo server) {
        return this.getOrCreateCosmeticData(server.address);
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        this.serverData.forEach((ip, data) -> nbt.put(ip, data.toNbt()));
        return nbt;
    }

    public void fromNbt(NbtCompound nbt) {
        this.serverData.clear();
        for (String ip : nbt.getKeys()) {
            CosmeticData data = new PlayerCosmeticData();
            data.fromNbt(nbt.getCompound(ip));
            this.serverData.put(ip, data);
        }
    }

    public void saveFile() {
        try {
            File temp = File.createTempFile(FILENAME.substring(0, FILENAME.length() - 4), ".dat", this.directory);
            NbtIo.write(this.toNbt(), temp);
            File backup = new File(this.directory, FILENAME + "_old");
            File newFile = new File(this.directory, FILENAME);
            Util.backupAndReplace(newFile, temp, backup);
        }
        catch (Exception exception) {
            LOGGER.error("Couldn't save local server cosmetics", exception);
        }
    }

    public void loadFile() {
        try {
            NbtCompound nbt = NbtIo.read(new File(this.directory, FILENAME));
            if (nbt != null) {
                this.fromNbt(nbt);
            }
        }
        catch (Exception exception) {
            LOGGER.error("Couldn't load local server cosmetics", exception);
        }
    }
}
