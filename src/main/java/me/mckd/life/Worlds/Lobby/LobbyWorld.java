package me.mckd.life.Worlds.Lobby;

import me.mckd.life.Life;
import me.mckd.life.Services.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LobbyWorld implements Listener {

    private Life plugin;
    private String worldName = "lobby";
    private NormalGachaService normalGachaService;

    public LobbyWorld(Life plugin) {
        // コンストラクタ
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent e) {
        if (!e.getWorld().getName().equals(this.worldName)) {
            return;
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        this.changeWorld(event.getPlayer());
        Player player = event.getPlayer();
        if (player.getWorld().getName().equals(this.worldName)) {
            if ((player.getUniqueId().toString().equals("4966b6cb-90e3-4b63-a523-cc62dc1e91ce"))) {
                player.setPlayerListName("[Admin]" + player.getName());
            }
            if ((player.getUniqueId().toString().equals("e3c7490a-3d9a-4e7b-bd3e-80cf0a218219"))) {
                player.setPlayerListName("[Admin]" + player.getName());
            }
            player.sendMessage(ChatColor.RED+"溶岩を使ったいたずらなどの荒らし行為はすぐにBAN対処します。");
            player.sendMessage(ChatColor.RED+"一人ひとりの行動・発言はログで取得できます。");
            player.sendMessage(ChatColor.BLUE+"みんなが気持ちよく遊べるように心がけてください");

            this.loginCheck(player);

            // サイドバー更新
            SidebarService sidebarService = new SidebarService(this.plugin, player);
            sidebarService.show();
        }
    }

    // ログインチェック
    // ガチャチケとかあげる
    public void loginCheck(Player player) {
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonth().getValue();
        int day = now.getDayOfMonth();
        String dayFormat = String.valueOf(month) + "/" + String.valueOf(day);
        PlayerDataService playerDataService = new PlayerDataService(this.plugin, player);
        String lastLoginDay = playerDataService.getLastLoginDay();

        // 最終のログインが今日じゃない場合ガチャチケ渡す
        Bukkit.getLogger().info("dayFormat:" + dayFormat);
        Bukkit.getLogger().info("lastLoginDay:" + lastLoginDay);
        if (!lastLoginDay.equals(dayFormat)) {
            int normalGachaTicket = playerDataService.getNormalGachaTicket();
            playerDataService.setNormalGachaTicket(normalGachaTicket + 1);
            player.sendMessage("ガチャチケット1枚ゲット");
            playerDataService.setLastLoginDay(dayFormat);
        }
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equals(this.worldName)) {
            this.changeWorld(player);
            player.setGameMode(GameMode.ADVENTURE);
            player.getWorld().setPVP(false);
            String name = player.getDisplayName();
            player.setCustomName(name + " ★0");
            player.sendMessage(ChatColor.RED+"溶岩を使ったいたずらなどの荒らし行為はすぐにBAN対処します。");
            player.sendMessage(ChatColor.RED+"一人ひとりの行動・発言はログで取得できます。");
            player.sendMessage(ChatColor.BLUE+"みんなが気持ちよく遊べるように心がけてください");
        }
    }

    /**
     * ブロックを壊せないように
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }

    /**
     * ブロックを置けないように
     * ロビーはアドベンチャーだけど念の為
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }

    /**
     * ワールドに移動してきたときの処理
     */
    private void changeWorld(Player player) {
        player.performCommand("mvtp lobby");
        Location location = new Location(Bukkit.getWorld("lobby"), 389, 5.5, 305);
        player.teleport(location);
        player.setGameMode(GameMode.ADVENTURE);

        player.sendTitle("生活・経済サーバー", "みんなでのんびり生活クラフト", 20, 20, 20);

        SidebarService sidebarService = new SidebarService(this.plugin, player);
        sidebarService.show();

        new BukkitRunnable() {
            @Override
            public void run () {
                // 配列のstringを用意して一気にチャットにメッセージ送る
                String[] lines = {
                        "",
                        "-- お知らせ --",
                        "6/01(月) 職業にパン屋さん・ケーキ屋さんが追加されました",
                        "6/01(月) アイテムショップのラインナップが増えました",
                        "6/01(月) 一部バグfix",
                        "5/31(日) 生活鯖に釣りゲーが追加されました。魚釣って売れます。",
                        "5/31(日) 生活鯖のロビーが新しくなりました",
                        "5/30(土) 職業欄が追加されました(そのうち職業選択ができるようになるよ)",
                        "5/30(土) アイテムショップのラインナップが増えました",
                        "",
                        ""
                };
                player.sendMessage(lines);
            }
        }.runTaskLater(this.plugin, 60);
    }

    /**
     * 空腹度を減らさない
     * フードレベルが変化した時に呼ばれるイベント
     */
    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent e){
        if(e.getEntity().getWorld().getName().equals(this.worldName)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        Bukkit.getLogger().info("........A:" + e.getEventName());
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = e.getClickedBlock();
            Bukkit.getLogger().info(block.toString());
            Bukkit.getLogger().info("........B:" + e.getEventName());
            if (block.getType() == Material.ENDER_PORTAL_FRAME) {
                Location location = e.getClickedBlock().getLocation();
                this.normalGachaService = new NormalGachaService(this.plugin, player);
                this.normalGachaService.run(location);
            }
        }
    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        String name = e.getRightClicked().getName();
        if (name.equals("メインロビー")) {
            this.gotoMainLobby(player);
        }
        if (name.equals("goto Endless")) {
            player.performCommand("mvtp endless");
        }
        if (name.equals("ChangeMoney")) {
            this.openCashOffice(player);
        }
        if (name.equals("ItemShop")) {
            this.openItemShop(player);
        }
        if (name.equals("Menu")) {
            this.openMenu(player);
        }
        if (name.equals("職業選択")) {
            JobService jobService = new JobService(this.plugin, player);
            jobService.openJobSelect();
        }
        if (name.equals("給料受取")) {
            JobService jobService = new JobService(this.plugin, player);
            jobService.clickReceiveSalary();
        }
        if (name.equals("銀行")) {
            BankService bankService = new BankService(this.plugin, player);
            bankService.open();
        }
        if (name.equals("クエストショップ")){
            QuestService questService = new QuestService(this.plugin, player);
            questService.open();
        }
        if (name.equals("Fishing")){
            player.performCommand("mvtp fishing");
        }
    }

    /**
     * サーバー間の移動をするためmvtpではダメで少し特殊な方法
     */
    private void gotoMainLobby(Player player) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF("Connect");
            dos.writeUTF("lobby");
            player.sendPluginMessage(this.plugin, "BungeeCord", baos.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * アイテムショップを右クリックしたときにショップ開く
     */
    private void openItemShop(Player player) {
        if (player == null) {
            return;
        }
        player.sendTitle("ようこそ アイテムショップ へ", "ゲットしたお金でアイテムを購入できます",0, 20, 0);
        // 1秒遅延させてショップ開く。なんとなく..
        new BukkitRunnable() {
            @Override
            public void run () {
                Inventory inv;
                inv = Bukkit.createInventory(null, 45, "アイテムショップ");
                inv.clear();
                inv.setItem(0, setItem(Material.WOOD, "160円", 32));
                inv.setItem(1, setItem(Material.LOG, "320円", 16));
                inv.setItem(2, setItem(Material.DIAMOND, "2500円", 1));
                inv.setItem(3, setItem(Material.APPLE, "100円", 1));
                inv.setItem(4, setItem(Material.CHEST, "80円", 1));
                inv.setItem(5, setItem(Material.CLAY, "320円", 16));
                inv.setItem(6, setItem(Material.IRON_INGOT, "200円", 1));
                inv.setItem(7, setItem(Material.GOLDEN_APPLE, "1250円", 1));
                inv.setItem(8, setItem(Material.BREAD, "300円", 10));

                inv.setItem(9, setItem(Material.RAILS, "320円", 16));
                inv.setItem(10, setItem(Material.POWERED_RAIL, "640円", 4));
                inv.setItem(11, setItem(Material.BOOK, "570円", 1));
                inv.setItem(12, setItem(Material.EMERALD, "10000円", 1));
                inv.setItem(13, setItem(Material.BLAZE_ROD, "1000円", 1));

                inv.setItem(18, setItem(Material.IRON_BOOTS, "800円", 1));
                inv.setItem(19, setItem(Material.IRON_HELMET, "1000円", 1));
                inv.setItem(20, setItem(Material.IRON_CHESTPLATE, "1600円", 1));
                inv.setItem(21, setItem(Material.IRON_LEGGINGS, "1400円", 1));
                player.openInventory(inv);
            }
        }.runTaskLater(this.plugin, 20);
    }

    /**
     * Menuをクリックした場合インベントリを開く
     */
    private void openMenu(Player player){
        player.sendTitle("メニュー", "プレーヤーメニュー",0, 20, 0);
        new BukkitRunnable() {
            @Override
            public void run () {
                String key = player.getUniqueId() + "-money";
                FileConfiguration c = plugin.getConfig();
                int currentMoney = c.getInt(key);
                // player.sendMessage(":" + currentMoney);

                // TODO: ランク確認 テストあとで消す
                String rankKey = player.getUniqueId() + "-rank";
                int currentRank = plugin.getConfig().getInt(rankKey);
                player.sendMessage("rank:" + currentRank);

                Inventory inv;
                inv = Bukkit.createInventory(null, 45, "メニュー");
                inv.clear();
                inv.setItem(0, setItem(Material.DIAMOND, "ランクアップ", 1));
            }
        }.runTaskLater(this.plugin, 20);
    }

    /**
     * アイテムを生成するのに手順ふむの面倒なので関数化
     */
    private ItemStack setItem(Material material, String name, int count) {
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta itemMeta  = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * 換金するインベントリを開く
     */
    private void openCashOffice(Player player) {
        player.sendTitle("ようこそ 換金所 へ", "ゲットしたアイテムをお金に変えることができます",0, 20, 0);
        new BukkitRunnable() {
            @Override
            public void run () {
                Inventory inv;
                inv = Bukkit.createInventory(null, 9, "換金所");
                inv.clear();
                player.openInventory(inv);
            }
        }.runTaskLater(this.plugin, 20);
    }


    /**
     * 開いているショップのインベントリをクリックしたとき
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }

        Inventory inv = e.getInventory();
        String invName = inv.getName();
        if (invName.equals("ノーマルガチャ")) {
            // NormalGachaInventoryService.run(e);
            this.normalGachaService.click(e);
        }
        if (invName.equals("アイテムショップ")) {
            String key = player.getUniqueId() + "-money";
            FileConfiguration c = this.plugin.getConfig();
            int currentMoney = c.getInt(key, 0);

            String itemName = "";
            if (e.getCurrentItem().hasItemMeta()) {
                itemName = e.getCurrentItem().getItemMeta().getDisplayName();
            }
            if (itemName.equals("")) return;
            if (e.getRawSlot() > 45) return;

            int price = Integer.parseInt(itemName.replace("円", ""));
            if (currentMoney < price) {
                Bukkit.getLogger().info("お金が足りません");
                e.setCancelled(true);
                return;
            }

            int myMoney = c.getInt(key, 0);
            int nextMoney = myMoney - price;
            c.set(key, nextMoney);
            this.plugin.saveConfig();
            player.sendMessage("所持金が" + nextMoney + "円になりました");
            // サイドバー更新
            SidebarService sidebarService = new SidebarService(this.plugin, player);
            sidebarService.show();
        }


        if (invName.equals("メニュー")) {
            if(e.getCurrentItem().getType() == (Material.DIAMOND)){
                player.sendMessage("You clicked a diamond in menu box");
            }
        }


        if (invName.equals("職業選択")) {
            JobService jobService = new JobService(this.plugin, player);
            jobService.clickJobInventory(e);
        }

    }




    /**
     * インベントリを閉じるときに呼ばれる
     */
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        World world = e.getPlayer().getWorld();
        if (!world.getName().equals(this.worldName)) {
            return;
        }
        Player player = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        String invName = inv.getName();
        if (invName.equals("換金所")) {
            this.changeMoney(player, inv);
        }
    }

    /**
     * 換金処理
     */
    private void changeMoney(Player player, Inventory inv) {
        int price = 0;
        List<String> m = new ArrayList<String>();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null) {
                int p = this.getItemPrice(item);
                price += p;
                m.add(item.getType().name() + "x" + item.getAmount() + "=" + p);
            }
        }
        if (m.size() > 0) {
            // データ保存
            String key = player.getUniqueId() + "-money";
            FileConfiguration c = this.plugin.getConfig();
            int myMoney = c.getInt(key, 0);
            int nextMoney = myMoney + price;
            c.set(key, nextMoney);
            this.plugin.saveConfig();

            // サイドバー更新
            SidebarService sidebarService = new SidebarService(this.plugin, player);
            sidebarService.show();

            // メッセージ整形
            for (String s: m) {
                player.sendMessage(s);
            }
            player.sendMessage("---");
            player.sendMessage(price + "円になりました");
            player.sendMessage("所持金:" + nextMoney + "円");
        } else {
            player.sendMessage("アイテムを選択していないので何も売りませんでした");
        }
    }

    /**
     * アイテムの売価
     * あとで別クラスに分けた方が良さげ
     */
    private int getItemPrice(ItemStack item) {
        Material type = item.getType();
        if (type == Material.DIAMOND) return item.getAmount() * 1000;
        if (type == Material.EMERALD) return item.getAmount() * 1500;
        if (type == Material.IRON_INGOT) return item.getAmount() * 100;
        if (type == Material.GOLD_INGOT) return item.getAmount() * 250;
        if (type == Material.REDSTONE) return item.getAmount() * 20;
        if (type == Material.COAL) return item.getAmount() * 10;
        if (type == Material.GLASS) return item.getAmount() * 10;

        return item.getAmount();
    }

    // バケツから水とかまいた時
    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        e.setCancelled(true);
    }

    // 水組めないようにする
    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        e.setCancelled(true);
    }
}

