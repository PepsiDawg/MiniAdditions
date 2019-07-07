package io.github.pepsidog.miniadditions.additions.chatitem;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;


public class ChatItemListener implements Listener {
    private final String DELIM = "((?<=\\[item\\])|(?=\\[item\\]))";

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(event.getMessage().contains("[item]") && !event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            String itemJSON = getJSONString(item);
            String name = getName(item);
            String format = event.getFormat().substring(0, event.getFormat().length() - 4);
            String color = format.substring(format.length() - 2);
            TextComponent component = getComponentMessage(event.getMessage(), format, color, itemJSON, name);

            for(Player player : event.getRecipients()) {
                player.spigot().sendMessage(component);
                event.setCancelled(true);
            }
        }
    }

    private String getJSONString(ItemStack itemStack) {
        net.minecraft.server.v1_13_R2.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbt = new NBTTagCompound();

        nbt = item.save(nbt);
        return nbt.toString();
    }

    private TextComponent getComponentMessage(String msg, String format, String color, String jsonItem, String itemName) {
        String[] parts = msg.split(DELIM);
        TextComponent component = new TextComponent(format);

        for(String part : parts) {
            if(part.equals("[item]")) {
                TextComponent hover = new TextComponent(ChatColor.AQUA + itemName);
                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[] {new TextComponent(jsonItem)}));
                component.addExtra(hover);
            } else {
                component.addExtra(new TextComponent(color + part));
            }
        }

        return component;
    }

    private String getName(ItemStack item) {
        if(item.hasItemMeta() && !item.getItemMeta().getDisplayName().isEmpty()) {
            return item.getItemMeta().getDisplayName();
        }

        return WordUtils.capitalizeFully(item.getType().name().toLowerCase().replace('_', ' '));
    }
}
