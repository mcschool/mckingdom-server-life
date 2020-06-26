package me.mckd.life.Services;

import me.mckd.life.Life;
import org.bukkit.entity.Player;

/**
 * クエストの報酬に関連するプログラム
 */
public class QuestRewardService {
    private Life plugin;
    private Player player;

    public QuestRewardService (Life plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
}
