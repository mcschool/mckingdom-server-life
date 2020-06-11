package me.mckd.life.Services;

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

public class JobService {

    private Life plugin;
    private Player player;

    public JobService(Life plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    /**
     * 職業選択画面
     */
    public void openJobSelect() {
        Player player = this.player;
        player.sendTitle("職業選択", "好きな職業を選択してください",0, 20, 0);
        new BukkitRunnable() {
            @Override
            public void run () {
                Inventory inv;
                inv = Bukkit.createInventory(null, 18, "職業選択");
                inv.clear();
                inv.setItem(0, setItem(Material.WOOD_BUTTON, "ホームレス", 1));
                inv.setItem(1, setItem(Material.IRON_SWORD, "警備員", 1));
                inv.setItem(2, setItem(Material.BREAD, "パン屋さん", 1));
                inv.setItem(3, setItem(Material.CAKE, "ケーキ屋さん", 1));
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
        if (itemName.equals("ホームレス")) {
            c.set(key, "");
            message = "ホームレスに戻りました。あなたは無職です";
        }
        if (itemName.equals("警備員")) {
            c.set(key, "警備員");
            message = "街をモンスターから守るお仕事です！";
        }
        if (itemName.equals("木こり")) {
            c.set(key, "木こり");
            message = "たくさんの原木を集めてください！";
        }
        if (itemName.equals("パン屋さん")) {
            c.set(key, "パン屋さん");
            message = "パンをたくさん作ってみんなに分けてね！";
        }
        if (itemName.equals("ケーキ屋さん")) {
            c.set(key, "ケーキ屋さん");
            message = "美味しいケーキをたくさん作ってください！";
        }

        // 仕事カウントをリセット
        String workCountKey = player.getUniqueId() + "-job-work-count";
        c.set(workCountKey, 0);

        // セットした設定を保存
        this.plugin.saveConfig();

        // スコアボード更新
        SidebarService sidebarService = new SidebarService(this.plugin, player);
        sidebarService.show();
        // インベントリ閉じてメッセージ
        this.player.closeInventory();
        this.player.sendTitle(itemName, message, 20, 20, 20);
    }

    /**
     * 給料受け取りのNpcクリック
     */
    public void clickReceiveSalary() {
        Player player = this.player;
        player.sendTitle("お給料計算中...", "", 20, 20, 20);
        FileConfiguration c = this.plugin.getConfig();
        Life plugin = this.plugin;
        new BukkitRunnable() {
            @Override
            public void run () {
                String key = player.getUniqueId() + "-job-type";
                String jobType = c.getString(key, "");
                String workCountKey = player.getUniqueId() + "-job-work-count";
                int workCount = c.getInt(workCountKey, 0); // お仕事完了回数
                int unitPrice = getWorkSalary(jobType); // お仕事の単価
                int salary = unitPrice * workCount; // 単価 x 完了 = お給料

                // 設定ファイルに書き込む
                MoneyService moneyService = new MoneyService(plugin, player);
                moneyService.addMoney(salary);

                // 仕事カウントを0にする
                c.set(workCountKey, 0);

                // サイドバー更新
                SidebarService sidebarService = new SidebarService(plugin, player);
                sidebarService.show();

                // メッセージ送る
                player.sendTitle(workCount + "回のお仕事", salary + "円のお給料をもらいました", 0, 40, 20);
                String[] messages = {
                        " ",
                        "□ 給料明細",
                        "単価: " + unitPrice + "円",
                        "完了数:" + workCount + "回",
                        "--",
                        "合計:" + salary + "円",
                        " "
                };
                player.sendMessage(messages);
            }
        }.runTaskLater(this.plugin, 60);
    }

    /**
     * お給料の単価を取得
     */
    private int getWorkSalary (String jobType) {
        if (jobType.equals("警備員")) return 50;
        if (jobType.equals("パン屋さん")) return 5;
        if (jobType.equals("ケーキ屋さん")) return 150;
        return 0;
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

    public static void doWork(Life plugin, Player player, String workType) {
        FileConfiguration c = plugin.getConfig();
        String key = player.getUniqueId() + "-job-type";
        String jobType = c.getString(key);

        String workCountKey = player.getUniqueId() + "-job-work-count";
        int workCount = c.getInt(workCountKey, 0);
        if (workType.equals("killedEntity") && jobType.equals("警備員")) {
            c.set(workCountKey, workCount + 1);
        }
        if (workType.equals("breakWood") && jobType.equals("木こり")) {
            c.set(workCountKey, workCount + 1);
        }
        if (workType.equals("createBread") && jobType.equals("パン屋さん")) {
            c.set(workCountKey, workCount + 1);
        }
        if (workType.equals("createCake") && jobType.equals("ケーキ屋さん")) {
            c.set(workCountKey, workCount + 1);
        }
        plugin.saveConfig();
    }
}
