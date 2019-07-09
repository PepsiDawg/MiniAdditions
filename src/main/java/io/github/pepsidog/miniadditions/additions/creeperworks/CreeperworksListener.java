package io.github.pepsidog.miniadditions.additions.creeperworks;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class CreeperworksListener implements Listener {
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        // Check if the entity is a creeper
        if (entity.getType() == EntityType.CREEPER) {
            // Spawn the firework at the creeper's location when it explodes
            Firework firework = (Firework) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.FIREWORK);

            // Get the fireworks meta and add effects
            FireworkMeta meta = firework.getFireworkMeta();
            meta.setPower(2);
            meta.addEffect(FireworkEffect.builder()
                    .with(FireworkEffect.Type.CREEPER)
                    .withColor(Color.LIME)
                    .withFade(Color.GREEN)
                    .flicker(true)
                    .build());

            firework.setFireworkMeta(meta);
        }
    }
}
