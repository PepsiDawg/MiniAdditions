package io.github.pepsidog.miniadditions.additions.experimental;

import io.github.pepsidog.miniadditions.MiniAdditions;
import io.github.pepsidog.miniadditions.utils.Module;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

public class BlockMD extends Module {
    public BlockMD() {
        super("BlockMD");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        String id = UUID.randomUUID().toString();
        block.setMetadata("miniadditions_id", new FixedMetadataValue(MiniAdditions.getInstance(), id));
        Bukkit.broadcastMessage("Placed " + block.getType().name() + " with id " + id);
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.STICK)) {
                Block block = event.getClickedBlock();
                if (block != null && block.hasMetadata("miniadditions_id")) {
                    Bukkit.broadcastMessage(block.getMetadata("miniadditions_id").get(0).asString());
                }
            }
        }
    }
}
