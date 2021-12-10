package au.id.jrose.substantials.commands;

import au.id.jrose.substantials.PlayerDataController;
import au.id.jrose.substantials.models.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DelHomeCommandExecutor implements CommandExecutor {

    @NotNull
    private final PlayerDataController dataController;

    public DelHomeCommandExecutor(@NotNull PlayerDataController dataController) {
        this.dataController = dataController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length > 1) {
            sender.sendMessage("Too many arguments.");
            return false;
        }

        PlayerData playerData = dataController.getPlayerData(player.getUniqueId());
        String homeName = args.length == 1 ? args[0] : "home";

        if (!playerData.getHomeNames().contains(homeName)) {
            player.sendMessage("Home does not exist.");
            return true;
        }

        playerData.removeHome(homeName);

        player.sendMessage(ChatColor.GREEN + "Home removed!");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);

        dataController.savePlayerData(player.getUniqueId());

        return true;
    }
}