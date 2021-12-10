package au.id.jrose.substantials;

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
        UUID uuid = event.getPlayer().getUniqueId();
        playerDataController.loadPlayerData(uuid);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        //TODO: Remove player data once they quit so we don't waste space on memory.
    }
}
