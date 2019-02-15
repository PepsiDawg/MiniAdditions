package io.github.pepsidog.miniadditions.NamePing;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamePing implements Listener {
    private Map<String, Date> cooldowns;
    private Pattern pattern;
    private int cooldown;

    public NamePing(int cooldown) {
        cooldowns = new HashMap<>();
        pattern = Pattern.compile("@(\\w+)\\s*");
        this.cooldown = cooldown;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String msg = event.getMessage();
        Matcher matcher = pattern.matcher(msg);
        while(matcher.find()) {
            Player player = Bukkit.getPlayer(matcher.group(1));

            if(player != null && player.isOnline()) {
                if(cooldowns.containsKey(player.getName())) {
                    Date expire = cooldowns.get(player.getName());

                    if(new Date().before(expire)) { return; }
                }

                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);

                Date expire = new Date();
                expire.setTime(expire.getTime() + (cooldown * 1000));
                cooldowns.put(player.getName(), expire);
            }
        }
    }
}
