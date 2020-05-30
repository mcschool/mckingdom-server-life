package me.mckd.life.Worlds.EndlessWorld;

import me.mckd.life.Life;
import me.mckd.life.Services.SidebarService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Job {

    private Life plugin;
    private Player player;

    public Job(Life plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    /**
     * 職業選択画面
     */
    public void openJobSelect() {
        player.sendTitle("職業選択", "好きな職業を選択してください",0, 20, 0);
        new BukkitRunnable() {
            @Override
            public void run () {
                Inventory inv;
                inv = Bukkit.createInventory(null, 18, "職業選択");
                inv.clear();
                inv.setItem(0, setItem(Material.IRON_SWORD, "警備員", 1));
                inv.setItem(1, setItem(Material.WOOD_AXE, "木こり", 1));
                player.openInventory(inv);
            }
        }.runTaskLater(this.plugin, 20);
    }

    /**
     * 職業選択のインベントリのタイムをクリックした場合
     * @param e
     */
    public void clickJobInventory(InventoryClickEvent e) {
        String itemName = "";
        if (e.getCurrentItem().hasItemMeta()) {
            itemName = e.getCurrentItem().getItemMeta().getDisplayName();
        }
        if (itemName.equals("")) return;
        if (e.getRawSlot() > 18) return;

        FileConfiguration c = this.plugin.getConfig();
        String key = player.getUniqueId() + "-job-type";

        String message = "";
        if (itemName.equals("警備員")) {
            c.set(key, "警備員");
            message = "街をモンスターから守ってください！";
        }
        else if (itemName.equals("木こり")) {
            c.set(key, "木こり");
            message = "たくさんの原木を集めてください！";
        }

        // セットした設定を保存
        this.plugin.saveConfig();

        // スコアボード更新
        SidebarService sidebarService = new SidebarService(this.plugin, player);
        sidebarService.show();
        // インベントリ閉じてメッセージ
        this.player.closeInventory();
        this.player.sendTitle(itemName, message, 0, 20, 0);
    }

    public void receiveSalary() {
        this.player.sendMessage("すみません... 準備中...");
    }

    /**
     * アイテムを生成するのに手順ふむの面倒なので関数化
     * @param material
     * @param name
     * @param count
     * @return
     */
    private ItemStack setItem(Material material, String name, int count) {
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta itemMeta  = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
