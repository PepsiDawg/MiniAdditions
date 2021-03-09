package io.github.pepsidog.miniadditions;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.ConfigManager;
import io.github.pepsidog.miniadditions.additions.armorstands.ArmorStandAdditions;
import io.github.pepsidog.miniadditions.additions.cobblegenerator.CobbleGeneratorListener;
import io.github.pepsidog.miniadditions.additions.concretemixer.ConcreteMixerListener;
import io.github.pepsidog.miniadditions.additions.craftingkeeper.CraftingKeeperListener;
import io.github.pepsidog.miniadditions.additions.craftingkeeper.CraftingKeeperManager;
import io.github.pepsidog.miniadditions.additions.easypaintings.EasyPaintings;
import io.github.pepsidog.miniadditions.additions.easysleep.EasySleepListener;
import io.github.pepsidog.miniadditions.additions.experimental.ExperimentalCommands;
import io.github.pepsidog.miniadditions.additions.experimental.SoundSynthExperiment;
import io.github.pepsidog.miniadditions.additions.featherplucker.FeatherPlucker;
import io.github.pepsidog.miniadditions.additions.igneousgenerator.IgneousGeneratorListener;
import io.github.pepsidog.miniadditions.additions.improvedshears.ShearListener;
import io.github.pepsidog.miniadditions.additions.inventoryinspector.InventoryInspector;
import io.github.pepsidog.miniadditions.additions.leadattacher.LeadAttacherListener;
import io.github.pepsidog.miniadditions.additions.nameping.NamePing;
import io.github.pepsidog.miniadditions.additions.nosheepgriefing.NoSheepGriefingListener;
import io.github.pepsidog.miniadditions.additions.slimyboots.SlimyBootsListener;
import io.github.pepsidog.miniadditions.additions.woodpile.WoodPileListener;
import io.github.pepsidog.miniadditions.utils.Module;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MiniAdditions extends JavaPlugin {
    private static MiniAdditions self;
    private static Random rand;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        self = this;
        rand = new Random();

        saveDefaultConfig();

        ArrayList<Module> modules = Lists.newArrayList(
                new ArmorStandAdditions(),
                new CobbleGeneratorListener(),
                new ConcreteMixerListener(),
                new CraftingKeeperListener(),
                new EasyPaintings(),
                new EasySleepListener(),
                new IgneousGeneratorListener(),
                new ShearListener(),
                new NamePing(),
                new SlimyBootsListener(),
                new WoodPileListener(),
                new LeadAttacherListener(),
                new NoSheepGriefingListener(),
                new FeatherPlucker(),
                new InventoryInspector()
        );
        ArrayList<String> names = new ArrayList<>();
        for (Module module : modules) {
            names.add(module.getName());
        }

        this.configManager = new ConfigManager(this, names, true);

        for (Module module : modules) {
            YamlConfiguration config = this.configManager.getConfig(module.getName().toLowerCase());
            module.init(config);
        }

        loadCrafting();
        initExperimental();
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

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public void initExperimental() {
        this.getCommand("synth").setExecutor(new SoundSynthExperiment());
        this.getLogger().info("Sound Synth enabled");

        this.getCommand("shoot").setExecutor(new ExperimentalCommands());
        this.getLogger().info("Experimental Commands enabled");
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
            config.get("tables");
        } catch (Exception e) {
            getLogger().warning("Error loading crafting tables!");
            e.printStackTrace();
        }
    }
}