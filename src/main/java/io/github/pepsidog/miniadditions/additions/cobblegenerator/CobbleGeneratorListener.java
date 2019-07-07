package io.github.pepsidog.miniadditions.additions.cobblegenerator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.util.Arrays;
import java.util.List;

public class CobbleGeneratorListener implements Listener {
    private List<Material> checks = Arrays.asList(Material.LAVA, Material.WATER);
    private BlockFace[] faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

    @EventHandler
    public void onFromTo(BlockFormEvent event) {
        if(event.getNewState().getType().equals(Material.COBBLESTONE)) {
            event.getNewState().setType(CobbleGeneratorManager.getInstance().generateBlock());
            event.getNewState().getWorld().playSound(event.getNewState().getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1f, 1.5f);
        }
    }
}