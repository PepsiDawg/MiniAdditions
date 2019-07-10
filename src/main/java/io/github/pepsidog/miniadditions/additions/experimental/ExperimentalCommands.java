package io.github.pepsidog.miniadditions.additions.experimental;

import io.github.pepsidog.miniadditions.utils.CustomProjectile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExperimentalCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            Bukkit.getLogger().info("Only players can use this command!");
            return true;
        }

        if(command.getName().equalsIgnoreCase("shoot")) {
            Player player = (Player) commandSender;
            CustomProjectile projectile = BottleProjectile.getBottleProjectile(player.getLocation().clone(), player.getLocation().getDirection().clone());
            projectile.launch();
        }
        return true;
    }
}
