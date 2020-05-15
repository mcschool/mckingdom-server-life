package me.mckd.life.Worlds;

import me.mckd.life.Life;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

public class LobbyWorld implements Listener {
    public String worldName = "lobby";

    public LobbyWorld(Life plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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

    public void openCashOffice(Player player) {
        Inventory inv;
        inv = Bukkit.createInventory(null, 9, "換金所");
        inv.clear();
        player.openInventory(inv);
    }
}

