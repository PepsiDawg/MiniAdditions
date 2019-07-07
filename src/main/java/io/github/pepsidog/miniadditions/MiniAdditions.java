package io.github.pepsidog.miniadditions;

import io.github.pepsidog.miniadditions.additions.armorstands.ArmorStandAdditions;
import io.github.pepsidog.miniadditions.additions.biomebombs.BiomeBombListener;
import io.github.pepsidog.miniadditions.additions.chatitem.ChatItemListener;
import io.github.pepsidog.miniadditions.additions.cobblegenerator.CobbleGeneratorListener;
import io.github.pepsidog.miniadditions.additions.cobblegenerator.CobbleGeneratorManager;
import io.github.pepsidog.miniadditions.additions.concretemixer.ConcreteMixerListener;
import io.github.pepsidog.miniadditions.additions.craftingkeeper.CraftingKeeperListener;
import io.github.pepsidog.miniadditions.additions.craftingkeeper.CraftingKeeperManager;
import io.github.pepsidog.miniadditions.utils.custommeta.CustomMeta;
import io.github.pepsidog.miniadditions.utils.custommeta.MetaHandler;
import io.github.pepsidog.miniadditions.additions.easypaintings.EasyPaintings;
import io.github.pepsidog.miniadditions.additions.igneousgenerator.IgneousGeneratorListener;
import io.github.pepsidog.miniadditions.additions.improvedshears.ShearListener;
import io.github.pepsidog.miniadditions.additions.nameping.NamePing;
import io.github.pepsidog.miniadditions.utils.CraftingUtil;
import io.github.pepsidog.miniadditions.utils.ItemBuilder;
import io.github.pepsidog.miniadditions.utils.StringHelper;
import io.github.pepsidog.miniadditions.additions.woodpile.WoodPileListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class MiniAdditions extends JavaPlugin {
    private static MiniAdditions self;
    private static Random rand;
    private static MetaHandler metaHandler = null;

    @Override
    public void onEnable() {
        self = this;
        rand = new Random();
        metaHandler = CustomMeta.getHandler(this);

        saveDefaultConfig();

        initCobbleGen();
        initIgneousGenerator();
        initConcreteMixer();
        initCraftingKeeper();
        initWoodPile();
        initImprovedShears();
        initChatItem();
        initNamePing();
        initArmorStandAdditions();
        //initEasyPaintings();

        BiomeBombListener.initRecipes();

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

    private void initIgneousGenerator() {
        getServer().getPluginManager().registerEvents(new IgneousGeneratorListener(), this);
    }

    private void initConcreteMixer() {
        int useChance = getConfig().getInt("concrete-water-use-chance", 5);
        getServer().getPluginManager().registerEvents(new ConcreteMixerListener(useChance), this);
    }

    private void initArmorStandAdditions() {
        getServer().getPluginManager().registerEvents(new ArmorStandAdditions(), this);
    }

    private void initNamePing() {
        int pingChance = getConfig().getInt("name-ping-cooldown", 5);
        getServer().getPluginManager().registerEvents(new NamePing(pingChance), this);
    }

    private void initEasyPaintings() {
        getServer().getPluginManager().registerEvents(new EasyPaintings(), this);
    }

    private void initChatItem() {
        getServer().getPluginManager().registerEvents(new ChatItemListener(), this);
    }

    private void initCraftingKeeper() {
        ConfigurationSerialization.registerClass(CraftingKeeperManager.class, "CraftingKeeperManager");
        getServer().getPluginManager().registerEvents(new CraftingKeeperListener(), this);
    }

    private void initWoodPile() {
        int convertTime = getConfig().getInt("log-convert-time", 5);
        getServer().getPluginManager().registerEvents(new WoodPileListener(convertTime), this);
    }

    private void initImprovedShears() {
        int chance = getConfig().getInt("dye-chance", 25);
        Map<Material, Integer> ingredients = new HashMap<Material, Integer>() {{
            put(Material.SHEARS, 2);
            put(Material.DIAMOND, 1);
        }};
        ItemStack result = new ItemBuilder(Material.SHEARS)
                                .setName(StringHelper.rainbowfy("Improved Shears"))
                                .addLore(ChatColor.GOLD + "Has a " + chance + "% chance to drop dye instead of wool!")
                                .build();

        result = metaHandler.setKey(result, "improved-shears", "true");
        CraftingUtil.addShapelessCrafting("Improved_Shears", ingredients, result);

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