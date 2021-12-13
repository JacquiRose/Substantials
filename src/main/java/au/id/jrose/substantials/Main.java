package au.id.jrose.substantials;

import au.id.jrose.substantials.commands.*;
import org.bukkit.command.Command;
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

        for (Player player : this.getServer().getOnlinePlayers()) {
            playerDataController.loadPlayerData(player);
        }
    }

    @Override
    public void onDisable() {
    }
}
