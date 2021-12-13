package au.id.jrose.substantials.listeners.combat;

import au.id.jrose.substantials.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class HugeDamage implements Listener {

    @NotNull
    private final HashMap<UUID, Integer> rageMap;

    public HugeDamage(@NotNull Main main) {
        rageMap = new HashMap<>();

        // This task will continually deplete rage over time.
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!rageMap.isEmpty())
                    for (UUID next : rageMap.keySet()) {
                        int rage = rageMap.get(next);
                        if (rage == 1)
                            rageMap.remove(next);
                        else
                            rageMap.put(next, rage - 1);
                    }
            }
        }.runTaskTimer(main, 0L, 20L);
    }

    @EventHandler
    public void onHugeDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.getEntity() instanceof Player) return;

        // Rage is reset if the player attacks without their fist.
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            rageMap.remove(event.getDamager().getUniqueId());
        }

        Location eye = ((LivingEntity) event.getEntity()).getEyeLocation();
        // Check combo.

        int rage = rageMap.getOrDefault(player.getUniqueId(), 0);

        player.playSound(eye, Sound.BLOCK_NOTE_BLOCK_BASS, 2F, 1F + (rage / 4F));

        if (rage < 4) {
            rageMap.put(player.getUniqueId(), rage + 1);
            return;
        }

        rageMap.remove(player.getUniqueId());
        player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, eye, 1);
        player.getWorld().playSound(eye, Sound.ENTITY_GENERIC_EXPLODE, 2F, 1.1F);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(ChatColor.GREEN + "** HUGE DAMAGE **"));
    }
}
