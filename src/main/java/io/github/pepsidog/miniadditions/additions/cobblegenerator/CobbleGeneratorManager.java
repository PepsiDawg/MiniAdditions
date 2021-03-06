package io.github.pepsidog.miniadditions.additions.cobblegenerator;

import io.github.pepsidog.miniadditions.MiniAdditions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CobbleGeneratorManager {
    private static CobbleGeneratorManager self;
    private final Map<Material, Double> materials;

    private CobbleGeneratorManager() {
        this.materials = new HashMap<>();
    }

    void addMaterial(Material material, double weight) {
        this.materials.put(material, weight);
    }

    Material generateBlock() {
        if (this.materials.size() == 0) {
            return Material.COBBLESTONE;
        }

        List<Double> weights = new ArrayList<>(materials.values());
        double total = weights.stream().mapToDouble(Double::doubleValue).sum();

        double randValue = MiniAdditions.getRandom().nextDouble() * total;
        List<Material> mats = new ArrayList<>(this.materials.keySet());

        for (int index = 0; index < mats.size(); index++) {
            if (randValue < weights.get(index)) {
                return mats.get(index);
            }
            randValue -= weights.get(index);
        }

        return mats.get(mats.size() - 1);
    }

    public static CobbleGeneratorManager getInstance() {
        if (self == null) {
            self = new CobbleGeneratorManager();
        }
        return self;
    }

    public static void loadConfig(ConfigurationSection section) {
        CobbleGeneratorManager manager = getInstance();

        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            try {
                Material mat = Material.valueOf(key.toUpperCase());
                double weight = section.getDouble(key);
                manager.addMaterial(mat, weight);
            } catch (Exception e) {
                Bukkit.getLogger().warning("Invalid block option! (" + e.getMessage() + ")");
            }
        }
    }
}
