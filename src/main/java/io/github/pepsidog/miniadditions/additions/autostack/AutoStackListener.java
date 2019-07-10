package io.github.pepsidog.miniadditions.additions.autostack;

import io.github.pepsidog.miniadditions.MiniAdditions;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.logging.Logger;

public class AutoStackListener implements Listener {
    private void searchForStack(Player player, ItemStack item, int amount) {
        Logger log = MiniAdditions.getInstance().getLogger();
        // Check if the player is in survival or adventure mode
        if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) {
            return;
        }

        // Check if the item is enchanted
        if (item.getEnchantments().size() != 0) {
            return;
        }

        // Get the player's inventory
        PlayerInventory inventory = player.getInventory();
        // Make sure the inventory contains the material type we're looking for
        if (!inventory.contains(item.getType())) {
            return;
        }

        // Search the inventory for an item stack
        for (int index = 0; index < 36; index++) {
            // Skip over the current slot, its item may not have been removed yet
            if (index == inventory.getHeldItemSlot()) {
                continue;
            }

            // Check if the item stack is actually being depleted
            if (inventory.getItemInMainHand().getAmount() != amount) {
                continue;
            }

            // Get the current item stack
            ItemStack current = inventory.getItem(index);
            // Check if its null (empty inventory slots are null)
            if (current == null) {
                continue;
            }

            // Check if the current item stack is equal to the item stack we're looking for without considering amount
            if (!current.isSimilar(item)) {
                continue;
            }

            // Check if the data matches
            if (item.getData().getData() != current.getData().getData()) {
                continue;
            }

            // Set the item
            inventory.setItemInMainHand(current);
            inventory.getItem(index).setAmount(0);
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // Check if the hand used is the main hand so stacking never happens to the offhand
        if (event.getHand() == EquipmentSlot.HAND) {
            Player player = event.getPlayer();

            // Search with an amount of 1 because the block hasn't been placed yet
            this.searchForStack(event.getPlayer(), event.getItemInHand(), 1);
            // Get rid of any ghost items
            player.updateInventory();
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        // Search with an amount of 0 because the item has been thrown
        this.searchForStack(event.getPlayer(), event.getItemDrop().getItemStack(), 0);
        // Get rid of any ghost items
        player.updateInventory();
    }
}
