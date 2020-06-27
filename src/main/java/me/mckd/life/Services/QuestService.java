package me.mckd.life.Services;

import me.mckd.life.Life;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * クエストに関するプログラム
 * ※報酬は別のプログラム
 */
public class QuestService {
    private Life plugin;
    private Player player;

    public QuestService (Life plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void open() {
        player.sendTitle("クエストショップ", "クエストを選択してください",0, 20, 0);
        new BukkitRunnable() {
            @Override
            public void run () {
                Inventory inv;
                inv = Bukkit.createInventory(null, 18, "クエストショップ");
                inv.clear();
                inv.setItem(0, setItem(Material.WOOD_SWORD, "", 1));

                player.openInventory(inv);
            }
        }.runTaskLater(this.plugin, 20);
    }


    private ItemStack setItem(Material material, String name, int count) {
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta itemMeta  = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


}
