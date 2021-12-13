package au.id.jrose.substantials;

import au.id.jrose.substantials.commands.*;
import au.id.jrose.substantials.listeners.PlayerListener;
import au.id.jrose.substantials.listeners.combat.ExplosionParry;
import au.id.jrose.substantials.listeners.combat.HugeDamage;
import au.id.jrose.substantials.listeners.combat.Parry;
import au.id.jrose.substantials.listeners.combat.Uppercut;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        PlayerDataController playerDataController = new PlayerDataController(this.getDataFolder(), this.getLogger());

        HomeCommandExecutor homeCommandExecutor = new HomeCommandExecutor(playerDataController);
        PluginCommand homeCommand = this.getCommand("home");
        assert homeCommand != null;
        homeCommand.setExecutor(homeCommandExecutor);
        homeCommand.setTabCompleter(homeCommandExecutor);

        SetHomeCommandExecutor setHomeCommandExecutor = new SetHomeCommandExecutor(playerDataController);
        this.getCommand("sethome").setExecutor(setHomeCommandExecutor);

        DelHomeCommandExecutor delHomeCommandExecutor = new DelHomeCommandExecutor(playerDataController);
        PluginCommand delHomeCommand = this.getCommand("delhome");
        assert delHomeCommand != null;
        delHomeCommand.setExecutor(delHomeCommandExecutor);
        delHomeCommand.setTabCompleter(delHomeCommandExecutor);

        HomesCommandExecutor homesCommandExecutor = new HomesCommandExecutor(playerDataController);
        this.getCommand("homes").setExecutor(homesCommandExecutor);

        NicknameCommandExecutor nicknameCommandExecutor = new NicknameCommandExecutor(playerDataController);
        this.getCommand("nickname").setExecutor(nicknameCommandExecutor);

        PlayerListener playerListener = new PlayerListener(playerDataController);
        this.getServer().getPluginManager().registerEvents(playerListener, this);

        HugeDamage hugeDamage = new HugeDamage(this);
        this.getServer().getPluginManager().registerEvents(hugeDamage, this);

        ExplosionParry explosionParry = new ExplosionParry(this);
        this.getServer().getPluginManager().registerEvents(explosionParry, this);

        Parry parry = new Parry(this);
        this.getServer().getPluginManager().registerEvents(parry, this);

        Uppercut uppercut = new Uppercut(this);
        this.getServer().getPluginManager().registerEvents(uppercut, this);

        for (Player player : this.getServer().getOnlinePlayers()) {
            playerDataController.loadPlayerData(player);
        }
    }

    @Override
    public void onDisable() {
    }
}
