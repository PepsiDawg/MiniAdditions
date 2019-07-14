package io.github.pepsidog.miniadditions.additions.easysleep;

import io.github.pepsidog.miniadditions.MiniAdditions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EasySleepListener implements Listener {
    private List<UUID> sleeping;
    private double threshold;

    public EasySleepListener(double threshold) {
        this.sleeping = new ArrayList<>();
        this.threshold = threshold;
    }

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        if(!event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)) {
            return;
        }

        if(!this.sleeping.contains(event.getPlayer().getUniqueId())) {
            this.sleeping.add(event.getPlayer().getUniqueId());
            Bukkit.broadcastMessage(ChatColor.YELLOW + event.getPlayer().getName() + " is now sleeping. " + getPlayersInBed());
        }

        if((double)this.sleeping.size() / (double)Bukkit.getOnlinePlayers().size() >= this.threshold) {

            Bukkit.getScheduler().runTaskLater(MiniAdditions.getInstance(), ()->{
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Wakey wakey, eggs and bakey.");
                this.sleeping.clear();
                event.getPlayer().getWorld().setTime(0);
            }, 100);
        }
    }

    @EventHandler
    public void onPlayerWake(PlayerBedLeaveEvent event) {
        if(this.sleeping.contains(event.getPlayer().getUniqueId())) {
            this.sleeping.remove(event.getPlayer().getUniqueId());
            Bukkit.broadcastMessage(ChatColor.YELLOW + event.getPlayer().getName() + " is no longer sleeping. " + getPlayersInBed());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if(this.sleeping.contains(event.getPlayer().getUniqueId())) {
            this.sleeping.remove(event.getPlayer().getUniqueId());
            Bukkit.broadcastMessage(ChatColor.YELLOW + event.getPlayer().getName() + " mysteriously vanished. They are no longer sleeping. " + getPlayersInBed());
        }
    }

    private String getPlayersInBed() {
        return "(" + ChatColor.GREEN + sleeping.size() + ChatColor.YELLOW + "/" + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + ChatColor.YELLOW + ")";
    }


}
