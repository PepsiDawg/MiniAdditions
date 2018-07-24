package io.github.pepsidog.miniadditions.CobbleGenerator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.Arrays;
import java.util.List;

public class CobbleGeneratorListener implements Listener {
    private List<Material> checks = Arrays.asList(Material.LAVA, Material.STATIONARY_LAVA, Material.WATER, Material.STATIONARY_WATER);
    private BlockFace[] faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

    @EventHandler
    public void onFromTo(BlockFromToEvent event) {
        Material mat  = event.getBlock().getType();
        if(checks.contains(event.getBlock().getType())) {
            if(generatesCobble(mat, event.getToBlock()) && !event.getToBlock().getType().isSolid()) {
                event.getToBlock().setType(CobbleGeneratorManager.getInstance().generateBlock());
                event.getToBlock().getWorld().playSound(event.getToBlock().getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1f, 1.5f);
            }
        }
    }

    private boolean generatesCobble(Material type, Block block) {
        Material opt1 = (type == Material.WATER || type == Material.STATIONARY_WATER) ? Material.LAVA : Material.WATER;
        Material opt2 = (type == Material.WATER || type == Material.STATIONARY_WATER) ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER;

        for(BlockFace face : faces) {
            Block check = block.getRelative(face, 1);
            if(check.getType().equals(opt1) || check.getType().equals(opt2)) {
                return true;
            }
        }

        return false;
    }
}