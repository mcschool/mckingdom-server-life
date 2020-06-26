package me.mckd.life;

import me.mckd.life.Commands.PingCommand;
import me.mckd.life.Worlds.*;
import me.mckd.life.Worlds.Endless.EndlessWorld;
import me.mckd.life.Worlds.Lobby.LobbyWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
        new NetherWorld(this);
        new FishingWorld(this);
        new WorldWorld(this);
        new TheEndWorld(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ping")) {
            PingCommand.command(sender, command, label, args);
        }
        return true;
    }
}
