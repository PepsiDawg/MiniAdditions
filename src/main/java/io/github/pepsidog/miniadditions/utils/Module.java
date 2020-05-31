package io.github.pepsidog.miniadditions.utils;

import io.github.pepsidog.miniadditions.MiniAdditions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

public class Module implements IModule, Listener {
    private String name;

    public Module(String name) {
        this.name = name;
    }

    @Override
    public void init(YamlConfiguration config) {
        if(config != null && config.getBoolean("enabled")) {
            Bukkit.getLogger().info(this.getName() + " Initialized!");
            Bukkit.getServer().getPluginManager().registerEvents(this, MiniAdditions.getInstance());
        } else {
            Bukkit.getLogger().info(this.getName() + " Disabled!");
        }
    }

    @Override
    public String getName() {
        return this.name;
    }
}
