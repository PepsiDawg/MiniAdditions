package io.github.pepsidog.miniadditions.ImprovedShears;

import io.github.pepsidog.miniadditions.MiniAdditions;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ShearListener implements Listener {
    private int chance;

    public ShearListener(int chance) {
        this.chance = chance;
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        if(event.getEntity() instanceof Sheep) {
            Player player = event.getPlayer();
            Sheep sheep = (Sheep) event.getEntity();
            ItemStack itemUsed = checkAndGet(player);

            if(itemUsed.getType().equals(Material.SHEARS) && MiniAdditions.getMetaHandler().hasKey(itemUsed, "improved-shears")) {
                if(MiniAdditions.getRandom().nextDouble() <= (chance / 100.0)) {
                    event.setCancelled(true);
                    ItemStack drop = new ItemStack(Material.INK_SACK, 1, (short)sheep.getColor().getDyeData());
                    int amount = (int) Math.floor(MiniAdditions.getRandom().nextDouble() * 3) + 1;
                    drop.setAmount(amount);
                    sheep.setSheared(true);
                    sheep.getWorld().dropItemNaturally(sheep.getLocation().clone().add(0, 0.5, 0), drop);
                    player.getWorld().playSound(sheep.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
                }
            }
        }
    }

    private ItemStack checkAndGet(Player player) {
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();

        if(main.getType().equals(Material.SHEARS)) {
            return main;
        } else if(off.getType().equals(Material.SHEARS)) {
            return off;
        }
        return main;
    }
}
