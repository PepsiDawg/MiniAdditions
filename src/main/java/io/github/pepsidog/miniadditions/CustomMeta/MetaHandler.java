package io.github.pepsidog.miniadditions.CustomMeta;

import org.bukkit.inventory.ItemStack;

public interface MetaHandler {
    boolean hasKey(ItemStack var1, String var2);

    ItemStack setKey(ItemStack var1, String var2, String var3);

    String getValue(ItemStack var1, String var2);
}
