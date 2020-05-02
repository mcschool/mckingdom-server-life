package me.mckd.life;

import me.mckd.life.World.lobby;
import org.bukkit.plugin.java.JavaPlugin;

public final class Life extends JavaPlugin {

    @Override
    public void onEnable() {
        new lobby(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
