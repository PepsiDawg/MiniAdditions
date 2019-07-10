package io.github.pepsidog.miniadditions;

import com.google.common.collect.Lists;
import io.github.pepsidog.miniadditions.additions.armorstands.ArmorStandAdditions;
import io.github.pepsidog.miniadditions.additions.autostack.AutoStackListener;
import io.github.pepsidog.miniadditions.additions.biomebombs.BiomeBombListener;
import io.github.pepsidog.miniadditions.additions.chatitem.ChatItemListener;
import io.github.pepsidog.miniadditions.additions.cobblegenerator.CobbleGeneratorListener;
import io.github.pepsidog.miniadditions.additions.cobblegenerator.CobbleGeneratorManager;
import io.github.pepsidog.miniadditions.additions.compressedmobs.CompressedMobsListener;
import io.github.pepsidog.miniadditions.additions.concretemixer.ConcreteMixerListener;
import io.github.pepsidog.miniadditions.additions.craftingkeeper.CraftingKeeperListener;
import io.github.pepsidog.miniadditions.additions.craftingkeeper.CraftingKeeperManager;
import io.github.pepsidog.miniadditions.additions.creeperworks.CreeperworksListener;
import io.github.pepsidog.miniadditions.additions.easysleep.EasySleepListener;
import io.github.pepsidog.miniadditions.additions.experimental.ExperimentalCommands;
import io.github.pepsidog.miniadditions.additions.experimental.SoundSynthExperiment;
import io.github.pepsidog.miniadditions.additions.slimyboots.SlimyBootsListener;
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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
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

        ArrayList<String> names = Lists.newArrayList(
                "ArmorStands",          "AutoStack",       "BiomeBombs",
                "ChatItem",             "CobbleGenerator", "CompressedMobs",
                "ConcreteMixer",        "CraftingKeeper",  "Creeperworks",
                "EasyPaintings",        "EasySleep",       "Experimental",
                "ExperimentalCommands", "IgneousGenerator","ImprovedShears",
                "NamePing",             "SlimyBoots",      "WoodPile"
        );
        ConfigManager.Initialize(this, names);
        for (String name : names) {
            YamlConfiguration config = ConfigManager.getConfig(name);
            if (config == null || config.getBoolean("enabled")) {
                try {
                    this.getClass().getMethod("init" + name).invoke(this);
                } catch (NoSuchMethodException | InvocationTargetException ex) {
                    this.getLogger().severe("Could not find method for initialization: " + name);
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    this.getLogger().severe("Could not access method for initialization: " + name);
                }
            }
        }

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

    public void initArmorStands() {
        this.getServer().getPluginManager().registerEvents(new ArmorStandAdditions(), this);
        this.getLogger().info("Armor Stands enabled");
    }

    public void initAutoStack() {
        this.getServer().getPluginManager().registerEvents(new AutoStackListener(), this);
        this.getLogger().info("Auto Stack enabled");
    }

    public void initBiomeBombs() {
        BiomeBombListener.initRecipes();
        this.getLogger().info("Biome Bombs enabled");
    }

    public void initChatItem() {
        this.getServer().getPluginManager().registerEvents(new ChatItemListener(), this);
        this.getLogger().info("Chat Item enabled");
    }

    public void initCobbleGenerator() {
        YamlConfiguration config = ConfigManager.getConfig("cobblegenerator");
        if (config != null) {
            if (config.isConfigurationSection("blocks")) {
                CobbleGeneratorManager.loadConfig(config.getConfigurationSection("blocks"));

                this.getServer().getPluginManager().registerEvents(new CobbleGeneratorListener(), this);
                this.getLogger().info("Cobble Generator enabled");
            }
        }
    }

    public void initCompressedMobs() {
        this.getServer().getPluginManager().registerEvents(new CompressedMobsListener(this), this);
        this.getLogger().info("Compressed Mobs enabled");
    }

    public void initConcreteMixer() {
        YamlConfiguration config = ConfigManager.getConfig("concretemixer");
        if (config != null) {
            int useChance = config.getInt("water-use-chance", 5);
            this.getServer().getPluginManager().registerEvents(new ConcreteMixerListener(useChance), this);
            this.getLogger().info("Concrete Mixer enabled");
        }
    }

    public void initCraftingKeeper() {
        ConfigurationSerialization.registerClass(CraftingKeeperManager.class, "CraftingKeeperManager");
        this.getServer().getPluginManager().registerEvents(new CraftingKeeperListener(), this);
        this.getLogger().info("Crafting Keeper enabled");
    }

    public void initCreeperworks() {
        this.getServer().getPluginManager().registerEvents(new CreeperworksListener(), this);
        this.getLogger().info("Creeperworks enabled");
    }

    public void initEasyPaintings() {
        this.getServer().getPluginManager().registerEvents(new EasyPaintings(), this);
        this.getLogger().info("Easy Paintings enabled");
    }

    public void initEasySleep() {
        YamlConfiguration config = ConfigManager.getConfig("easysleep");
        double threshold = 25;
        if (config != null) {
            threshold = config.getInt("threshold");
        }
        getServer().getPluginManager().registerEvents(new EasySleepListener(threshold), this);
        this.getLogger().info("Easy Sleeping enabled");

    }

    public void initExperimental() {
        this.getCommand("synth").setExecutor(new SoundSynthExperiment());
        this.getLogger().info("Sound Synth enabled");

    }

    public void initExperimentalCommands() {
        this.getCommand("shoot").setExecutor(new ExperimentalCommands());
        this.getLogger().info("Experimental Commands enabled");
    }

    public void initIgneousGenerator() {
        this.getServer().getPluginManager().registerEvents(new IgneousGeneratorListener(), this);
        this.getLogger().info("Igneous Generator enabled");
    }

    public void initImprovedShears() {
        YamlConfiguration config = ConfigManager.getConfig("improvedshears");
        int chance = 25;
        if (config != null) {
            chance = config.getInt("dye-chance");
        }

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

        this.getServer().getPluginManager().registerEvents(new ShearListener(chance), this);
        this.getLogger().info("Improved Shears enabled");
    }

    public void initNamePing() {
        YamlConfiguration config = ConfigManager.getConfig("nameping");
        int chance = 5;
        if (config != null) {
            chance = config.getInt("ping-cooldown");
        }

        this.getServer().getPluginManager().registerEvents(new NamePing(chance), this);
        this.getLogger().info("Name Ping enabled");
    }

    public void initSlimyBoots() {
        SlimyBootsListener.initRecipies();
        this.getServer().getPluginManager().registerEvents(new SlimyBootsListener(), this);
        this.getLogger().info("Slimy Boots enabled");
    }

    public void initWoodPile() {
        YamlConfiguration config = ConfigManager.getConfig("woodpile");
        int convertTime = 5;
        if (config != null) {
            config.getInt("log-convert-time");
        }

        this.getServer().getPluginManager().registerEvents(new WoodPileListener(convertTime), this);
        this.getLogger().info("Wood Pile enabled");
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