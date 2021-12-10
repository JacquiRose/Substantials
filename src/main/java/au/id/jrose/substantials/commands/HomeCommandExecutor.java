package au.id.jrose.substantials.commands;

import au.id.jrose.substantials.PlayerDataController;
import au.id.jrose.substantials.models.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeCommandExecutor implements CommandExecutor {

    @NotNull
    private final PlayerDataController dataController;

    public HomeCommandExecutor(@NotNull PlayerDataController dataController) {
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
        Location home = playerData.getHome(homeName);

        if (home == null) {
            sender.sendMessage("Home does not exist.");
            return true;
        }

        player.teleport(home);
        player.sendMessage(ChatColor.GREEN + "Teleported!");
        player.playSound(player.getLocation(), Sound.ENTITY_FOX_AGGRO, 1F, 1F);

        return true;
    }
}
