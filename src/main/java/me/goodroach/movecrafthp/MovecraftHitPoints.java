package me.goodroach.movecrafthp;

import me.goodroach.movecrafthp.commands.HitPointCommands;
import me.goodroach.movecrafthp.config.ConfigManager;
import me.goodroach.movecrafthp.config.Keys;
import me.goodroach.movecrafthp.hitpoints.HitPointManager;
import me.goodroach.movecrafthp.listeners.DamageListener;
import me.goodroach.movecrafthp.listeners.MovecraftListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MovecraftHitPoints extends JavaPlugin {
    private static MovecraftHitPoints instance;
    private HitPointManager manager;
    private ConfigManager configManager;
    @Override
    public void onLoad () {
        Keys.register();
    }
    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new MovecraftListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);

        configManager = new ConfigManager();
        configManager.reloadConfig();
        saveDefaultConfig();

        this.manager = new HitPointManager();

        getCommand("hitpoint").setExecutor(new HitPointCommands());
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MovecraftHitPoints getInstance () {
        return instance;
    }
    public HitPointManager getHitPointManager() {
        return manager;
    }

    public ConfigManager getConfigManager() {return configManager;}
}
