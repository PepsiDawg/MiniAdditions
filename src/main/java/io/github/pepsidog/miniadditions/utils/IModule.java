package io.github.pepsidog.miniadditions.utils;


import org.bukkit.configuration.file.YamlConfiguration;

public interface IModule {
    public void init(YamlConfiguration config);
    public String getName();
}
