package io.github.pepsidog.miniadditions.utils.custommeta;

import org.bukkit.plugin.java.JavaPlugin;

public class CustomMeta {
    public static MetaHandler getHandler(JavaPlugin plugin) {
        return new NBTHandler(plugin);
    }
}