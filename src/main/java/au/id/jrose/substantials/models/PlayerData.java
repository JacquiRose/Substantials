package au.id.jrose.substantials.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerData {

    @Nullable
    private String nickname;
    @NotNull
    private final Map<String, Location> homes;
    private boolean tpToggle;

    /**
     * If the player doesn't have an existing .yml file, use this constructor.
     */
    public PlayerData() {
        nickname = null;
        homes = new HashMap<>();
        tpToggle = false;
    }

    /**
     * If the player has an existing .yml file, use this constructor.
     *
     * @param nickname The player's chosen nickname, otherwise {@code null} if they don't have one.
     * @param homes    The player's homes, if any.
     */
    public PlayerData(@Nullable String nickname, @NotNull Map<String, Location> homes, boolean tpToggle) {
        this.nickname = nickname;
        this.homes = homes;
        this.tpToggle = tpToggle;
    }

    /**
     * @return The player's chosen nickname, otherwise {@code null} if they don't have one.
     */
    @Nullable
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the player's chosen nickname.
     *
     * @param nickname The player's chosen nickname, otherwise {@code null} if they don't have one.
     */
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
     * Sets a home for this player.
     *
     * @param homeName The name of the home.
     * @param location The {@link Location} of the home.
     */
    public void setHome(@NotNull String homeName, @NotNull Location location) {
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

    /**
     * @return The name(s) of the player's home(s).
     */
    public Set<String> getHomeNames() {
        return homes.keySet();
    }

    public boolean isTpToggle() {
        return tpToggle;
    }

    public void setTpToggle(boolean tpToggle) {
        this.tpToggle = tpToggle;
    }
}
