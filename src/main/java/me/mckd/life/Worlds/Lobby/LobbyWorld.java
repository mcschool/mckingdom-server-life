package me.mckd.life.Worlds.Lobby;

import com.google.gson.JsonObject;
import me.mckd.life.Life;
import me.mckd.life.Utils.HttpReq;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LobbyWorld implements Listener{

    public String worldName = "lobby";
    private Life plugin;
    public int money = 0;

    public LobbyWorld(Life plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event) {
        if (event.getPlayer().getWorld().equals(this.worldName)) {
            Player player = event.getPlayer();
            World world = event.getPlayer().getWorld();
            Location location = event.getPlayer().getLocation();
            player.setMaxHealth(20);
            player.setHealth(player.getMaxHealth());

            // 管理者権限(admin)を持っている場合
            PermissionUser user = PermissionsEx.getUser(event.getPlayer());
            PermissionGroup permissionGroup = PermissionsEx.getPermissionManager().getGroup("admin");
            //管理者以上
            if (user.inGroup(permissionGroup)) {
                Location location1 = new Location(world, location.getX(), location.getY(), location.getZ());
                world.spawnParticle(Particle.DRAGON_BREATH, location1, 3, 1, 1, 1, 0);
                Location location3 = new Location(world, location.getX(), location.getY(), location.getZ());
                world.spawnParticle(Particle.CLOUD, location3, 10, 0.5, 0, 0.5, 0);
                Location location2 = new Location(world, location.getX(), location.getY(), location.getZ());
                world.spawnParticle(Particle.VILLAGER_HAPPY, location2, 3, 1, 1, 1, 0);
            }

            PermissionUser user2 = PermissionsEx.getUser(event.getPlayer());
            PermissionGroup permissionGroup2 = PermissionsEx.getPermissionManager().getGroup("vip");
            //VIP以上
            if (user2.inGroup(permissionGroup2)) {
                Location location2 = new Location(world, location.getX(), location.getY(), location.getZ());
                world.spawnParticle(Particle.HEART, location2, 3, 0.5, 0.5, 0.5, 0);
                Location location3 = new Location(world, location.getX(), location.getY(), location.getZ());
                world.spawnParticle(Particle.FLAME, location3, 3, 1, 1, 1, 0);
            }
        }
    }


    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event){
        Player player = event.getPlayer();
        if (player.getWorld().getName().equals(this.worldName)) {
            this.changeWorld(player);
            player.removePotionEffect(PotionEffectType.GLOWING);
            player.setGameMode(GameMode.SURVIVAL);
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.changeWorld(event.getPlayer());
        // Bossbar
        BossBar bossBar = this.plugin.getServer().createBossBar("★★ ようこそ MC LIFE SERVER へ ★★", BarColor.BLUE, BarStyle.SOLID);
        bossBar.addPlayer(event.getPlayer());
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        HttpReq req = new HttpReq();
        JsonObject obj = new JsonObject();
        obj.addProperty("uuid", event.getPlayer().getUniqueId().toString());
        obj.addProperty("name", event.getPlayer().getDisplayName());
        JsonObject response = req.post("/api/game/players", obj);
        System.out.println(response);
        /*
        int loginCount = Integer.parseInt(response.get("money").toString());
        System.out.println(loginCount);
        this.money = Integer.parseInt(response.get("money").toString());
         */
    }

    public void changeWorld(Player player) {
        player.performCommand("mvtp lobby");
        Location location = new Location(Bukkit.getWorld(this.worldName),-106.5,3,-1506.5);
        player.teleport(location);

        // タイトル
        player.sendTitle(
                ChatColor.GREEN + "M" + ChatColor.WHITE+"CKINGDOM",
                "Welcome To MCK", 60,80,60);
        if (player.getWorld().getName().equals(this.worldName)){
            // インベントリを一度空にする
            player.getInventory().clear();
            player.setFoodLevel(20);
            player.setHealth(20.0);
            player.setFlying(false);
            player.setGravity(true);
            player.setGameMode(GameMode.SURVIVAL);

            // ゲームメニュー用の "紙" 渡す
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("ゲームメニュー");
            itemStack.setItemMeta(itemMeta);
            player.getInventory().addItem(itemStack);

            player.sendMessage(ChatColor.YELLOW + "=====================");
            player.sendMessage("");
            player.sendMessage("★☆ お知らせ ☆★");
            player.sendMessage("LIFEサーバーは現在開発中のため不具合などが発生します");
            player.sendMessage("");
            player.sendMessage(ChatColor.YELLOW + "=====================");
        }
    }

    /**
     * インベントリをクリック
     * @param e
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws IOException {
        Player player = (Player) e.getWhoClicked();
        if(!player.getWorld().getName().equals(this.worldName)) return;

        return;
    }


    @EventHandler
    public  void PlayerUnleashEntityEvent(PlayerUnleashEntityEvent e){
        if(e.getEntity().getWorld().getName().equals(this.worldName)){
            Player p = e.getPlayer();
            e.setCancelled(true);
        }
    }

    /**
     * エンティティがダメージを受けた時
     * @param e
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity().getWorld().getName().equals(this.worldName)) {
            // ダメージを受けたエンティティがプレーヤーじゃなかったらreturn
            if (!(e.getEntity() instanceof Player)) {
                return;
            }
            if (e.getCause() != null && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
            return;
        }
    }


    /**
     * 空腹度が変わった時
     * @param event
     */
    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        // イベント情報からワールド名を取得する
        String worldname = event.getEntity().getWorld().getName();
        // もし現在のワールド名が自分のワールドだった場合
        if (worldname.equals(this.worldName)) {
            // イベントをキャンセルすることで空腹度を減らさないようにできる
            event.setCancelled(true);
            return;
        }
    }

    /**
     * チャット
     * @param event
     */
    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event){
        if (event.getPlayer().getWorld().getName().equals(this.worldName)) {
            //管理者とVIPでチャットするときの名前の文字の色を変える
            // 管理者権限(admin)を持っている場合
            PermissionUser user = PermissionsEx.getUser(event.getPlayer());
            PermissionGroup permissionGroup = PermissionsEx.getPermissionManager().getGroup("admin");
            //管理者以上
            if (user.inGroup(permissionGroup)) {
                event.setMessage(ChatColor.AQUA + event.getMessage());
            }

            PermissionUser user2 = PermissionsEx.getUser(event.getPlayer());
            PermissionGroup permissionGroup2 = PermissionsEx.getPermissionManager().getGroup("vip");
            //管理者以上
            if (user2.inGroup(permissionGroup2)) {
                event.setMessage(ChatColor.GREEN + event.getMessage());
            }
        }
    }

    /**
     * 燃え広がった時
     * @param event
     */
    @EventHandler
    public void onBlockSpreadEvent(BlockSpreadEvent event){
        Block block = event.getBlock();
        if (block.getWorld().getName().equals(this.worldName)) {
            if (block.getType() == Material.FIRE) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * 爆発キャンセル
     * @param event
     */
    @EventHandler
    public void ExplosionPrimeEvent(ExplosionPrimeEvent event) {
        if (event.getEntity().getWorld().getName().equals(this.worldName)) {
            event.setCancelled(true);
        }
    }

    /**
     * ブロックが設置させた時
     * @param event
     */
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getName().equals(this.worldName)) {
            if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * ブロックが壊された時
     * @param event
     */
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent event) {
        if (event.getPlayer().getWorld().getName().equals(this.worldName)) {
            if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                event.getPlayer().sendMessage("壊せないよ");
                event.setCancelled(true);
            }
        }
    }

    /**
     * 右クリックした場合のイベント
     * @param event
     */
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) throws IOException {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equals(this.worldName)) {
            Block block = event.getClickedBlock();
            World world = event.getPlayer().getWorld();
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                if(event.getMaterial() == Material.LEASH) {
                    event.setCancelled(true);
                }
            }

            if (event.getAction().equals(Action.RIGHT_CLICK_AIR)){

                if (event.getMaterial() == Material.PAPER) {
                    LobbyInventory lobbyInventory = new LobbyInventory();
                    player.openInventory(lobbyInventory.gameMenu());
                }
            }
        }
    }

    /**
     * アイテムを捨てられないようにする
     * @param e
     */
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if(player.getWorld().getName().equals(this.worldName)) {
            if(e.getPlayer().getGameMode() == GameMode.SURVIVAL){
                e.setCancelled(true);
            }
        }
    }

    /**
     * サーバーを移動する関数
     * @param player
     * @param serverName
     * @throws IOException
     */
    public void changeServer(Player player, String serverName) throws IOException {
        player.sendMessage(serverName + "に移動します");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF("Connect");
        dos.writeUTF(serverName); // サーバー名だけいれればOK
        player.sendPluginMessage(this.plugin, "BungeeCord", baos.toByteArray());
        baos.close();
        dos.close();
    }

}