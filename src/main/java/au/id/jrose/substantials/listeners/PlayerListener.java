package au.id.jrose.substantials.listeners;

import au.id.jrose.substantials.PlayerDataController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerListener implements Listener {

    @NotNull
    private final PlayerDataController playerDataController;

    public PlayerListener(@NotNull PlayerDataController playerDataController) {
        this.playerDataController = playerDataController;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        playerDataController.loadPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playerDataController.removePlayerData(event.getPlayer().getUniqueId());
    }
}
