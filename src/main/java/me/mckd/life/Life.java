package me.mckd.life;

import me.mckd.life.Worlds.LobbyWorld;
import me.mckd.life.Worlds.EndlessWorld;
import org.bukkit.plugin.java.JavaPlugin;

public final class Life extends JavaPlugin {

    @Override
    public void onEnable() {
        new LobbyWorld(this);
        new EndlessWorld(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
