package me.mckd.life.Worlds;

import me.mckd.life.Life;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.awt.*;

public class FishingWorld implements Listener{
    private Life plugin;
    public String worldname = "fishing";

    public FishingWorld(Life plugin){
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerWorldChangedEvent(PlayerChangedWorldEvent event){
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals(this.worldname)) return;
        Location location = new Location(player.getWorld(),-1017,4,-1828);
        player.teleport(location);
        player.sendTitle("みんなで釣りゲーム", "",40,60,40);
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals(this.worldname)) {
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event){
        if (event.getEntity().getWorld().getName().equals(this.worldname)){
            event.setCancelled(true);
        }
    }
}
