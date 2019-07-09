package io.github.pepsidog.miniadditions.additions.experimental;

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
            BottleProjectile projectile = new BottleProjectile(player);
            projectile.launch();
        }
        return true;
    }
}
