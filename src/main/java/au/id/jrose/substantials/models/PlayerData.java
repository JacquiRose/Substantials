package au.id.jrose.substantials.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {

    @Nullable
    private String nickname;
    @NotNull
    private final Map<String, Location> homes;

    public PlayerData() {
        nickname = null;
        homes = new HashMap<>();
    }

    public PlayerData(@Nullable String nickname, @NotNull Map<String, Location> homes) {
        this.nickname = nickname;
        this.homes = homes;
    }

    @Nullable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(@Nullable String nickname) {
        this.nickname = nickname;
    }

    /**
     * @param homeName The name of the home.
     * @return The {@link Location} of this home, or {@code null} if the home doesn't exist.
     */
    @Nullable
    public Location getHome(@NotNull String homeName) {
        return homes.getOrDefault(homeName, null);
    }

    /**
     * Adds a new home for this player.
     *
     * @param homeName The name of the home.
     * @param location The {@link Location} of the home.
     */
    public void addHome(@NotNull String homeName, @NotNull Location location) {
        homes.put(homeName, location);
    }

    /**
     * Removes the specified home for this player.
     *
     * @param homeName The name of the home.
     */
    public void removeHome(@NotNull String homeName) {
        homes.remove(homeName);
    }
}
