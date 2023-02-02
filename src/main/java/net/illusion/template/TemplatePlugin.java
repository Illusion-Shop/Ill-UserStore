package net.illusion.template;

import net.illusion.core.data.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class TemplatePlugin extends JavaPlugin {

    private final Logger log = Bukkit.getLogger();
    private static TemplatePlugin plugin;
    public static Config config;

    @Override
    public void onEnable() {
        // DEPENDENCY
        if (getServer().getPluginManager().getPlugin("Ill-Core") != null) {
            log.warning("적용뭐시기 하세연");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // CONFIG
        plugin = this;
        config = new Config("config");
        config.setPlugin(this);
        config.loadDefualtConfig();

        // EVENT
        // TODO


        // COMMAND
        // TODO
    }

    @Override
    public void onDisable() {
        // TODO
    }

    public static TemplatePlugin getPlugin() {
        return plugin;
    }
}
