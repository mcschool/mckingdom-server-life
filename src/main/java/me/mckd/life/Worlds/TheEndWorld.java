package me.mckd.life.Worlds;

import me.mckd.life.Life;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class TheEndWorld implements Listener {

    Life plugin;
    String worldName = "world_the_end";

    public TheEndWorld(Life plugin) {
        // コンストラクタ
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // サバイバルワールドは管理者でもゲームモードの変更はできないように
    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent e) {
        if (e.getPlayer().getWorld().getName().equals(this.worldName)) {
            Player player = e.getPlayer();
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage("ゲームモードの変更はできません");
        }
    }

}
