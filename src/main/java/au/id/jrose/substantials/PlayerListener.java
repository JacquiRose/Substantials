package au.id.jrose.substantials;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerListener implements Listener {

    @NotNull
    private final Main main;

    public PlayerListener(@NotNull Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        main.loadPlayerData(uuid);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // dsadasd
    }
}
