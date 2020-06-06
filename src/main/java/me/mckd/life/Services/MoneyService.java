package me.mckd.life.Services;

import me.mckd.life.Life;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MoneyService {

    private Life plugin;
    private Player player;

    public MoneyService (Life plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    /**
     * m = 増やすお金
     */
    public void addMoney (int m) {
        String key = player.getUniqueId() + "-money";
        FileConfiguration c = this.plugin.getConfig();
        int currentMoney = c.getInt(key);
        int newMoney = currentMoney + m;
        c.set(key, newMoney);
        plugin.saveConfig();
    }

    public void useMoney () {}
}
