package io.github.pepsidog.miniadditions.additions.igneousgenerator;

import io.github.pepsidog.miniadditions.MiniAdditions;
import io.github.pepsidog.miniadditions.utils.BlockUtils;
import io.github.pepsidog.miniadditions.utils.Module;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class IgneousGeneratorListener extends Module {
    private List<Material> igneousMaterials = Arrays.asList(Material.DIORITE, Material.GRANITE, Material.ANDESITE);

    public IgneousGeneratorListener() {
        super("IgneousGenerator");
    }

    @EventHandler
    public void blockFromToEvent(BlockFromToEvent event) {
        Block to = event.getToBlock();
        if(event.getBlock().getType().equals(Material.WATER)) {
            if(BlockUtils.isNextTo(to, Material.MAGMA_BLOCK)) {
                Material rock = this.igneousMaterials.get(MiniAdditions.getRandom().nextInt(3));
                event.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        to.setType(rock);
                        to.getWorld().playSound(to.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.25f, 0.25f);
                    }
                }.runTaskLater(MiniAdditions.getInstance(), MiniAdditions.getRandom().nextInt(40));
            }
        }
    }
}
