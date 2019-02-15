package io.github.pepsidog.miniadditions.ConcreteMixer;

import io.github.pepsidog.miniadditions.MiniAdditions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cauldron;

import java.util.Arrays;
import java.util.List;

public class ConcreteMixerListener implements Listener {
    private List<Material> concrete = Arrays.asList(
            Material.BLACK_CONCRETE_POWDER,
            Material.CYAN_CONCRETE_POWDER,
            Material.BLUE_CONCRETE_POWDER,
            Material.BROWN_CONCRETE_POWDER,
            Material.GRAY_CONCRETE_POWDER,
            Material.GREEN_CONCRETE_POWDER,
            Material.LIGHT_BLUE_CONCRETE_POWDER,
            Material.LIGHT_GRAY_CONCRETE_POWDER,
            Material.LIME_CONCRETE_POWDER,
            Material.MAGENTA_CONCRETE_POWDER,
            Material.ORANGE_CONCRETE_POWDER,
            Material.PINK_CONCRETE_POWDER,
            Material.PURPLE_CONCRETE_POWDER,
            Material.RED_CONCRETE_POWDER,
            Material.WHITE_CONCRETE_POWDER,
            Material.YELLOW_CONCRETE_POWDER);
    private int waterUseChance;

    public ConcreteMixerListener(int useChance) {
        this.waterUseChance = useChance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getHand().equals(EquipmentSlot.HAND)) {
            ItemStack item = event.getItem();
            Block block = event.getClickedBlock();

            if(block != null && block.getType().equals(Material.CAULDRON) && item != null) {
                Cauldron cauldron = (Cauldron) block.getState().getData();
                Material type = item.getType();
                if(concrete.contains(type) && !cauldron.isEmpty()) {
                    Material result = Material.valueOf(type.name().substring(0, type.name().length() - 7));
                    block.getWorld().dropItem(block.getLocation().add(0.5, 1.5, 0.5), new ItemStack(result));

                    if(item.getAmount() == 1) {
                       event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    } else {
                        item.setAmount(item.getAmount()-1);
                    }

                    if(MiniAdditions.getRandom().nextInt(100) <= this.waterUseChance) {
                        Levelled levelled = (Levelled) block.getBlockData();
                        levelled.setLevel(levelled.getLevel() - 1);
                        block.setBlockData(levelled);
                    }
                }
            }

        }
    }
}
