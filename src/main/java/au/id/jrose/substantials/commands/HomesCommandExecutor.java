package au.id.jrose.substantials.commands;

import au.id.jrose.substantials.PlayerDataController;
import au.id.jrose.substantials.models.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomesCommandExecutor implements CommandExecutor {

    @NotNull
    private final PlayerDataController dataController;

    public HomesCommandExecutor(@NotNull PlayerDataController dataController) {
        this.dataController = dataController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        PlayerData playerData = dataController.getPlayerData(player.getUniqueId());
        String[] homeNames = playerData.getHomeNames().toArray(new String[0]);

        StringBuilder message = new StringBuilder(ChatColor.YELLOW + String.format("Homes (%d): ", homeNames.length));
        for (int i = 0; i < homeNames.length; i++) {
            message.append(homeNames[i]);
            message.append(i < homeNames.length - 1 ? ", " : "");
        }

        sender.sendMessage(message.toString());

        return true;
    }
}
