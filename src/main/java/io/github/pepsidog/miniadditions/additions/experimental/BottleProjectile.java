package io.github.pepsidog.miniadditions.additions.experimental;

import io.github.pepsidog.miniadditions.utils.Projectile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BottleProjectile extends Projectile {

    public BottleProjectile(Player player) {
        super(new ItemStack(Material.GLASS_BOTTLE), player.getEyeLocation());
        this.setLifespan(100);
        this.setDragDelta(0);
        this.applyForce(player.getLocation().getDirection().normalize().multiply(1.5));
    }
}
