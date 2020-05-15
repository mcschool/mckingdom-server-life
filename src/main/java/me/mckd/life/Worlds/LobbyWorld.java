package me.mckd.life.Worlds;

import me.mckd.life.Life;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
            player.setGameMode(GameMode.SURVIVAL);
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
        if (name.equals("goto Endless")) {
            player.performCommand("mvtp endless");
        }
        if (name.equals("ChangeMoney")) {
            this.openCashOffice(player);
        }
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
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            Bukkit.getLogger().info(item.toString());
            if (item.getType() != null) {
                price += this.getItemPrice(item);
            }
        }
        player.sendMessage(price + "円になりました");
    }

    public int getItemPrice(ItemStack item) {
        Material type = item.getType();
        if (type == Material.DIAMOND) return 1000;
        return 1;
    }
}

