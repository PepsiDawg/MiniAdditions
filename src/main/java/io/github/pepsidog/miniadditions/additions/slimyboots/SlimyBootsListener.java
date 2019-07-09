package io.github.pepsidog.miniadditions.additions.slimyboots;

import io.github.pepsidog.miniadditions.MiniAdditions;
import io.github.pepsidog.miniadditions.utils.CraftingUtil;
import io.github.pepsidog.miniadditions.utils.custommeta.MetaHandler;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SlimyBootsListener implements Listener {
    MetaHandler handler;

    public static void initRecipies() {
        ItemStack result = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) result.getItemMeta();
        itemMeta.setColor(Color.fromRGB(100, 255, 100));
        itemMeta.setDisplayName(ChatColor.GREEN + "Slimy Boots");
        itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "A bit squishy but it should protect from falls"));

        result.setItemMeta(itemMeta);
        result = MiniAdditions.getMetaHandler().setKey(result, "slimy", "true");
        Map<Character, Material> ingredients = new HashMap<Character, Material>() {{ put('B', Material.LEATHER_BOOTS); put('S', Material.SLIME_BLOCK); }};
        CraftingUtil.addShapedCrafting("slimy_boots", ingredients, result, "SSS", "SBS", "SSS");
    }

    public SlimyBootsListener() {
        handler = MiniAdditions.getMetaHandler();
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        Vector dir = player.getLocation().getDirection();

        if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            if(player.isSneaking() || !handler.hasKey(player.getInventory().getBoots(), "slimy")) {
                return;
            }

            float fallDist = player.getFallDistance();
            float fallDistModified = (-.0011f * fallDist * fallDist) + (0.43529f * fallDist);
            double velY = Math.sqrt(0.32 * fallDistModified);

            player.setVelocity(new Vector(dir.getX(), velY, dir.getZ()));
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_SLIME_BLOCK_FALL, 2, 1);
            event.setCancelled(true);
        }
    }
}
