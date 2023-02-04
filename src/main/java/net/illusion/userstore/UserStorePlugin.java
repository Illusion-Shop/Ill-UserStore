package net.illusion.userstore;

import net.illusion.core.data.Config;
import net.illusion.userstore.command.UserStoreCommand;
import net.illusion.userstore.event.UserStoreListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class UserStorePlugin extends JavaPlugin {

    private final Logger log = Bukkit.getLogger();
    private static UserStorePlugin plugin;
    public static Config config, store;


    @Override
    public void onEnable() {
        // DEPENDENCY
        if (getServer().getPluginManager().getPlugin("Ill-Core") != null) {
            log.warning("[ " + Bukkit.getName() + "] Ill-Core 플러그인이 적용되지 않아 비활성화 됩니다.");
            log.warning("[ " + Bukkit.getName() + "] 다운로드 링크: https://discord.gg/illusion-shop");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // CONFIG
        plugin = this;
        config = new Config("config");
        config.setPlugin(this);
        config.loadDefualtConfig();
        store = new Config("store");
        store.setPlugin(this);

        // EVENT
        // TODO
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new UserStoreListener(), this);
        // COMMAND
        getCommand("유저상점").setExecutor(new UserStoreCommand());
    }


    public static UserStorePlugin getPlugin() {
        return plugin;
    }
}
