package me.mckd.life.Worlds;

import me.mckd.life.Life;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class NetherWorld implements Listener {

    Life plugin;
    String worldName = "nether";

    public NetherWorld(Life plugin) {
        // コンストラクタ
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractAtEntityEvent event){
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        String name = event.getRightClicked().getName();
        if (name.equals("survival")){
            player.performCommand("mvtp endless");
        }
    }

}
