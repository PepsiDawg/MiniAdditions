package io.github.pepsidog.miniadditions.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Arrays;
import java.util.List;

public class BlockUtils {
    private static final List<BlockFace> faces = Arrays.asList(BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);

    public static boolean isNextTo(Block block, Material material) {
        return getIfNextTo(block, material) != null;
    }

    public static Block getIfNextTo(Block block, Material mat) {
        BlockFace found = faces.stream().filter(face -> block.getRelative(face).getType().equals(mat)).findFirst().orElse(null);

        return found == null ? null : block.getRelative(found);
    }
}
