package io.github.pepsidog.miniadditions.additions.slimyboots;

import io.github.mrsperry.mcutils.ItemMetaHandler;
import io.github.pepsidog.miniadditions.MiniAdditions;
import io.github.pepsidog.miniadditions.utils.CraftingUtil;
import io.github.pepsidog.miniadditions.utils.Module;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SlimyBootsListener extends Module {
    private NamespacedKey bootsKey;
    private final PersistentDataType BYTE = PersistentDataType.BYTE;

    public SlimyBootsListener() {
        super("SlimyBoots");
        bootsKey = new NamespacedKey(MiniAdditions.getInstance(), "Slimy_Boots");
        initRecipies();
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        Vector dir = player.getLocation().getDirection();

        if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            if(player.isSneaking() || !ItemMetaHandler.hasKey(player.getInventory().getBoots(), bootsKey, BYTE)) {
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

    private void initRecipies() {
        ItemStack result = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) result.getItemMeta();
        itemMeta.setColor(Color.fromRGB(100, 255, 100));
        itemMeta.setDisplayName(ChatColor.GREEN + "Slimy Boots");
        itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "A bit squishy but it should protect from falls"));

        result.setItemMeta(itemMeta);
        ItemMetaHandler.set(result, bootsKey, BYTE, (byte) 1);
        Map<Character, Material> ingredients = new HashMap<Character, Material>() {{ put('B', Material.LEATHER_BOOTS); put('S', Material.SLIME_BLOCK); }};
        CraftingUtil.addShapedCrafting("slimy_boots", ingredients, result, "SSS", "SBS", "SSS");
    }
}
