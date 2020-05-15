package me.mckd.life.Worlds;

import me.mckd.life.Life;
import me.mckd.life.Services.SidebarService;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class LobbyWorld implements Listener {

    private Life plugin;
    public String worldName = "lobby";


    public LobbyWorld(Life plugin) {
        // コンストラクタ
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();
        this.changeWorld(event.getPlayer());
    }
    

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equals(this.worldName)) {
            this.changeWorld(player);
            player.setGameMode(GameMode.ADVENTURE);
            player.getWorld().setPVP(false);
        }
    }

    /**
     * ブロックを壊せないように
     * @param e
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
     * @param e
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
     * @param player
     */
    public void changeWorld(Player player) {
        player.performCommand("mvtp lobby");
        Location location = new Location(Bukkit.getWorld("lobby"), 387, 10, 393);
        player.teleport(location);
        player.setGameMode(GameMode.ADVENTURE);

        player.sendTitle("生活・経済サーバー", "みんなでのんびり生活クラフト", 20, 20, 20);

        SidebarService sidebarService = new SidebarService(this.plugin, player);
        sidebarService.show();
    }

    /**
     * 空腹度を減らさない
     * フードレベルが変化した時に呼ばれるイベント
     * @param e
     */
    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent e){
        if(e.getEntity().getWorld().getName().equals(this.worldName)){
            e.setCancelled(true);
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
    }

    public void gotoMainLobby(Player player) {
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

    public void openItemShop(Player player) {
        player.sendTitle("ようこそ アイテムショップ へ", "ゲットしたお金でアイテムを購入できます",0, 20, 0);
        new BukkitRunnable() {
            @Override
            public void run () {
                Inventory inv;
                inv = Bukkit.createInventory(null, 45, "アイテムショップ");
                inv.clear();
                inv.setItem(0, setItem(Material.WOOD, "320円", 32));
                inv.setItem(1, setItem(Material.DIAMOND, "1500円", 1));
                inv.setItem(2, setItem(Material.APPLE, "100円", 1));
                inv.setItem(3, setItem(Material.CHEST, "80円", 1));
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

    /**
     * 換金するインベントリを開く
     * @param player
     */
    public void openCashOffice(Player player) {
        Inventory inv;
        inv = Bukkit.createInventory(null, 9, "換金所");
        inv.clear();
        player.sendTitle("ようこそ 換金所 へ", "ゲットしたアイテムをお金に変えることができます",0, 20, 0);
        new BukkitRunnable() {
            @Override
            public void run () {
                player.openInventory(inv);
            }
        }.runTaskLater(this.plugin, 20);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }

        Inventory inv = e.getInventory();
        String invName = inv.getName();
        if (invName.equals("アイテムショップ")) {
            String key = player.getUniqueId() + "-money";
            FileConfiguration c = this.plugin.getConfig();
            int currentMoney = c.getInt(key);

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

            int myMoney = c.getInt(key);
            int nextMoney = myMoney - price;
            c.set(key, nextMoney);
            this.plugin.saveConfig();
            player.sendMessage("所持金が" + nextMoney + "円になりました");
            // サイドバー更新
            SidebarService sidebarService = new SidebarService(this.plugin, player);
            sidebarService.show();
        }
    }

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

    public void changeMoney(Player player, Inventory inv) {
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
            int myMoney = c.getInt(key);
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

    public int getItemPrice(ItemStack item) {
        Material type = item.getType();
        if (type == Material.DIAMOND) return item.getAmount() * 1000;
        if (type == Material.IRON_INGOT) return item.getAmount() * 100;
        if (type == Material.COAL) return item.getAmount() * 10;
        if (type == Material.GLASS) return item.getAmount() * 10;

        return item.getAmount() * 1;
    }
}

