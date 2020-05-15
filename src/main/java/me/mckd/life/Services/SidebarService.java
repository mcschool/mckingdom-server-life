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
        // スコアボード
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Objective obj = board.registerNewObjective("a", "b");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("ステータス");
        Score money = obj.getScore("お金");
        money.setScore(100);
        this.player.setScoreboard(board);
    }

}
