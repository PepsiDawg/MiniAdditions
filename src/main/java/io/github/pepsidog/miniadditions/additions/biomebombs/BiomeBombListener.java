package io.github.pepsidog.miniadditions.additions.biomebombs;

import io.github.mrsperry.mcutils.ItemMetaHandler;
import io.github.pepsidog.miniadditions.MiniAdditions;
import io.github.pepsidog.miniadditions.utils.CraftingUtil;
import io.github.pepsidog.miniadditions.utils.CustomProjectile;
import io.github.pepsidog.miniadditions.utils.ItemBuilder;
import io.github.pepsidog.miniadditions.utils.Module;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;

public class BiomeBombListener extends Module {
    private NamespacedKey biomeBombKey;
    private final PersistentDataType STRING = PersistentDataType.STRING;

    public BiomeBombListener() {
        super("BiomeBombs");
        biomeBombKey = new NamespacedKey(MiniAdditions.getInstance(), "biomb_bomb_type");
        this.initRecipes();
    }

    @EventHandler
    public void onBiomeBombUse(PlayerInteractEvent event) {
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Player player = event.getPlayer();
        if(item != null && ItemMetaHandler.hasKey(item, biomeBombKey, STRING)) {
            CustomProjectile biomeBomb = getBiomeBomb(item, player.getEyeLocation(), player.getLocation().getDirection());
            item.setAmount(item.getAmount() - 1);
            biomeBomb.launch();
        }
    }

    private CustomProjectile getBiomeBomb(ItemStack item, Location location, Vector direction) {
        CustomProjectile biomeBomb = new CustomProjectile(location, direction, 0.1, 500)
                .addAcceleration(0.05, 0.75)
                .addGravity(0.05)
                .onDisplay(proj -> {
                    if(proj.hasMetadata("biomebomb_as")) {
                        ArmorStand armorStand = (ArmorStand) proj.getMetadata("biomebomb_as");
                        armorStand.teleport(proj.getLocation().clone().subtract(0, 2, 0));
                    }
                })
                .onBlockCollision((proj, block) -> {
                    if(proj.hasMetadata("biomebomb_as") && proj.hasMetadata("type")) {
                        ArmorStand armorStand = (ArmorStand) proj.getMetadata("biomebomb_as");
                        String type = String.valueOf(proj.getMetadata("type"));

                        armorStand.remove();
                        proj.destroy();
                        Bukkit.broadcastMessage(ChatColor.AQUA + "KABOOOOOOM (" + type + ")");
                    }
                });
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().subtract(0, 2, 0), EntityType.ARMOR_STAND);
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setHelmet(item.clone());

        biomeBomb.setMetaData("biomebomb_as", armorStand);
        biomeBomb.setMetaData("type", ItemMetaHandler.get(item, biomeBombKey, STRING));
        return biomeBomb;
    }

    private void addRecipe(Map<Material, Integer> ingredients, String type, ChatColor textColor, Color... fireworkColors) {
        ItemBuilder builder = new ItemBuilder(Material.FIREWORK_STAR)
                .setName(ChatColor.GREEN + "Biome " + ChatColor.GRAY + "Bomb");
        ItemStack item = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + textColor + type)).build();
        ItemMetaHandler.set(item, biomeBombKey, STRING, type.toUpperCase());

        FireworkEffectMeta meta = (FireworkEffectMeta) item.getItemMeta();
        FireworkEffect effect = FireworkEffect.builder().withColor(fireworkColors).build();
        meta.setEffect(effect);
        item.setItemMeta(meta);

        CraftingUtil.addShapelessCrafting("Biomb_Bomb_" + type, ingredients, item);
    }

    private void initRecipes() {
        //Plains
        this.addRecipe(
                new HashMap<Material, Integer>() {{
                    put(Material.EGG, 1);
                    put(Material.GRASS, 8);
                }},
                "Plains",
                ChatColor.GREEN,
                Color.fromRGB(130, 255, 185)
        );

        //Ocean
        this.addRecipe(
                new HashMap<Material, Integer>() {{
                    put(Material.EGG, 1);
                    put(Material.KELP, 8);
                }},
                "Ocean",
                ChatColor.BLUE,
                Color.fromRGB(225, 205, 95)
        );

        //Forest
        this.addRecipe(
                new HashMap<Material, Integer>() {{
                    put(Material.EGG, 1);
                    put(Material.OAK_LEAVES, 8);
                }},
                "Forest",
                ChatColor.DARK_GREEN,
                Color.fromRGB(75, 130, 35)
        );

        //Desert
        this.addRecipe(
                new HashMap<Material, Integer>() {{
                    put(Material.EGG, 1);
                    put(Material.SAND, 8);
                }},
                "Desert",
                ChatColor.YELLOW,
                Color.fromRGB(255, 240, 150)
        );

        //Tiaga
        this.addRecipe(
                new HashMap<Material, Integer>() {{
                    put(Material.EGG, 1);
                    put(Material.SPRUCE_LEAVES, 8);
                }},
                "Tiaga",
                ChatColor.DARK_BLUE,
                Color.fromRGB(90, 115, 60)
        );

        //Jungle
        this.addRecipe(
                new HashMap<Material, Integer>() {{
                    put(Material.EGG, 1);
                    put(Material.JUNGLE_LEAVES, 8);
                }},
                "Jungle",
                ChatColor.GREEN,
                Color.fromRGB(55, 255, 45)
        );

        //Mesa
        this.addRecipe(
                new HashMap<Material, Integer>() {{
                    put(Material.EGG, 1);
                    put(Material.TERRACOTTA, 8);
                }},
                "Mesa",
                ChatColor.GOLD,
                Color.fromRGB(165, 120, 90)
        );

        //Roofed Forest
        this.addRecipe(
                new HashMap<Material, Integer>() {{
                    put(Material.EGG, 1);
                    put(Material.DARK_OAK_LEAVES, 8);
                }},
                "Roofed_Forest",
                ChatColor.DARK_GREEN,
                Color.fromRGB(25, 90, 25)
        );

        //Mushroom Island
        this.addRecipe(
                new HashMap<Material, Integer>() {{
                    put(Material.EGG, 1);
                    put(Material.MYCELIUM, 8);
                }},
                "Mushroom_Island",
                ChatColor.LIGHT_PURPLE,
                Color.fromRGB(200, 125, 200)
        );
    }
}
