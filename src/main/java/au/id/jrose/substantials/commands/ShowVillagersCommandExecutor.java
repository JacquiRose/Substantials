package au.id.jrose.substantials.commands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class ShowVillagersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        int found = 0;

        for (Entity entity : player.getNearbyEntities(100, 100, 100)) {
            if (entity instanceof Villager villager) {
                villager.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 0));
                found++;
            }
        }

        if (found > 0) player.playSound(player.getLocation(), Sound.BLOCK_BELL_RESONATE, 1L, 1L);
        else player.sendMessage(ChatColor.RED + "No villagers found nearby.");

        return true;
    }
}
