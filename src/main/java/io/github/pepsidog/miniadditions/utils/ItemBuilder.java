package io.github.pepsidog.miniadditions.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.SimpleAttachableMaterialData;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private ItemStack itemStack;
    private List<String> lore;
    private String name;

    public ItemBuilder() {
        this(Material.AIR, 1);
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.lore = new ArrayList<>();
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder setData(SimpleAttachableMaterialData data) {
        this.itemStack.setData(data);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addLore(String lore) {
        this.lore.add(lore);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStack build() {
        ItemMeta meta = this.itemStack.getItemMeta();

        if(!this.name.equals("")) {
            meta.setDisplayName(this.name);
        }

        if(this.lore.size() > 0) {
            meta.setLore(this.lore);
        }

        this.itemStack.setItemMeta(meta);
        return this.itemStack;
    }
}
