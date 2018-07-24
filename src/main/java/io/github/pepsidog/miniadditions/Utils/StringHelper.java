package io.github.pepsidog.miniadditions.Utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class StringHelper {
    private static List<ChatColor> rainbowColors = Arrays.asList(ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.BLUE, ChatColor.LIGHT_PURPLE);
    public static String rainbowfy(String str) {
        int index = 0;
        String result = "";

        for(String s : str.split("")) {
            if(s.equals(" ")) {
                result += s;
            } else {

                result += rainbowColors.get(index) + s;
                index = (index + 1) % rainbowColors.size();
            }
        }

        return result;
    }
}
