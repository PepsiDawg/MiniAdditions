package io.github.pepsidog.miniadditions.additions.woodpile;

import io.github.pepsidog.miniadditions.MiniAdditions;
import io.github.pepsidog.miniadditions.utils.Module;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class WoodPileListener extends Module {
    private int logConvertTime;
    private final Map<WoodPile, BukkitRunnable> woodPiles;

    public WoodPileListener() {
        super("WoodPile");
        woodPiles = new HashMap<>();
    }

    @Override
    public void init(YamlConfiguration config) {
        super.init(config);
        this.logConvertTime = config.getInt("log-convert-time", 5);
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent event) {
        Material replaced = event.getBlockReplacedState().getType();

        if (replaced.equals(Material.FIRE) && WoodPile.isValidCovering(event.getBlock())) {
            WoodPile wp = new WoodPile();
            if (wp.checkValid(event.getBlock())) {
                woodPiles.put(wp, new BukkitRunnable() {
                    final int lifespan = wp.getFuelSize() * logConvertTime * 20;
                    int age = 0;

                    @Override
                    public void run() {
                        if (age > lifespan) {
                            wp.convertFuel();
                            this.cancel();
                            woodPiles.remove(wp);
                            event.getBlock().getWorld().playSound(event.getBlock().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.5f, 0.5f);
                        }

                        if (age % 10 == 0) {
                            wp.showBurning(Particle.SMOKE_LARGE);
                        }

                        if (age % 40 == 0 && MiniAdditions.getRandom().nextBoolean()) {
                            event.getBlock().getWorld().playSound(event.getBlock().getLocation(), Sound.BLOCK_FIRE_AMBIENT, 0.5f, 0.5f);
                        }
                        age += 2;
                    }
                });

                woodPiles.get(wp).runTaskTimer(MiniAdditions.getInstance(), 0, 2);
            }
        }
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        for (WoodPile pile : woodPiles.keySet()) {
            if (pile.contains(event.getBlock())) {
                woodPiles.get(pile).cancel();
                woodPiles.remove(pile);
                event.getBlock().setType(Material.FIRE);
            }
        }
    }
}
