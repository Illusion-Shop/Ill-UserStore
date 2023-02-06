package net.illusion.userstore;

import net.illusion.core.data.Config;
import net.illusion.core.util.text.Gradient;

import net.illusion.userstore.command.UserStoreCommand;
import net.illusion.userstore.command.tabcomplete.UserStoreTab;
import net.illusion.userstore.event.UserStoreListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.logging.Logger;

public class UserStorePlugin extends JavaPlugin {

    private final Logger log = Bukkit.getLogger();
    private static UserStorePlugin plugin;

    private static Economy econ = null;

    public static Config config, store;

    public static String prefix;

    @Override
    public void onEnable() {
        // DEPENDENCY
        if (!setupEconomy()) {
            log.warning("[ " + Bukkit.getName() + "] Vault 플러그인이 적용되지 않아 비활성화 됩니다.");
            log.warning("[ " + Bukkit.getName() + "] 다운로드 링크: https://www.spigotmc.org/resources/vault.34315/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (getServer().getPluginManager().getPlugin("Ill-Core") != null) {
            log.warning("[ " + Bukkit.getName() + "] Ill-Core 플러그인이 적용되지 않아 비활성화 됩니다.");
            log.warning("[ " + Bukkit.getName() + "] 다운로드 링크: https://discord.gg/illusion-shop");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }


        // CONFIG
        plugin = this;

        config = new Config("config");
        config.setPlugin(this);
        config.loadDefualtConfig();

        store = new Config("store");
        store.setPlugin(this);
        // EVENT
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new UserStoreListener(), this);
        // COMMAND
        getCommand("유저상점").setExecutor(new UserStoreCommand());
        getCommand("유저상점").setTabCompleter(new UserStoreTab());

        prefix = ChatColor.translateAlternateColorCodes('&',
                UserStorePlugin.config.getConfig().getString("prefix"));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }

    public static UserStorePlugin getPlugin() {
        return plugin;
    }
}
