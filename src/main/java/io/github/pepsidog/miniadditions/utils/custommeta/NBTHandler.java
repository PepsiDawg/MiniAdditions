package io.github.pepsidog.miniadditions.utils.custommeta;

import net.minecraft.server.v1_13_R2.NBTTagCompound;

import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class NBTHandler implements MetaHandler {
    private JavaPlugin host;
    private String basePath;

    protected NBTHandler(JavaPlugin plugin) {
        this.host = plugin;
        this.basePath += this.host.getName() + ".";
    }

    public boolean hasKey(ItemStack item, String key) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

        return tag.hasKey(this.basePath + key);
    }

    public ItemStack setKey(ItemStack item, String key, String value) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        tag.setString(this.basePath + key, value);
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public String getValue(ItemStack item, String key) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

        return this.hasKey(item, key) ? tag.getString(this.basePath + key) : null;
    }
}
