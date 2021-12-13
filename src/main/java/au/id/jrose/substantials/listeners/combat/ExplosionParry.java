package au.id.jrose.substantials.listeners.combat;

import au.id.jrose.substantials.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class ExplosionParry implements Listener {

    @NotNull
    private final Main main;

    public ExplosionParry(@NotNull Main main) {
        this.main = main;
    }

    @EventHandler
    public void onExplosionParry(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return;
        if (!player.isBlocking()) return;

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(ChatColor.GREEN + "** DEFLECT **"));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1F, 1.175F);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5F, 0.75F);
                for (Entity nearby : event.getEntity().getNearbyEntities(10, 5, 10))
                    if (nearby instanceof Monster monster) {
                        monster.damage(event.getDamage() * 2);
                        monster.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, monster.getLocation(), 2);
                    }
            }
        }.runTaskLater(main, 8L);
    }
}