package io.github.pepsidog.miniadditions.additions.improvedshears;

import io.github.mrsperry.mcutils.ItemMetaHandler;
import io.github.mrsperry.mcutils.builders.ItemBuilder;
import io.github.pepsidog.miniadditions.MiniAdditions;
import io.github.pepsidog.miniadditions.utils.CraftingUtil;
import io.github.pepsidog.miniadditions.utils.Module;
import io.github.pepsidog.miniadditions.utils.StringHelper;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class ShearListener extends Module {
    private int chance;
    private final NamespacedKey shearKey;
    private final PersistentDataType<Byte, Byte> BYTE = PersistentDataType.BYTE;

    public ShearListener() {
        super("ImprovedShears");
        this.shearKey = new NamespacedKey(MiniAdditions.getInstance(), "Improved_Shears");
    }

    @Override
    public void init(YamlConfiguration config) {
        super.init(config);
        this.chance = config.getInt("dye-chance", 25);
        this.initRecipes();
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        if (event.getEntity() instanceof Sheep) {
            Player player = event.getPlayer();
            Sheep sheep = (Sheep) event.getEntity();
            ItemStack itemUsed = checkAndGet(player);

            if (itemUsed.getType().equals(Material.SHEARS) && ItemMetaHandler.hasKey(itemUsed, this.shearKey, BYTE)) {
                if (MiniAdditions.getRandom().nextDouble() <= (chance / 100.0)) {
                    event.setCancelled(true);
                    DyeColor color = sheep.getColor();
                    if (color != null) {
                        ItemStack drop = new ItemStack(Material.valueOf(color.name() + "_DYE"));
                        int amount = (int) Math.floor(MiniAdditions.getRandom().nextDouble() * 3) + 1;
                        drop.setAmount(amount);
                        sheep.setSheared(true);
                        sheep.getWorld().dropItemNaturally(sheep.getLocation().clone().add(0, 0.5, 0), drop);
                        player.getWorld().playSound(sheep.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
                    }
                }
            }
        }
    }

    private ItemStack checkAndGet(Player player) {
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();

        if (main.getType().equals(Material.SHEARS)) {
            return main;
        } else if (off.getType().equals(Material.SHEARS)) {
            return off;
        }
        return main;
    }

    private void initRecipes() {
        Map<Material, Integer> ingredients = new HashMap<Material, Integer>() {{
            put(Material.SHEARS, 2);
            put(Material.DIAMOND, 1);
        }};
        ItemStack result = new ItemBuilder(Material.SHEARS)
                .setName(StringHelper.rainbowfy("Improved Shears"))
                .addLore(ChatColor.GOLD + "Has a " + chance + "% chance to drop dye instead of wool!")
                .build();

        ItemMetaHandler.set(result, this.shearKey, PersistentDataType.BYTE, (byte) 1);
        CraftingUtil.addShapelessCrafting("Improved_Shears", ingredients, result);
    }
}
