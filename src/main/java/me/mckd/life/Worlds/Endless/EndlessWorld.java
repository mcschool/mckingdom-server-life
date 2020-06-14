package me.mckd.life.Worlds.Endless;

import me.mckd.life.Life;
import me.mckd.life.Services.JobService;
import me.mckd.life.Services.SidebarService;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.NameTagVisibility;

import javax.naming.Name;
import java.io.IOException;
import java.util.jar.Attributes;

import static org.bukkit.Material.COOKED_BEEF;
import static org.bukkit.Material.WOOD;

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
        player.sendMessage(ChatColor.RED + "溶岩を使ったいたずらなどの荒らし行為はすぐにBAN対処します。");
        player.sendMessage(ChatColor.RED + "一人ひとりの行動・発言はログで取得できます。");
        player.sendMessage(ChatColor.BLUE + "みんなが気持ちよく遊べるように心がけてください");

        e.getPlayer().getInventory().setHeldItemSlot(8);
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("ゲームメニュー");
        itemStack.setItemMeta(itemMeta);
        player.getInventory().addItem(itemStack);

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

        // 倒したモンスターが以下のタイプだったら
        // フラグをオンtrueにする
        if (entity.getType() == EntityType.ZOMBIE) isKilledMonster = true;
        if (entity.getType() == EntityType.SKELETON) isKilledMonster = true;
        if (entity.getType() == EntityType.CREEPER) isKilledMonster = true;
        if (entity.getType() == EntityType.SPIDER) isKilledMonster = true;
        if (entity.getType() == EntityType.ENDERMAN) isKilledMonster = true;
        if (entity.getType() == EntityType.WITCH) isKilledMonster = true;

        // フラグがtrueだった場合、モンスターを倒した回数を増やす
        if (isKilledMonster) {
            String key = player.getUniqueId() + "-killed-monster";

            FileConfiguration c = this.plugin.getConfig();
            int killedMonster = c.getInt(key);
            int newKilledMonster = killedMonster + 1;
            c.set(key, newKilledMonster);
            this.plugin.saveConfig();

            // 仕事
            JobService.doWork(this.plugin, player, "killedEntity");

            SidebarService sidebarService = new SidebarService(this.plugin, player);
            sidebarService.show();
        }
    }

    /**
     * ブロック壊したとき
     * @param e
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        JobService.doWork(this.plugin, player, "breakWool");
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
        if (name.equals("End")){
            player.performCommand("mvtp world_the_end");
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

    /**
     * 何かをクラフトした時。とりあえず作業台しかテストしてない
     * @param e
     */
    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!player.getWorld().getName().equals(this.worldName)) {
            return;
        }
        Material material = e.getCurrentItem().getType();
        // パンを作成
        if (material == Material.BREAD) {
            JobService.doWork(this.plugin, player, "createBread");
        }
        // ケーキを作成
        if (material == Material.CAKE) {
            JobService.doWork(this.plugin, player, "createCake");
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) throws IOException {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equals(this.worldName)) {
            Block block = event.getClickedBlock();
            World world = event.getPlayer().getWorld();
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            }
            if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                if (event.getMaterial() == Material.LEASH) {
                    event.setCancelled(true);
                }
            }

            if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {

                if (event.getMaterial() == Material.PAPER) {
                    EndlessInventory lobbyInventory = new EndlessInventory();
                    player.openInventory(lobbyInventory.gameMenu());
                }
            }
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws IOException {
        Player player = (Player) e.getWhoClicked();
        if (!player.getWorld().getName().equals(this.worldName)) return;
        if (e.getCurrentItem().getType() == Material.LOG) {
            if (player.getUniqueId().toString().equals("4966b6cb-90e3-4b63-a523-cc62dc1e91ce")) {
                Location location = new Location(player.getWorld(), 131, 68, 193);
                player.teleport(location);
            }

            if (player.getUniqueId().toString().equals("b8438a10-825c-46a1-862e-f0f12323ee89")) {
                Location location = new Location(player.getWorld(), 87, 69, 325);
                player.teleport(location);
            }
            //e.setCancelled(true);
        }

        return;
    }



}
