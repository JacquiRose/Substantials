package au.id.jrose.substantials.listeners.combat;

import au.id.jrose.substantials.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Uppercut implements Listener {

    @NotNull
    private final Main main;

    public Uppercut(@NotNull Main main) {
        this.main = main;
    }

    @EventHandler
    public void onUppercut(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (entity instanceof Player) return;
        if (player.getVelocity().getY() <= 0) return;

        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.25F, 1.25F);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(ChatColor.GREEN + "** UPPERCUT **"));
        new BukkitRunnable() {
            @Override
            public void run() {
                entity.setVelocity(new Vector(entity.getVelocity().getX(),
                                              entity.getVelocity().getY() * 2.85,
                                              entity.getVelocity().getZ()));
            }
        }.runTask(main);
    }
}
