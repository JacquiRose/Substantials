package au.id.jrose.substantials.listeners.combat;

import au.id.jrose.substantials.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class Parry implements Listener {

    @NotNull
    private final Main main;
    @NotNull
    private final HashMap<UUID, Double> parryMap;

    public Parry(@NotNull Main main) {
        this.main = main;
        parryMap = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onParrySetup(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

        if (parryMap.containsKey(player.getUniqueId())) {
            parryMap.remove(player.getUniqueId());
            player.playSound(event.getEntity().getLocation(), Sound.ENTITY_BAT_HURT, 2F, 2F);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                        TextComponent.fromLegacyText(ChatColor.RED + "** PARRY FAIL **"));
            return;
        }

        if (!player.isBlocking()) return;

        parryMap.put(player.getUniqueId(), event.getDamage());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!parryMap.containsKey(player.getUniqueId())) return;
                parryMap.remove(event.getEntity().getUniqueId());
                player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 0.5F, 2F);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                            TextComponent.fromLegacyText(ChatColor.RED + "** PARRY FAIL **"));
            }
        }.runTaskLater(main, 15L);
    }

    @EventHandler
    public void onParry(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (event.getEntity() instanceof Player) return;

        if (parryMap.containsKey(player.getUniqueId())) {
            double damage = parryMap.remove(player.getUniqueId());
            entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 2F, 2F);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                        TextComponent.fromLegacyText(ChatColor.GREEN + "** PARRY **"));
            entity.setVelocity(player.getLocation().getDirection().setY(0).normalize().multiply(5));
            event.setDamage(event.getDamage() + damage);
        }
    }
}
