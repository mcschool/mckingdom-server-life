package me.mckd.life;

import me.mckd.life.Worlds.LobbyWorld;
import me.mckd.life.Worlds.EndlessWorld;
<<<<<<< HEAD
import me.mckd.life.Worlds.Monster;
=======
import me.mckd.life.Worlds.NetherWorld;
>>>>>>> c799ffd8c76fc5e1bf5ba0399930173b214c99e6
import org.bukkit.plugin.java.JavaPlugin;

public final class Life extends JavaPlugin {

    @Override
    public void onEnable() {
        // BungeeCordに繋ぐための前処理
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // 各種コードをロード
        new LobbyWorld(this);
        new EndlessWorld(this);
<<<<<<< HEAD
        new Monster(this);
=======
        new NetherWorld(this);
>>>>>>> c799ffd8c76fc5e1bf5ba0399930173b214c99e6
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
