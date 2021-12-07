package au.id.jrose.substantials;

import au.id.jrose.substantials.models.PlayerData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class Main extends JavaPlugin {

    @NotNull
    private Map<UUID, PlayerData> playerDataMap;

    @Override
    public void onEnable() {
        File pluginFolder = this.getDataFolder();
        if (!pluginFolder.exists() && !pluginFolder.mkdir()) {
            throw new IllegalStateException("Could not create Substantials folder");
        }

        playerDataMap = new HashMap<>();

        PlayerListener playerListener = new PlayerListener(this);
        this.getServer().getPluginManager().registerEvents(playerListener, this);
    }

    @Override
    public void onDisable() {
    }

    public void loadPlayerData(@NotNull UUID uuid) {
        File playerDataFile = new File(this.getDataFolder(), uuid + ".yml");

        PlayerData playerData;

        if (playerDataFile.exists()) {
            playerData = loadPlayerDataFromFile(playerDataFile);
        } else {
            playerData = new PlayerData();
        }

        playerDataMap.put(uuid, playerData);
    }

    private PlayerData loadPlayerDataFromFile(File playerDataFile) {
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(playerDataFile);

        String nickname = yamlFile.getString("nickname");

        Map<String, Location> homesMap = new HashMap<>();

        ConfigurationSection homes = yamlFile.getConfigurationSection("homes");
        Objects.requireNonNull(homes);

        for (String homeName : homes.getKeys(false)) {
            ConfigurationSection home = homes.getConfigurationSection(homeName);
            Objects.requireNonNull(home);

            World world = this.getServer().getWorld(home.getString("world"));
            double x = home.getDouble("x");
            double y = home.getDouble("y");
            double z = home.getDouble("z");
            float pitch = (float) home.getDouble("pitch");
            float yaw = (float) home.getDouble("yaw");

            homesMap.put(homeName, new Location(world, x, y, z, pitch, yaw));
        }

        return new PlayerData(nickname, homesMap);
    }

    public void savePlayerData(@NotNull UUID uuid) {
        File playerDataFile = new File(this.getDataFolder(), uuid + ".yml");
        PlayerData playerData = playerDataMap.get(uuid);

        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(playerDataFile);
        yamlFile.set("homes", playerData.getNickname());
        //yamlFile.createSection()
    }
}
