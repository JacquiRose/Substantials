package au.id.jrose.substantials;

import au.id.jrose.substantials.commands.*;
import au.id.jrose.substantials.listeners.PlayerListener;
import au.id.jrose.substantials.listeners.combat.ExplosionParry;
import au.id.jrose.substantials.listeners.combat.HugeDamage;
import au.id.jrose.substantials.listeners.combat.Parry;
import au.id.jrose.substantials.listeners.combat.Uppercut;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
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

        TpAskCommandsExecutor tpAskCommandsExecutor = new TpAskCommandsExecutor(playerDataController);
        this.getCommand("tpask").setExecutor(tpAskCommandsExecutor);
        this.getCommand("tpaccept").setExecutor(tpAskCommandsExecutor);
        this.getCommand("tpdeny").setExecutor(tpAskCommandsExecutor);
        this.getCommand("tptoggle").setExecutor(tpAskCommandsExecutor);

        ShowVillagersCommandExecutor showVillagersCommandExecutor = new ShowVillagersCommandExecutor();
        this.getCommand("showvillagers").setExecutor(showVillagersCommandExecutor);


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

        addEnchantedGoldenAppleRecipe();
    }

    @Override
    public void onDisable() {
    }

    private void addEnchantedGoldenAppleRecipe() {
        ItemStack enchantedGoldenApple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        NamespacedKey key = new NamespacedKey(this, "substantials_enchanted_golden_apple");
        ShapedRecipe recipe = new ShapedRecipe(key, enchantedGoldenApple);
        recipe.shape("GGG", "GAG", "GGG");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('A', Material.APPLE);
        this.getServer().addRecipe(recipe);
    }
}
