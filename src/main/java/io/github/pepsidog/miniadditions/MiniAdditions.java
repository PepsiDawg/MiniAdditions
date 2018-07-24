package io.github.pepsidog.miniadditions;

import io.github.pepsidog.custommeta.CustomMeta;
import io.github.pepsidog.custommeta.nbt.MetaHandler;
import io.github.pepsidog.miniadditions.CobbleGenerator.CobbleGeneratorListener;
import io.github.pepsidog.miniadditions.CobbleGenerator.CobbleGeneratorManager;
import io.github.pepsidog.miniadditions.CraftingKeeper.CraftingKeeperListener;
import io.github.pepsidog.miniadditions.CraftingKeeper.CraftingKeeperManager;
import io.github.pepsidog.miniadditions.ImprovedShears.ShearListener;
import io.github.pepsidog.miniadditions.Utils.StringHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

public class MiniAdditions extends JavaPlugin {
    private static MiniAdditions self;
    private static Random rand;
    private static MetaHandler metaHandler = null;
    private static NamespacedKey namespace;

    @Override
    public void onEnable() {
        self = this;
        rand = new Random();
        namespace = new NamespacedKey(this, "miniAdditions");
        try {
            metaHandler = CustomMeta.getHandler(this);
        } catch (Exception e) { getLogger().info("Could not find support for custom meta"); }

        saveDefaultConfig();

        initCobbleGen();
        initCraftingKeeper();
        if(metaHandler != null) { initImprovedShears(); }

        loadCrafting();
    }

    @Override
    public void onDisable() {
        saveCrafting();
    }

    public static MiniAdditions getInstance() {
        return self;
    }

    public static Random getRandom() {
        return rand;
    }

    public static MetaHandler getMetaHandler() { return metaHandler; }

    private void initCobbleGen() {
        ConfigurationSection section = null;
        if(getConfig().isConfigurationSection("cobblegen.blocks")) {
            section = getConfig().getConfigurationSection("cobblegen.blocks");
        }

        CobbleGeneratorManager.loadConfig(section);
        getServer().getPluginManager().registerEvents(new CobbleGeneratorListener(), this);
    }

    private void initCraftingKeeper() {
        ConfigurationSerialization.registerClass(CraftingKeeperManager.class, "CraftingKeeperManager");
        getServer().getPluginManager().registerEvents(new CraftingKeeperListener(), this);
    }

    private void initImprovedShears() {
        ShapelessRecipe recipe;
        ItemStack result = new ItemStack(Material.SHEARS);
        ItemMeta meta = result.getItemMeta();
        int chance = getConfig().getInt("dye-chance", 25);

        meta.setDisplayName(StringHelper.rainbowfy("Improved Shears"));
        meta.setLore(Arrays.asList(ChatColor.GOLD + "Has a " + chance + "% chance to drop dye instead of wool!"));
        result.setItemMeta(meta);
        result = metaHandler.setKey(result, "improved-shears", "true");

        recipe = new ShapelessRecipe(namespace, result);
        recipe.addIngredient(Material.SHEARS);
        recipe.addIngredient(Material.SHEARS);
        recipe.addIngredient(Material.DIAMOND);

        getServer().addRecipe(recipe);
        getServer().getPluginManager().registerEvents(new ShearListener(chance), this);
    }

    private void saveCrafting() {
        CraftingKeeperManager manager = CraftingKeeperManager.getInstance();
        FileConfiguration config = new YamlConfiguration();

        config.set("tables", manager);
        try {
            config.save(new File(getDataFolder(), "crafting_tables.yml"));
        } catch (Exception e) {
            getLogger().warning("Error saving crafting tables!");
        }
    }

    private void loadCrafting() {
        try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "crafting_tables.yml"));
            CraftingKeeperManager manager = (CraftingKeeperManager) config.get("tables");
        } catch (Exception e) {
            getLogger().warning("Error loading crafting tables!");
            e.printStackTrace();
        }
    }
}