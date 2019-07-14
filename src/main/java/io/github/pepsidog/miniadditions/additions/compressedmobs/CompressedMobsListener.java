package io.github.pepsidog.miniadditions.additions.compressedmobs;

import io.github.mrsperry.mcutils.EntityTypes;
import io.github.pepsidog.miniadditions.ConfigManager;
import io.github.pepsidog.miniadditions.MiniAdditions;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;

public class CompressedMobsListener implements Listener {
    private JavaPlugin plugin;
    private Random random;

    private HashSet<EntityType> blacklist = new HashSet<>();
    private int chanceToSpawn = 5;
    private int yield = 5;

    public CompressedMobsListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.random = MiniAdditions.getRandom();

        YamlConfiguration config = ConfigManager.getConfig("compressedmobs");
        if (config != null) {
            for (String type : config.getStringList("blacklist")) {
                try {
                    // Allow for different name formats
                    this.blacklist.add(EntityType.valueOf(type.toUpperCase().replace(" ", "_")));
                } catch (Exception ex) {
                    plugin.getLogger().warning("Could not find entity type '" + type + "' from the compressed mobs blacklist!");
                }
            }

            this.chanceToSpawn = config.getInt("chance-to-spawn");
            this.yield = config.getInt("yield");
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity mob = event.getEntity();

        // Stop any slimes from a slime split from being compressed
        // Without this, slimes can infinitely duplicate and eventually crash the server (mathematically)
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
            return;
        }

        // Check if the entity is a mob that can be compressed
        if (EntityTypes.getAllTypes().contains(mob.getType())) {
            // Check if the mob can be compressed
            if (!mob.hasMetadata("no-compress")) {
                // Check if the mob is blacklisted
                if (!this.blacklist.contains(mob.getType())) {
                    if (this.random.nextInt(100) <= this.chanceToSpawn) {
                        // Set mob's metadata for its death event
                        mob.setMetadata("compressed", new FixedMetadataValue(this.plugin, true));

                        // Make the mob's name visible
                        mob.setCustomName(ChatColor.GRAY + "Compressed " + mob.getName());
                        mob.setCustomNameVisible(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity mob = event.getEntity();

        // Check if the mob is compressed
        if (mob.hasMetadata("compressed")) {
            // Create entities based on the yield amount
            for (int amount = 0; amount < this.yield; amount++) {
                // Create a new entity and set its metadata BEFORE the spawn event can be called
                Entity entity = mob.getWorld().spawn(mob.getLocation(), mob.getClass(),
                        spawn -> spawn.setMetadata("no-compress", new FixedMetadataValue(this.plugin, true)));

                // Set their velocity to a random value
                entity.setVelocity(new Vector(
                        (this.random.nextDouble() - 0.5) / 2,
                        this.random.nextDouble() / 2,
                        (this.random.nextDouble() - 0.5) / 2));
            }
        }
    }
}
