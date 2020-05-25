package me.mckd.life;

import me.mckd.life.Worlds.LobbyWorld;
import me.mckd.life.Worlds.EndlessWorld;
import me.mckd.life.Worlds.Monster;
import org.bukkit.plugin.java.JavaPlugin;

public final class Life extends JavaPlugin {

    @Override
    public void onEnable() {
        // BungeeCordに繋ぐための前処理
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // 各種コードをロード
        new LobbyWorld(this);
        new EndlessWorld(this);
        new Monster(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
