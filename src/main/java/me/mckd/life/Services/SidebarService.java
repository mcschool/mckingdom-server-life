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

        FileConfiguration c = this.plugin.getConfig();
        PlayerDataService playerDataService = new PlayerDataService(plugin, player);

        // お金
        String moneyKey = player.getUniqueId() + "-money";
        int currentMoney = c.getInt(moneyKey, 0);

        // 敵倒
        String killedMonsterKey = player.getUniqueId() + "-killed-monster";
        int currentKilledMonster = c.getInt(killedMonsterKey, 0);

        // 職業
        String jobKey = player.getUniqueId() + "-job-type";
        String currentJob = c.getString(jobKey, "");
        if (currentJob.equals("")) {
            currentJob = "ホームレス";
        }

        // ガチャチケット
        int normalGachaTicket = playerDataService.getNormalGachaTicket();

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("a", "b");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("★☆★  生活  ★☆★");

        Score s11 = obj.getScore("-");
        s11.setScore(11);
        Score s10 = obj.getScore("職業:  " + currentJob);
        s10.setScore(10);
        Score s9 = obj.getScore("お金:  " + currentMoney + "円");
        s9.setScore(9);
        Score s8 = obj.getScore("敵倒:  " + currentKilledMonster + "kills!");
        s8.setScore(8);
        Score s7 = obj.getScore("称号:  みじんこ");
        s7.setScore(7);
        Score s6 = obj.getScore("ガチャチケ:  " + String.valueOf(normalGachaTicket) + "枚");
        s6.setScore(6);
        Score s5 = obj.getScore("");
        s5.setScore(5);
        Score s4 = obj.getScore("                ");
        s4.setScore(4);

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
