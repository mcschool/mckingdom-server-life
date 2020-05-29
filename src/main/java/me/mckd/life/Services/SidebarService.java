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
        String moneyKey = player.getUniqueId() + "-money";

        String killedMonsterKey = player.getUniqueId() + "-killed-monster";
        FileConfiguration c = this.plugin.getConfig();
        int currentMoney = c.getInt(moneyKey);
        int currentKilledMonster = c.getInt(killedMonsterKey);

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("a", "b");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("★☆★  生活  ★☆★");

        Score s10 = obj.getScore("職業:  ホームレス");
        s10.setScore(10);
        Score s9 = obj.getScore("お金:  " + currentMoney);
        s9.setScore(9);
        Score s8 = obj.getScore("Kill:  " + currentKilledMonster);
        s8.setScore(8);

        //        Objective obj = board.registerNewObjective("a", "b");
//        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
//        obj.setDisplayName(player.getDisplayName());
//
//        Score rank = obj.getScore("Rank:");
//        rank.setScore(0);
//
//        Score money = obj.getScore("Money:");
//        money.setScore(currentMoney);
//
//        Score monster = obj.getScore("Killed:");
//        monster.setScore(currentKilledMonster);

        this.player.setScoreboard(board);
    }
}
