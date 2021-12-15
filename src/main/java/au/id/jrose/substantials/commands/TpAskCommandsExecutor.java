package au.id.jrose.substantials.commands;

import au.id.jrose.substantials.PlayerDataController;
import au.id.jrose.substantials.models.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class TpAskCommandsExecutor implements CommandExecutor {

    @NotNull
    private final PlayerDataController dataController;
    @NotNull
    private final ArrayList<TeleportRequest> teleportRequests;

    public TpAskCommandsExecutor(@NotNull PlayerDataController dataController) {
        this.dataController = dataController;

        teleportRequests = new ArrayList<>();
    }

    private record TeleportRequest(@NotNull UUID sender, @NotNull UUID target) {
        private TeleportRequest(@NotNull UUID sender, @NotNull UUID target) {
            this.sender = sender;
            this.target = target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TeleportRequest that = (TeleportRequest) o;
            return sender.equals(that.sender) && target.equals(that.target);
        }
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        return switch (label.toLowerCase()) {
            case "tpask", "tpa" -> onTpAsk(player, args);
            case "tpaccept", "tpyes" -> onTpRespond(player, true, args);
            case "tpdeny", "tpno" -> onTpRespond(player, false, args);
            case "tptoggle" -> onTpToggle(player);
            default -> throw new IllegalArgumentException("Unexpected command label");
        };
    }

    private boolean onTpAsk(@NotNull Player player, @NotNull String[] args) {
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Invalid arguments.");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is not online.");
            return true;
        }

        TeleportRequest request = new TeleportRequest(player.getUniqueId(), target.getUniqueId());

        if (player.equals(target)) {
            player.sendMessage(ChatColor.RED + "You cannot send a teleport request to yourself.");
            return true;
        }

        PlayerData playerData = dataController.getPlayerData(target.getUniqueId());

        if (playerData.isTpToggle()) {
            player.sendMessage(ChatColor.RED + "This player is not receiving teleport requests at the moment.");
            return true;
        }

        if (teleportRequests.contains(request)) {
            player.sendMessage(ChatColor.RED + "You have already sent a teleport request to this player.");
            return true;
        }

        teleportRequests.add(request);

        player.sendMessage(
                String.format(
                        ChatColor.YELLOW + "Teleport request sent to %s" + ChatColor.RESET + ChatColor.YELLOW + ".",
                        target.getDisplayName()));
        target.sendMessage(
                String.format(
                        ChatColor.YELLOW + "Teleport request received from %s" + ChatColor.RESET + ChatColor.YELLOW +
                                ".\n Type " + ChatColor.GOLD + "/tpaccept" + ChatColor.YELLOW + " to accept, otherwise type " +
                                ChatColor.GOLD + "/tpdeny" + ChatColor.YELLOW + ".",
                        player.getDisplayName()));

        return true;
    }

    private boolean onTpRespond(@NotNull Player player, boolean response, @NotNull String[] args) {
        Player target;
        if (args.length > 1) {
            player.sendMessage(ChatColor.RED + "Invalid arguments.");
            return false;
        } else if (args.length == 1) {
            target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(ChatColor.RED + "That player is not online.");
                return true;
            }
        } else {
            target = null;

            for (TeleportRequest existingRequest : teleportRequests) {
                if (existingRequest.target().equals(player.getUniqueId())) {
                    if (!Bukkit.getOfflinePlayer(existingRequest.sender()).isOnline()) {
                        player.sendMessage(ChatColor.RED + "That player is no longer online.");
                        return true;
                    }

                    target = Bukkit.getPlayer(existingRequest.sender());
                    break;
                }
            }

            if (target == null) {
                player.sendMessage(ChatColor.RED + "You do not have any teleport requests.");
                return true;
            }
        }

        TeleportRequest teleportRequest = new TeleportRequest(target.getUniqueId(), player.getUniqueId());

        if (teleportRequests.contains(teleportRequest)) {
            if (response) {
                target.sendMessage(String.format("%s" + ChatColor.YELLOW + " accepted your teleport request.",
                        player.getDisplayName()));
                player.sendMessage(String.format(
                        ChatColor.YELLOW + "Teleport request from %s" + ChatColor.RESET + ChatColor.YELLOW + " accepted.",
                        target.getDisplayName()));
                target.teleport(player);
            } else {
                target.sendMessage(String.format("%s" + ChatColor.YELLOW + " denied your teleport request.",
                        player.getDisplayName()));
                player.sendMessage(String.format(
                        ChatColor.YELLOW + "Teleport request from %s" + ChatColor.RESET + ChatColor.YELLOW + " denied.",
                        target.getDisplayName()));
            }

            teleportRequests.remove(teleportRequest);
        } else {
            player.sendMessage(ChatColor.RED + "You do not have a teleport request from this player.");
        }

        return true;
    }

    private boolean onTpToggle(@NotNull Player player) {
        PlayerData playerData = dataController.getPlayerData(player.getUniqueId());
        playerData.setTpToggle(!playerData.isTpToggle());

        if (playerData.isTpToggle()) {
            player.sendMessage(ChatColor.YELLOW + "Teleport requests disabled.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "Teleport requests enabled.");
        }

        dataController.savePlayerData(player.getUniqueId());

        return true;
    }
}
