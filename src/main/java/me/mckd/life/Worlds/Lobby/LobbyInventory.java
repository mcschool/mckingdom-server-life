package me.mckd.life.Worlds.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LobbyInventory {

    public Inventory gameMenu() {
        // オリジナルのインベントリを用意
        Inventory inv = Bukkit.createInventory(null, 54, "ゲームメニュー");

        // サバイバル: ダイヤのつるはし
        // ========================
        /*
        ItemStack survival = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta survivalMeta = survival.getItemMeta();
        survivalMeta.setDisplayName("サバイバル");
        List<String> survivalLores = new ArrayList<>();
        survivalLores.add("サバイバルワールドに移動します");
        survivalLores.add("誰でも遊べるので自由にサバイバル生活を楽しんでください");
        survivalMeta.setLore(survivalLores);
        survival.setItemMeta(survivalMeta);
        inv.setItem(0, survival);
         */

        // 建築ワールド: チェスト
        // ========================
        /*
        ItemStack build = new ItemStack(Material.CHEST);
        ItemMeta buildMeta = build.getItemMeta();
        buildMeta.setDisplayName("建築ワールド");
        List<String> buildLores = new ArrayList<>();
        buildLores.add("建築ワールドに移動します");
        buildLores.add("建築ワールドは常連さん専用のワールドです");
        buildMeta.setLore(buildLores);
        build.setItemMeta(buildMeta);
        inv.setItem(1, build);
        */

        // PVP: ダイヤの剣
        // ========================
        /*
        ItemStack pvp = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta pvpMeta = pvp.getItemMeta();
        pvpMeta.setDisplayName("PvP");
        List<String> pvpLores = new ArrayList<>();
        pvpLores.add("Player vs Player");
        pvpLores.add("なんでもありのバトルロワイヤル！");
        pvpMeta.setLore(pvpLores);
        pvp.setItemMeta(pvpMeta);
        inv.setItem(2, pvp);
        */

        // アスレ: ダイヤのブーツ
        // ========================
        /*
        ItemStack athletic = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta athleticMeta = athletic.getItemMeta();
        athleticMeta.setDisplayName("アスレチック");
        List<String> athleticLores = new ArrayList<>();
        athleticLores.add("コース数多！");
        athleticLores.add("ランキング・タイムアタックあり(準備中)");
        athleticMeta.setLore(athleticLores);
        athletic.setItemMeta(athleticMeta);
        inv.setItem(3, athletic);
        */

        /*
        ItemStack pve = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta pveMate = pve.getItemMeta();
        pveMate.setDisplayName("Mobs Killer");
        List<String> pveLores  = new  ArrayList<>();
        pveLores.add("PVE");
        pveMate.setLore(pveLores);
        pve.setItemMeta(pveMate);
        inv.setItem(5,pve);
        */

        /*
        ItemStack ty = new ItemStack(Material.DIAMOND);
        ItemMeta tyMate = ty.getItemMeta();
        tyMate.setDisplayName("TypingGame");
        List<String> tyLores = new ArrayList<>();
        tyLores.add("対戦型タイピングゲーム");
        tyMate.setLore(tyLores);
        ty.setItemMeta(tyMate);
        inv.setItem(7,ty);
        */

        // クリエ: シーランタン
        // ========================
        /*
        ItemStack creative = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta creativeMeta = creative.getItemMeta();
        creativeMeta.setDisplayName("クリエになろう!");
        List<String> creativeLores = new ArrayList<>();
        creativeLores.add("特定の人のみクリエイティブ可能");
        creativeMeta.setLore(creativeLores);
        creative.setItemMeta(creativeMeta);
        inv.setItem(4, creative);
        */

        /*
        ItemStack oni = new ItemStack(Material.ENDER_PEARL);
        ItemMeta oniMeta = oni.getItemMeta();
        oniMeta.setDisplayName("みんなでワイワイ鬼ごっこ!");
        List<String> oniLores = new ArrayList<>();
        oniLores.add("学校で鬼ごっこ！増やし鬼です！");
        oniMeta.setLore(oniLores);
        oni.setItemMeta(oniMeta);
        inv.setItem(5,oni);
        */

        return inv;
    }

}
