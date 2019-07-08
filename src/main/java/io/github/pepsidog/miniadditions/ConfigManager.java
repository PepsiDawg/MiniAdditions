package io.github.pepsidog.miniadditions;

import com.google.common.collect.Lists;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigManager {
    private static JavaPlugin plugin = null;
    private static ArrayList<String> configNames = Lists.newArrayList();
    private static HashMap<String, YamlConfiguration> configs = new HashMap<>();

    public static void Initialize(JavaPlugin plg, ArrayList<String> names) {
        Initialize(plg, names, true);
    }

    public static void Initialize(JavaPlugin plg, ArrayList<String> names, boolean saveConfigs) {
        plugin = plg;
        configNames = names;

        if (saveConfigs) {
            saveDefaultConfigs();
        }
    }

    public static void saveDefaultConfigs() {
        File directory = new File(plugin.getDataFolder().getAbsolutePath() + "/configs/");
        if (!directory.exists()) {
            directory.mkdir();
        }

        for (String name : configNames) {
            name = name.toLowerCase() + ".yml";

            File file = new File(directory, name);
            if (!file.exists()) {
                try {
                    plugin.saveResource("configs/" + name, false);
                    plugin.getLogger().info("Creating missing config '" + name + "'");
                } catch (IllegalArgumentException ex) {
                    plugin.getLogger().severe("Could not find YML file '" + name + "' in plugin jar!");
                    continue;
                }
            }

            YamlConfiguration config = new YamlConfiguration();
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException ex) {
                plugin.getLogger().severe("An error occurred while loading config '" + name + "'");
                ex.printStackTrace();
            }

            configs.put(name.substring(0, name.length() - 4), config);
        }
    }

    public static YamlConfiguration getConfig(String name) {
        return configs.getOrDefault(name, null);
    }

    public static void saveConfig(YamlConfiguration config) {
        try {
            config.save(new File(config.getName()));
        } catch (IOException ex) {
            plugin.getLogger().severe("An error occurred while saving config '" + config.getName() + "'");
            ex.printStackTrace();
        }
    }

    public static void saveAllConfigs() {
        for (YamlConfiguration config : configs.values()) {
            saveConfig(config);
        }
    }
}
