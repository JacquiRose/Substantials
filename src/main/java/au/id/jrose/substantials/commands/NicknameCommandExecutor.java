package au.id.jrose.substantials.commands;

import au.id.jrose.substantials.PlayerDataController;
import au.id.jrose.substantials.models.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NicknameCommandExecutor implements CommandExecutor {

    @NotNull
    private final PlayerDataController dataController;

    public NicknameCommandExecutor(@NotNull PlayerDataController dataController) {
        this.dataController = dataController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments.");
            return false;
        }

        String nickname = args[0];
        String formattedNickname = ChatColor.translateAlternateColorCodes('&', nickname);
        String plainNickname = ChatColor.stripColor(formattedNickname);

        if (plainNickname.length() > 16) {
            sender.sendMessage(ChatColor.RED + "Nickname cannot be longer than 16 characters.");
            return true;
        } else if (nickname.length() > 32) {
            sender.sendMessage(ChatColor.RED + "Formatted nickname cannot be longer than 32 characters.");
            return true;
        } else if (plainNickname.length() < 3)  {
            sender.sendMessage(ChatColor.RED + "Nickname must be at least 3 characters.");
            return true;
        }

        PlayerData playerData = dataController.getPlayerData(player.getUniqueId());

        if (nickname.equals("clear")) {
            playerData.setNickname(null);
            player.setDisplayName(player.getName());
            player.sendMessage(ChatColor.GREEN + "Nickname cleared!");
        } else {
            playerData.setNickname(nickname);
            player.setDisplayName(formattedNickname + ChatColor.RESET);
            player.sendMessage(ChatColor.GREEN + String.format("Your nickname is now %s", formattedNickname) + ChatColor.RESET + ChatColor.GREEN + ".");
        }

        dataController.savePlayerData(player.getUniqueId());
        return true;
    }
}
