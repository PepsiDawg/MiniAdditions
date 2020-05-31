package io.github.pepsidog.miniadditions.additions.cobblegenerator;

import io.github.pepsidog.miniadditions.utils.Module;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFormEvent;

import java.util.Arrays;
import java.util.List;

public class CobbleGeneratorListener extends Module {
    private List<Material> checks = Arrays.asList(Material.LAVA, Material.WATER);
    private BlockFace[] faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

    public CobbleGeneratorListener() {
        super("CobbleGenerator");
    }

    @Override
    public void init(YamlConfiguration config) {
        super.init(config);

        if(config.isConfigurationSection("blocks")) {
            CobbleGeneratorManager.loadConfig(config.getConfigurationSection("blocks"));
        }
    }

    @EventHandler
    public void onFromTo(BlockFormEvent event) {
        if(event.getNewState().getType().equals(Material.COBBLESTONE)) {
            event.getNewState().setType(CobbleGeneratorManager.getInstance().generateBlock());
            event.getNewState().getWorld().playSound(event.getNewState().getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1f, 1.5f);
        }
    }
}