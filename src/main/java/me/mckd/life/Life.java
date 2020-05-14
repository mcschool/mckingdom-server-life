package me.mckd.life;

import me.mckd.life.Worlds.LobbyWorld;
import me.mckd.life.Worlds.MainMaterialWorld;
import org.bukkit.plugin.java.JavaPlugin;

public final class Life extends JavaPlugin {

    @Override
    public void onEnable() {
        new LobbyWorld(this);
        new MainMaterialWorld(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
