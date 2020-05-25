package me.mckd.life.Worlds;

import me.mckd.life.Life;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Monster implements Listener {

    Life plugin;
    String worldName = "monster";

    public Monster(Life plugin) {

        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    

}
