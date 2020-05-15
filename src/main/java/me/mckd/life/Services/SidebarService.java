package me.mckd.life.Services;

import me.mckd.life.Life;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class SidebarService {

    Life plugin;
    Player player;

    public SidebarService(Life plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void show() {
        String key = player.getUniqueId() + "-money";
        FileConfiguration c = this.plugin.getConfig();
        int currentMoney = c.getInt(key);

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("a", "b");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(player.getDisplayName());

        Score money = obj.getScore("Money:");
        money.setScore(currentMoney);

        Score monster = obj.getScore("KilledMonster:");
        monster.setScore(0);

        Score blockBreak = obj.getScore("BlockBreak:");
        monster.setScore(0);

        this.player.setScoreboard(board);
    }
}
