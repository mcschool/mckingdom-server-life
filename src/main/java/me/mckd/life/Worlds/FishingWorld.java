package me.mckd.life.Worlds;

import me.mckd.life.Life;
import me.mckd.life.Services.SidebarService;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FishingWorld implements Listener{
    private Life plugin;
    public String worldname = "fishing";

    public FishingWorld(Life plugin){
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerWorldChangedEvent(PlayerChangedWorldEvent event){
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (!player.getWorld().getName().equals(this.worldname)) return;
        Location location = new Location(player.getWorld(),-1017,4,-1828);
        player.teleport(location);
        player.sendTitle("みんなで釣りゲーム", "",40,60,40);
        player.setGameMode(GameMode.ADVENTURE);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals(this.worldname)) {
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event){
        if (event.getEntity().getWorld().getName().equals(this.worldname)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals(this.worldname)){
            return;
        }
        String name = event.getRightClicked().getName();
        if (name.equals("ChangeMoney")){
            this.openCashOffice(player);
        }
        if (name.equals("ItemShop")){
            this.openItemShop(player);
        }
    }

    public void openCashOffice(Player player) {
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


    public void openItemShop(Player player){
        player.sendTitle("ようこそアイテムショップへ","釣りに必要なアイテムを購入しましょう",0,20,0);
        new BukkitRunnable(){
            @Override
            public void run () {
                Inventory inventory;
                inventory = Bukkit.createInventory(null, 45, "アイテムショップ");
                inventory.clear();
                inventory.setItem(2, setItem(Material.FISHING_ROD, "200円",1));
                player.openInventory(inventory);
            }
        }.runTaskLater(this.plugin, 20);
    }

    private ItemStack setItem(Material material, String name, int count){
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!player.getWorld().getName().equals(this.worldname)) {
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


        if (invName.equals("メニュー")) {
            if(e.getCurrentItem().getType() == (Material.DIAMOND)){
                player.sendMessage("You clicked a diamond in menu box");
            }
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        World world = e.getPlayer().getWorld();
        if (!world.getName().equals(this.worldname)) {
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
     *
     * @param player
     * @param inv
     */
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

    /**
     *
     * @param item
     * @return
     */
    public int getItemPrice(ItemStack item) {
        Material type = item.getType();
        if (type == Material.RAW_FISH) return item.getAmount() * 100;
        return item.getAmount() * 1;
    }

}
