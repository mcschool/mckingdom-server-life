package me.mckd.life.Services;

import me.mckd.life.Life;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerDataService {

    Player player;
    Life plugin;
    FileConfiguration config;
    String playerKeyPrefix;

    public PlayerDataService(Life plugin, Player player) {
        this.player = player;
        this.config = plugin.getConfig();
        this.playerKeyPrefix = player.getUniqueId().toString() + "";
    }

    /**
     * 現在の所持金を取得
     */
    public void setCurrentMoney(int money) {
        String key = "-money";
        config.set(playerKeyPrefix + key, money);
    }
    public int getCurrentMoney() {
        String key = "-money";
        return config.getInt(playerKeyPrefix + key, 0);
    }

    /**
     * 最後にログインした日
     * format: day 6/22
     */
    public void setLastLoginDay(String day) {
        String key = "-last-login-day";
        config.set(playerKeyPrefix + key, day);
    }
    public String getLastLoginDay() {
        String key = "-last-login-day";
        return config.getString(playerKeyPrefix + key, "");
    }

    /**
     * ノーマルガチャチケ
     */
    public void setNormalGachaTicket(int num) {
        String key = "-normal-gacha-ticket";
        config.set(playerKeyPrefix + key, num);
    }
    public int getNormalGachaTicket() {
        String key = "-normal-gacha-ticket";
        return config.getInt(playerKeyPrefix + key, 0);
    }
}
