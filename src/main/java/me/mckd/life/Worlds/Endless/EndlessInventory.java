package me.mckd.life.Worlds.Endless;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class EndlessInventory {


    public Inventory gameMenu() {
        // オリジナルのインベントリを用意
        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 54, "ゲームメニュー");

        ItemStack life = new ItemStack(Material.WOOD);
        ItemMeta lifeMate = life.getItemMeta();
        lifeMate.setDisplayName("自分の家");
        List<String> lifeLores = new ArrayList<>();
        lifeLores.add("これをクリックで自分の家にテレポートできます!もし自分の家を設定してないなら運営に言ってください!");
        lifeMate.setLore(lifeLores);
        life.setItemMeta(lifeMate);
        inv.setItem(5, life);

        return inv;
    }
}

