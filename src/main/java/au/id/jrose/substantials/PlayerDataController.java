package au.id.jrose.substantials;

import au.id.jrose.substantials.models.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Maintains instances of {@link PlayerData}.
 * These can be loaded through a file or created fresh, and saved back to a file.
 */
public class PlayerDataController {

    @NotNull
    private final Logger logger;
    @NotNull
    private final File dataFolder;
    @NotNull
    private final Map<UUID, PlayerData> playerDataMap;

    /**
     * Creates a new PlayerDataController instance.
     *
     * @param dataFolder The plugin's data directory.
     * @param logger     Logging object for the console.
     */
    public PlayerDataController(@NotNull File dataFolder, @NotNull Logger logger) {
        if (!dataFolder.exists() && !dataFolder.mkdir()) {
            throw new IllegalStateException("Could not create Substantials folder");
        }

        this.dataFolder = dataFolder;
        this.logger = logger;
        playerDataMap = new HashMap<>();
    }

    /**
     * @param uuid The player's {@link UUID}.
     * @return The associated {@link PlayerData} object.
     */
    @NotNull
    public PlayerData getPlayerData(@NotNull UUID uuid) {
        return playerDataMap.get(uuid);
    }

    /**
     * Removes the loaded {@link PlayerData} from the {@link #playerDataMap}.
     *
     * @param uuid The player's {@link UUID}.
     */
    public void removePlayerData(@NotNull UUID uuid) {
        playerDataMap.remove(uuid);
    }

    /**
     * Loads the player's data from the .yml file (or, creates a new .yml if one doesn't already exist).
     * The associated {@link PlayerData} is then stored in the {@link #playerDataMap}.
     *
     * @param player The player.
     */
    public void loadPlayerData(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        File playerDataFile = new File(dataFolder, uuid + ".yml");

        PlayerData playerData;

        if (playerDataFile.exists()) {
            logger.log(Level.INFO, String.format("Loading existing player data from %s", playerDataFile));
            playerData = loadPlayerDataFromFile(playerDataFile);
        } else {
            playerData = new PlayerData();
        }

        if (playerData.getNickname() != null) {
            player.setDisplayName(ChatColor.translateAlternateColorCodes('&', playerData.getNickname()) + ChatColor.RESET);
        }

        playerDataMap.put(uuid, playerData);

        if (!playerDataFile.exists()) savePlayerData(uuid);
    }

    /**
     * @param section The .yml section to validate.
     * @param paths   The paths required to exist in the .yml section.
     * @throws IllegalArgumentException One of the provided paths does not exist in the .yml section.
     */
    private void validateSection(@NotNull ConfigurationSection section, @NotNull String[] paths) throws IllegalArgumentException {
        for (String path : paths)
            if (!section.contains(path))
                throw new IllegalArgumentException(String.format("Required path %s does not exist", path));
    }

    /**
     * @param locationSection A section of a .yml file that stores a {@link Location}.
     * @return The specified {@link Location} based on the .yml section's data.
     * @throws IllegalArgumentException If there is missing data from the section (or the specified world doesn't exist).
     */
    @SuppressWarnings("ConstantConditions")
    @NotNull
    private Location loadLocation(@NotNull ConfigurationSection locationSection) throws IllegalArgumentException {
        validateSection(locationSection, new String[]{"world", "x", "y", "z", "yaw", "pitch"});

        String worldName = locationSection.getString("world");
        World world = Bukkit.getServer().getWorld(worldName);

        if (world == null) throw new IllegalArgumentException(String.format("World %s does not exist", world));

        double x = locationSection.getDouble("x");
        double y = locationSection.getDouble("y");
        double z = locationSection.getDouble("z");
        float yaw = (float) locationSection.getDouble("yaw");
        float pitch = (float) locationSection.getDouble("pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * @param homesSection The section of the player's .yml file that stores each home.
     * @return The player's homes, if any.
     */
    @NotNull
    private Map<String, Location> loadPlayerHomes(@Nullable ConfigurationSection homesSection) {
        Map<String, Location> result = new HashMap<>();
        if (homesSection == null) return result;

        for (String homeName : homesSection.getKeys(false)) {
            ConfigurationSection homeSection = homesSection.getConfigurationSection(homeName);
            assert homeSection != null;

            try {
                Location home = loadLocation(homeSection);
                result.put(homeName, home);
            } catch (IllegalArgumentException e) {
                logger.log(Level.WARNING, String.format("Unable to load home: %s", e.getMessage()));
            }
        }

        return result;
    }

    /**
     * @param playerDataFile The player's .yml file.
     * @return A new instance of {@link PlayerData} loaded from the player's .yml file.
     */
    @NotNull
    private PlayerData loadPlayerDataFromFile(@NotNull File playerDataFile) {
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(playerDataFile);

        String nickname = yamlFile.contains("nickname") ? yamlFile.getString("nickname") : null;
        Map<String, Location> homesMap = loadPlayerHomes(yamlFile.getConfigurationSection("homes"));

        return new PlayerData(nickname, homesMap);
    }

    /**
     * Saves the player's {@link PlayerData} state back to their .yml file.
     *
     * @param uuid The player's UUID.
     */
    public void savePlayerData(@NotNull UUID uuid) {
        File playerDataFile = new File(dataFolder, uuid + ".yml");
        PlayerData playerData = playerDataMap.get(uuid);

        logger.log(Level.INFO, String.format("Saving player data to %s", playerDataFile));

        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(playerDataFile);
        yamlFile.set("nickname", playerData.getNickname());

        ConfigurationSection homesSection = yamlFile.createSection("homes");
        for (String homeName : playerData.getHomeNames()) {
            Location home = playerData.getHome(homeName);
            assert home != null;

            if (home.getWorld() == null) {
                logger.log(Level.WARNING, String.format("Unable to save home %s: world was null", homeName));
                continue;
            }

            ConfigurationSection homeSection = homesSection.createSection(homeName);
            homeSection.set("world", home.getWorld().getName());
            homeSection.set("x", home.getX());
            homeSection.set("y", home.getY());
            homeSection.set("z", home.getZ());
            homeSection.set("yaw", home.getYaw());
            homeSection.set("pitch", home.getPitch());
        }

        try {
            yamlFile.save(playerDataFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, String.format("Unable to save player data to %s", playerDataFile), e);
        }
    }
}


