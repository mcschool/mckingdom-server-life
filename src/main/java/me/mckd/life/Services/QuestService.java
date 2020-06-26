package me.mckd.life.Services;

import me.mckd.life.Life;
import org.bukkit.entity.Player;

/**
 * クエストに関するプログラム
 * ※報酬は別のプログラム
 */
public class QuestService {
    private Life plugin;
    private Player player;

    public QuestService (Life plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void openShop() {}
}
