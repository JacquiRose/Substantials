package au.id.jrose.substantials;

import au.id.jrose.substantials.commands.DelHomeCommandExecutor;
import au.id.jrose.substantials.commands.HomeCommandExecutor;
import au.id.jrose.substantials.commands.SetHomeCommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        PlayerDataController playerDataController = new PlayerDataController(this.getDataFolder(), this.getLogger());

        HomeCommandExecutor homeCommandExecutor = new HomeCommandExecutor(playerDataController);
        this.getCommand("home").setExecutor(homeCommandExecutor);

        SetHomeCommandExecutor setHomeCommandExecutor = new SetHomeCommandExecutor(playerDataController);
        this.getCommand("sethome").setExecutor(setHomeCommandExecutor);

        DelHomeCommandExecutor delHomeCommandExecutor = new DelHomeCommandExecutor(playerDataController);
        this.getCommand("delhome").setExecutor(delHomeCommandExecutor);

        //TODO: Nickname command executor?

        PlayerListener playerListener = new PlayerListener(playerDataController);
        this.getServer().getPluginManager().registerEvents(playerListener, this);
    }

    @Override
    public void onDisable() {
    }
}
