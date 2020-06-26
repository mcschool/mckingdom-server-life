package me.mckd.life.Services;

import me.mckd.life.Life;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class BankService {
    private Life plugin;
    private Player player;

    public BankService(Life plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void open() {
        Player player = this.player;
        player.sendTitle("銀行", "お金を預けたり、引き出したりします", 0, 20, 0);
        // 1秒遅延させて実行
        new BukkitRunnable() {
            @Override
            public void run() {
                Inventory inv;
                inv = Bukkit.createInventory(null, 36, "銀行");
                inv.setItem(0, setItem(Material.PAPER, "残高照会", 1));

                inv.setItem(9, setItem(Material.PAPER, "10円預ける", 1));
                inv.setItem(10, setItem(Material.PAPER, "100円預ける", 1));
                inv.setItem(11, setItem(Material.PAPER, "1000円預ける", 1));
                inv.setItem(12, setItem(Material.PAPER, "10000円預ける", 1));
                inv.setItem(13, setItem(Material.PAPER, "100000円預ける", 1));
                inv.setItem(14, setItem(Material.PAPER, "1000000円預ける", 1));

                inv.setItem(27, setItem(Material.PAPER, "10円引き出す", 1));
                inv.setItem(28, setItem(Material.PAPER, "100円引き出す", 1));
                inv.setItem(29, setItem(Material.PAPER, "1000円引き出す", 1));
                inv.setItem(30, setItem(Material.PAPER, "10000円引き出す", 1));
                inv.setItem(31, setItem(Material.PAPER, "100000円引き出す", 1));
                inv.setItem(32, setItem(Material.PAPER, "1000000円引き出す", 1));

                inv.clear();
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
