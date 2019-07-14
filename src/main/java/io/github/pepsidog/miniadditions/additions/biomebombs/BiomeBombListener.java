package io.github.pepsidog.miniadditions.additions.biomebombs;

import io.github.pepsidog.miniadditions.MiniAdditions;
import io.github.pepsidog.miniadditions.utils.CraftingUtil;
import io.github.pepsidog.miniadditions.utils.CustomProjectile;
import io.github.pepsidog.miniadditions.utils.ItemBuilder;
import io.github.pepsidog.miniadditions.utils.custommeta.MetaHandler;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class BiomeBombListener implements Listener {
    private MetaHandler meta;

    public BiomeBombListener() {
        meta = MiniAdditions.getMetaHandler();
    }

    @EventHandler
    public void onBiomeBombUse(PlayerInteractEvent event) {
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Player player = event.getPlayer();
        if(item != null && meta.hasKey(item, "biome_type")) {
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
        biomeBomb.setMetaData("type", this.meta.getValue(item, "biome_type"));
        return biomeBomb;
    }

    public static void initRecipes() {
        ItemBuilder builder = new ItemBuilder(Material.FIREWORK_STAR)
                .setName(ChatColor.GREEN + "Biome " + ChatColor.GRAY + "Bomb");
        Map<Material, Integer> ingredients;
        ItemStack result;
        MetaHandler metaHandler = MiniAdditions.getMetaHandler();

        //Plains
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.GRASS, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.GREEN + "Plains")).build();
        result = metaHandler.setKey(result, "biome_type", "PLAINS");
        result = giveColors(result, Color.fromRGB(185, 255, 130));
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Plains", ingredients, result);

        //Ocean
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.KELP, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.BLUE + "Ocean")).build();
        result = metaHandler.setKey(result, "biome_type", "OCEAN");
        result = giveColors(result, Color.fromRGB(95, 205, 225));
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Ocean", ingredients, result);

        //Forest
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.OAK_LEAVES, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.DARK_GREEN + "Forest")).build();
        result = metaHandler.setKey(result, "biome_type", "FOREST");
        result = giveColors(result, Color.fromRGB(35, 130, 75));
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Forest", ingredients, result);

        //Desert
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.SAND, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.YELLOW + "Desert")).build();
        result = metaHandler.setKey(result, "biome_type", "DESERT");
        result = giveColors(result, Color.fromRGB(225, 240, 150));
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Desert", ingredients, result);

        //Tiaga
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.SPRUCE_LEAVES, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.DARK_BLUE + "Tiaga")).build();
        result = metaHandler.setKey(result, "biome_type", "TIAGA");
        result = giveColors(result, Color.fromRGB(90, 115, 60));
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Tiaga", ingredients, result);

        //Jungle
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.JUNGLE_LEAVES, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.GREEN + "Jungle")).build();
        result = metaHandler.setKey(result, "biome_type", "JUNGLE");
        result = giveColors(result, Color.fromRGB(55, 255, 45));
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Jungle", ingredients, result);

        //Mesa
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.TERRACOTTA, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.GOLD + "Mesa")).build();
        result = metaHandler.setKey(result, "biome_type", "MESA");
        result = giveColors(result, Color.fromRGB(165, 120, 90));
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Mesa", ingredients, result);

        //Roofed Forest
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.DARK_OAK_LEAVES, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.DARK_GREEN + "Roofed Forest")).build();
        result = metaHandler.setKey(result, "biome_type", "ROOFED_FOREST");
        result = giveColors(result, Color.fromRGB(25, 90, 25));
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Roofed_Forest", ingredients, result);

        //Mushroom Island
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.MYCELIUM, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.LIGHT_PURPLE + "Mushroom Island")).build();
        result = metaHandler.setKey(result, "biome_type", "MUSHROOM_ISLAND");
        result = giveColors(result, Color.fromRGB(200, 125, 200));
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Mushroom_Island", ingredients, result);
    }

    private static ItemStack giveColors(ItemStack item, Color... colors) {
        FireworkEffectMeta meta = (FireworkEffectMeta) item.getItemMeta();
        FireworkEffect effect = FireworkEffect.builder().withColor(colors).build();
        meta.setEffect(effect);
        item.setItemMeta(meta);
        return item;
    }
}
