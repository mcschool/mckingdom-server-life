package me.mckd.life.Services;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class SidebarService {

    Player player;

    public SidebarService(Player player) {
        this.player = player;
    }

    public void show() {
        Bukkit.getLogger().info("show Sidebar");
        Bukkit.getLogger().info("---");
        // スコアボード
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("a", "b");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("status");
        Score money = obj.getScore("money");
        money.setScore(100);
        this.player.setScoreboard(board);
    }

}
