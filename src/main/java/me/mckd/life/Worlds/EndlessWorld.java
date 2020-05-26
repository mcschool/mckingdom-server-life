package me.mckd.life.Worlds;

import me.mckd.life.Life;
import me.mckd.life.Services.SidebarService;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Date;

public class EndlessWorld implements Listener {

    Life plugin;
    String worldName = "endless";

    public EndlessWorld(Life plugin) {
        // コンストラクタ
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        World world = e.getEntity().getWorld();
        if (!world.getName().equals(this.worldName)) {
            return;
        }

        Player player = e.getEntity().getKiller();
        Entity entity = e.getEntity();

        // モンスターを倒したのがplayerじゃなかったら
        if (e.getEntity().getKiller() == null) {
            return;
        }
        Bukkit.getLogger().info("脂肪");
        Bukkit.getLogger().info(e.getEntity().getName());
        boolean isKilledMonster = false;

        if (entity.getType() == EntityType.ZOMBIE) isKilledMonster = true;

        if (isKilledMonster) {
            String key = player.getUniqueId() + "-killed-monster";

            FileConfiguration c = this.plugin.getConfig();
            int killedMonster = c.getInt(key);
            int newKilledMonster = killedMonster + 1;
            c.set(key, newKilledMonster);
            this.plugin.saveConfig();

            SidebarService sidebarService = new SidebarService(this.plugin, player);
            sidebarService.show();
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        String name = e.getRightClicked().getName();
        if (name.equals("goto Lobby")) {
            player.performCommand("mvtp lobby");
        }
        if (name.equals("Nether")){
            player.performCommand("mvtp nether");
        }
    }


    /**
     * ブロックが燃え広がる時
     * 放火防止
     * @param e
     */

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        if(e.getBlock().getWorld().getName().equals(this.worldName)){
            e.setCancelled(true);
        }
    }

    /*@EventHandler
    public void onBlockIgnite(BlockIgniteEvent e){
        if(e.getBlock().getWorld().getName().equals(this.worldName)){
            e.setCancelled(true);
        }
    }*/

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            e.setCancelled(true);
        }
        if (e.getEntity() instanceof Creeper) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        if (e.getBlock().getWorld().getName().equals(this.worldName)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void BlockPlaceEvent (BlockPlaceEvent event){
            Block block = event.getBlock();
            if (block.getType() == Material.CHEST){

            }
    }


}
