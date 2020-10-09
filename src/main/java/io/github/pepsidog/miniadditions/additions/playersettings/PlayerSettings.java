package io.github.pepsidog.miniadditions.additions.playersettings;

import io.github.pepsidog.miniadditions.MiniAdditions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerSettings {
    static PlayerSettings self;
    private final YamlConfiguration config;
    private boolean enabled = true;
    private ConfigurationSection defaultValues;
    private Map<UUID, ConfigurationSection> playerValues;

    private PlayerSettings() {
        config = MiniAdditions.getInstance().getConfigManager().getConfig("player-settings");

        if(config == null) {
            this.enabled = false;
            return;
        }

        defaultValues = config.getConfigurationSection("settings.defaults");

        Set<String> playerKeys = config.getConfigurationSection("settings.players").getKeys(false);
        playerValues = new HashMap<>();
        for(String key : playerKeys) {
            playerValues.put(UUID.fromString(key), config.getConfigurationSection("settings.players." + key));
        }
    }

    public static PlayerSettings getInstance() {
        if(self == null) {
            self = new PlayerSettings();
        }
        return self;
    }

    public Object getSettingValue(String key, Player player) {
        UUID uuid = player.getUniqueId();

        if(!this.enabled) { return null; }
        if(!playerValues.containsKey(uuid)) {
            playerValues.put(uuid, defaultValues);
        }

        return playerValues.get(uuid).get(key);
    }

    public void setSettingValue(String key, Object value, Player player) {
        playerValues.get(player.getUniqueId()).set(key, value);
    }

    public void saveSettings() {
        for(UUID uuid : playerValues.keySet()) {
            config.set("settings.players." + uuid, playerValues.get(uuid));
            MiniAdditions.getInstance().getConfigManager().saveConfig("player-settings");
        }
    }
}
