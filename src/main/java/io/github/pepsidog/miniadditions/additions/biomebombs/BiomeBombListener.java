package io.github.pepsidog.miniadditions.additions.biomebombs;

import io.github.pepsidog.miniadditions.utils.CraftingUtil;
import io.github.pepsidog.miniadditions.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BiomeBombListener {
    public static void initRecipes() {
        ItemBuilder builder = new ItemBuilder(Material.FIREWORK_STAR)
                .setName(ChatColor.GREEN + "Biome " + ChatColor.GRAY + "Bomb")
                .addEnchantment(Enchantment.DURABILITY, 10);
        Map<Material, Integer> ingredients;
        ItemStack result;

        //Plains
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.TALL_GRASS, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.GREEN + "Plains")).build();
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Plains", ingredients, result);

        //Ocean
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.KELP, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.BLUE + "Ocean")).build();
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Ocean", ingredients, result);

        //Forest
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.OAK_LEAVES, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.DARK_GREEN + "Forest")).build();
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Forest", ingredients, result);

        //Desert
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.SAND, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.YELLOW + "Desert")).build();
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Desert", ingredients, result);

        //Tiaga
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.SPRUCE_LEAVES, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.DARK_BLUE + "Tiaga")).build();
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Tiaga", ingredients, result);

        //Jungle
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.JUNGLE_LEAVES, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.GREEN + "Jungle")).build();
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Jungle", ingredients, result);

        //Mesa
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.TERRACOTTA, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.GOLD + "Mesa")).build();
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Mesa", ingredients, result);

        //Roofed Forest
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.DARK_OAK_LEAVES, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.DARK_GREEN + "Roofed Forest")).build();
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Roofed_Forest", ingredients, result);

        //Mushroom Island
        ingredients = new HashMap<Material, Integer>() {{
            put(Material.EGG, 1);
            put(Material.MYCELIUM, 8);
        }};
        result = builder.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.LIGHT_PURPLE + "Mushroom Island")).build();
        CraftingUtil.addShapelessCrafting("Biomb_Bomb_Mushroom_Island", ingredients, result);
    }
}
