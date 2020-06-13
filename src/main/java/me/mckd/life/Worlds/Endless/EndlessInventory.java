package me.mckd.life.Worlds.Endless;

import oracle.jrockit.jfr.JFR;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EndlessInventory {


    public Inventory gameMenu(Player player) {
        // オリジナルのインベントリを用意
        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 54, "ゲームメニュー");

        if (player.getUniqueId().toString().equals("4966b6cb-90e3-4b63-a523-cc62dc1e91ce")) {
            ItemStack life = new ItemStack(Material.WOOD);
            ItemMeta lifeMate = life.getItemMeta();
            lifeMate.setDisplayName("自分の家");
            List<String> lifeLores = new ArrayList<>();
            lifeLores.add("life");
            lifeMate.setLore(lifeLores);
            life.setItemMeta(lifeMate);
            inv.setItem(5, life);

        }
        return inv;
    }
}

