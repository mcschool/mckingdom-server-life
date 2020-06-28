package me.mckd.life.Services;

import me.mckd.life.Life;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;


public class NormalGachaService {

    Life plugin;
    Player player;
    Location location;
    Inventory inv;
    ArrayList<ItemStack> items = new ArrayList<>();

    private int currentItemIndex = 0;

    public NormalGachaService(Life plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void run() {
        PlayerDataService playerDataService = new PlayerDataService(plugin, player);
        int normalGachaTicket = playerDataService.getNormalGachaTicket();
        if (normalGachaTicket > 0) {
            this.createInv();
            this.setItems();

            new BukkitRunnable() {
                int c = 0;
                @Override
                public void run() {

                    Random rand = new Random();
                    int n = rand.nextInt(items.size());
                    ItemStack i = items.get(n);
                    currentItemIndex = n;
                    inv.setItem(2, i);
                    player.updateInventory();
                    location.getWorld().playSound(location, Sound.BLOCK_NOTE_BELL, 1, 1);

                    c++;
                    if (c > 10) {
                        this.cancel();
                        spawn();
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        } else {
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "ノーマルガチャチケットがないのでガチャはできません");
            player.sendMessage("");
        }
    }

    public void animate() {
        Random rand = new Random();
        int n = rand.nextInt(this.items.size());
        ItemStack i = this.items.get(n);
        currentItemIndex = n;
        inv.setItem(2, i);
        player.updateInventory();
        location.getWorld().playSound(location, Sound.BLOCK_NOTE_BELL, 1, 1);
    }

    public void createInv() {
        this.inv = Bukkit.createInventory(null, InventoryType.HOPPER, "ノーマルガチャ");
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        inv.setItem(0, glass);
        inv.setItem(1, glass);
        inv.setItem(3, glass);
        inv.setItem(4, glass);
    }

    public void setItems() {
        items.add(new ItemStack(Material.WOOD, 32));
        items.add(new ItemStack(Material.IRON_BOOTS, 1));
        items.add(new ItemStack(Material.IRON_CHESTPLATE,1));
        items.add(new ItemStack(Material.IRON_HELMET, 1));
        items.add(new ItemStack(Material.IRON_LEGGINGS, 1));
        items.add(new ItemStack(Material.EXP_BOTTLE, 1));
    }

    public void click(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    public void spawn() {
        World world = player.getWorld();
        ItemStack item = this.items.get(this.currentItemIndex);
        world.dropItemNaturally(player.getLocation(), item);
    }
}
