package io.github.pepsidog.miniadditions.utils.custommeta;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTUtils {
    public static String getJSONString(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbt = new NBTTagCompound();

        nbt = item.save(nbt);
        return nbt.toString();
    }
}
