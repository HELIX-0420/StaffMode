package me.chelsea1124.staffmode.Commands;

import java.util.*;

import me.chelsea1124.staffmode.GUIS.FreezeGUI;
import me.chelsea1124.staffmode.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StaffMode
        implements CommandExecutor, Listener {
    private Main main;
    private Map<UUID, Location> storedLocation = new HashMap();
    private ArrayList<String> frozen = new ArrayList();
    public List<String> players = new ArrayList();
    private Map<String, ItemStack[]> inventories = new HashMap();
    private Map<String, ItemStack[]> armor = new HashMap();

    public StaffMode(Main main) {
        this.main = main;
    }

    public List<String> getPlayers() {
        return this.players;
    }

    public Map<String, ItemStack[]> getInventories() {
        return this.inventories;
    }

    public Map<String, ItemStack[]> getArmor() {
        return this.armor;
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to execute this command.");
            return true;
        }
        Player p = (Player) commandSender;
        if (!p.hasPermission("StaffMode.Allow")) {
            p.sendMessage(color(this.main.getConfig().getString("no-Perm-staffchat")).replace("%PREFIX%", this.main.prefix));
            return false;
        }
        if (!getPlayers().contains(p.getName())) {
            add(p.getName());
            p.sendMessage(color(this.main.getConfig().getString("Staffmode.Enable")).replace("%PREFIX%", this.main.prefix));
            return true;
        }
        if (getPlayers().contains(p.getName())) {
            remove(p.getName());
            p.sendMessage(color(this.main.getConfig().getString("Staffmode.Disable")).replace("%PREFIX%", this.main.prefix));
        }
        return true;
    }

    public void add(String name) {
        Player p = Bukkit.getPlayer(name);
        this.players.add(name);
        p.getLocation();
        this.inventories.put(name, p.getInventory().getContents());
        this.armor.put(name, p.getInventory().getArmorContents());

        this.storedLocation.put(p.getUniqueId(), p.getLocation());

        p.getInventory().clear();
        p.setGameMode(GameMode.SURVIVAL);

        ItemStack Helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemMeta Helmetmeta = Helmet.getItemMeta();
        Helmetmeta.setDisplayName(color("&cStaffMode"));
        Helmet.setItemMeta(Helmetmeta);
        Helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        p.getInventory().setHelmet(Helmet);

        ItemStack ChestPlate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta ChestPlatemeta = ChestPlate.getItemMeta();
        ChestPlatemeta.setDisplayName(color("&cStaffMode"));
        ChestPlate.setItemMeta(ChestPlatemeta);
        ChestPlate.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        p.getInventory().setChestplate(ChestPlate);

        ItemStack Leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemMeta Leggingsemeta = Leggings.getItemMeta();
        Leggingsemeta.setDisplayName(color("&cStaffMode"));
        Leggings.setItemMeta(Leggingsemeta);
        Leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        p.getInventory().setLeggings(Leggings);

        ItemStack Boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta Bootsemeta = Boots.getItemMeta();
        Bootsemeta.setDisplayName(color("&cStaffMode"));
        Boots.setItemMeta(Bootsemeta);
        Boots.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        p.getInventory().setBoots(Boots);

        ItemStack Teleport = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("randomteleport.RandomTeleportItem")));
        ItemMeta TeleportM = Teleport.getItemMeta();
        TeleportM.setDisplayName(color(this.main.getConfig().getString("randomteleport.RandomTeleportItemName")));
        List randomteleportl = new ArrayList();
        for (String randomteleportL : this.main.getConfig().getStringList("randomteleport.lore")) {
            randomteleportl.add(ChatColor.translateAlternateColorCodes('&', randomteleportL));
        }
        TeleportM.setLore(randomteleportl);
        Teleport.setItemMeta(TeleportM);

        ItemStack Vanish = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("vanish.VanishOffItem")));
        ItemMeta VanishM = Vanish.getItemMeta();
        VanishM.setDisplayName(color(this.main.getConfig().getString("vanish.VanishOffItemName")));
        List vanishl = new ArrayList();
        for (String vanishL : this.main.getConfig().getStringList("vanish.lore")) {
            vanishl.add(ChatColor.translateAlternateColorCodes('&', vanishL));
        }
        VanishM.setLore(vanishl);
        Vanish.setItemMeta(VanishM);

        ItemStack Fly = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("togglefly.ToggleFlyOffItem")));
        ItemMeta FlyM = Fly.getItemMeta();
        FlyM.setDisplayName(color(this.main.getConfig().getString("togglefly.ToggleFlyOffItemName")));
        List flylorel = new ArrayList();
        for (String flyloreL : this.main.getConfig().getStringList("togglefly.lore")) {
            flylorel.add(ChatColor.translateAlternateColorCodes('&', flyloreL));
        }
        FlyM.setLore(flylorel);
        Fly.setItemMeta(FlyM);

        ItemStack Inventory = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("inventorypreview.InventoryPreviewItem")));
        ItemMeta InventoryM = Inventory.getItemMeta();
        InventoryM.setDisplayName(color(this.main.getConfig().getString("inventorypreview.InventoryPreviewItemName")));
        List InventoryLore = new ArrayList();
        for (String InventoryL : this.main.getConfig().getStringList("inventorypreview.lore")) {
            InventoryLore.add(ChatColor.translateAlternateColorCodes('&', InventoryL));
        }
        InventoryM.setLore(InventoryLore);
        Inventory.setItemMeta(InventoryM);

        ItemStack Zombie = new ItemStack(Material.MONSTER_EGG, 1, (short) this.main.getConfig().getInt("mob.MobItem"));
        ItemMeta ZombieM = Zombie.getItemMeta();
        ZombieM.setDisplayName(color(this.main.getConfig().getString("mob.MobItemName")));
        List ZombueLore = new ArrayList();
        for (String zombielore : this.main.getConfig().getStringList("mob.lore")) {
            ZombueLore.add(ChatColor.translateAlternateColorCodes('&', zombielore));
        }
        ZombieM.setLore(ZombueLore);
        Zombie.setItemMeta(ZombieM);

        ItemStack Freeze = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("freeze.FreezeItem")));
        ItemMeta FreezeM = Freeze.getItemMeta();
        FreezeM.setDisplayName(color(this.main.getConfig().getString("freeze.FreezeItemName")));
        List FreezeLore = new ArrayList();
        for (String freezeloere : this.main.getConfig().getStringList("freeze.lore")) {
            FreezeLore.add(ChatColor.translateAlternateColorCodes('&', freezeloere));
        }
        FreezeM.setLore(FreezeLore);
        Freeze.setItemMeta(FreezeM);

        ItemStack fill = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta fillmeta = fill.getItemMeta();
        fillmeta.setDisplayName(ChatColor.RED + "<EMPTY SLOT>");
        fill.setItemMeta(fillmeta);

        p.getInventory().setItem(0, Fly);
        p.getInventory().setItem(1, Inventory);
        p.getInventory().setItem(2, Vanish);
        p.getInventory().setItem(3, fill);
        p.getInventory().setItem(4, Teleport);
        p.getInventory().setItem(5, fill);
        p.getInventory().setItem(6, fill);
        p.getInventory().setItem(7, Zombie);
        p.getInventory().setItem(8, Freeze);
        for (Player vanish : Bukkit.getServer().getOnlinePlayers()) {
            vanish.hidePlayer(p);
        }
        p.sendMessage(color(this.main.getConfig().getString("vanish.VanishEnabledMessage")));
    }

    public void remove(String name) {
        Player p = Bukkit.getPlayer(name);
        p.getInventory().clear();
        p.teleport((Location) this.storedLocation.get(p.getUniqueId()));
        this.players.remove(name);

        this.storedLocation.get(p.getUniqueId());

        p.setAllowFlight(false);
        p.getInventory().setArmorContents((ItemStack[]) this.armor.get(name));
        p.getInventory().setContents((ItemStack[]) this.inventories.get(name));
        this.armor.remove(name);
        this.inventories.remove(name);
        p.setGameMode(GameMode.SURVIVAL);
        for (Player vanish : Bukkit.getServer().getOnlinePlayers()) {
            vanish.showPlayer(p);
        }
        p.sendMessage(color(this.main.getConfig().getString("vanish.VanishDisabledMessage")));
    }

    @EventHandler
    public void leave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (this.players.contains(player.getName())) {
            Player p = e.getPlayer();
            p.getInventory().clear();
            p.teleport((Location) this.storedLocation.get(p.getUniqueId()));
            this.players.remove(p.getName());

            this.storedLocation.get(p.getUniqueId());

            p.setAllowFlight(false);
            p.getInventory().setArmorContents((ItemStack[]) this.armor.get(p.getName()));
            p.getInventory().setContents((ItemStack[]) this.inventories.get(p.getName()));
            this.armor.remove(p.getName());
            this.inventories.remove(p.getName());
            p.setGameMode(GameMode.SURVIVAL);
            for (Player vanish : Bukkit.getServer().getOnlinePlayers()) {
                vanish.showPlayer(player);
            }
        }
    }

    @EventHandler
    public void ih(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if ((e.getInventory().getTitle().equals(color("&cFreeze Menu"))) && (e.getRawSlot() >= 0)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getPlayer().getItemInHand();
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) || ((e.getAction() == Action.RIGHT_CLICK_AIR) && (this.players.contains(player.getName())))) {
            if (e.getItem() == null) {
                return;
            }
            if ((e.getItem().getItemMeta().hasDisplayName()) && (e.getItem().getItemMeta().getDisplayName().equals(color(this.main.getConfig().getString("togglefly.ToggleFlyOnItemName"))))) {
                ItemStack fly = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("togglefly.ToggleFlyOffItem")));
                ItemMeta flyMeta = fly.getItemMeta();
                flyMeta.setDisplayName(color(this.main.getConfig().getString("togglefly.ToggleFlyOffItemName")));
                List flyL = new ArrayList();
                for (String flyl : this.main.getConfig().getStringList("togglefly.lore")) {
                    flyL.add(ChatColor.translateAlternateColorCodes('&', flyl));
                }
                flyMeta.setLore(flyL);
                fly.setItemMeta(flyMeta);
                player.getInventory().setItem(0, fly);
                player.setAllowFlight(true);
                player.sendMessage(color(this.main.getConfig().getString("togglefly.VanishDisabledMessage")));
            }
            if ((e.getItem().getItemMeta().hasDisplayName()) && (e.getItem().getItemMeta().getDisplayName().equals(color(this.main.getConfig().getString("togglefly.ToggleFlyOffItemName"))))) {
                ItemStack fly = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("togglefly.ToggleFlyOnItem")));
                ItemMeta flyeMeta = fly.getItemMeta();
                flyeMeta.setDisplayName(color(this.main.getConfig().getString("togglefly.ToggleFlyOnItemName")));
                List flyL = new ArrayList();
                for (String flyl : this.main.getConfig().getStringList("togglefly.lore")) {
                    flyL.add(ChatColor.translateAlternateColorCodes('&', flyl));
                }
                flyeMeta.setLore(flyL);
                fly.setItemMeta(flyeMeta);
                player.getInventory().setItem(0, fly);
                player.setAllowFlight(false);
            }
        }
        if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) && (this.players.contains(player.getName()))) {
            if (e.getItem() == null) {
                return;
            }
            if ((e.getItem().getItemMeta().hasDisplayName()) && (e.getItem().getItemMeta().getDisplayName().equals(color(this.main.getConfig().getString("mob.MobItemName"))))) {
                ItemStack zombieEgg = new ItemStack(Material.MONSTER_EGG, 1, (short) this.main.getConfig().getInt("mob.MobItem"));
                ItemMeta zombieEggMeta = zombieEgg.getItemMeta();
                zombieEggMeta.setDisplayName(color(this.main.getConfig().getString("mob.MobItemName")));
                List mobL = new ArrayList();
                for (String mobl : this.main.getConfig().getStringList("mob.lore")) {
                    mobL.add(ChatColor.translateAlternateColorCodes('&', mobl));
                }
                zombieEggMeta.setLore(mobL);
                zombieEgg.setItemMeta(zombieEggMeta);
                player.getInventory().setItem(7, zombieEgg);
            }
            if ((e.getItem().getItemMeta().hasDisplayName()) && (e.getItem().getItemMeta().getDisplayName().equals(color(this.main.getConfig().getString("mob.MobItemName"))))) {
                ItemStack zombiereplace = new ItemStack(Material.MONSTER_EGG, 1, (short) this.main.getConfig().getInt("mob.MobItem"));
                ItemMeta zombiereplaceMeta = zombiereplace.getItemMeta();
                List mobll = new ArrayList();
                for (String mobl : this.main.getConfig().getStringList("mob.lore")) {
                    mobll.add(ChatColor.translateAlternateColorCodes('&', mobl));
                }
                zombiereplaceMeta.setLore(mobll);
                zombiereplaceMeta.setDisplayName(color(this.main.getConfig().getString("mob.MobItemName")));

                zombiereplace.setItemMeta(zombiereplaceMeta);
                player.getInventory().setItem(7, zombiereplace);
                player.updateInventory();
            }
        }

        ItemMeta vanishMeta;
        if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) && (this.players.contains(player.getName()))) {
            if (e.getItem() == null) {
                return;
            }
            if ((e.getItem().getItemMeta().hasDisplayName()) && (e.getItem().getItemMeta().getDisplayName().equals(color(this.main.getConfig().getString("vanish.VanishOffItemName"))))) {
                ItemStack v2 = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("vanish.VanishOffItem")));
                ItemMeta v2Meta = v2.getItemMeta();
                v2Meta.setDisplayName(color(this.main.getConfig().getString("vanish.VanishOnItemName")));
                List mobL = new ArrayList();
                for (String mobl : this.main.getConfig().getStringList("mob.lore")) {
                    mobL.add(ChatColor.translateAlternateColorCodes('&', mobl));
                }
                v2Meta.setLore(mobL);
                v2.setItemMeta(v2Meta);
                player.getInventory().setItem(2, v2);
                for (Player vanish : Bukkit.getServer().getOnlinePlayers()) {
                    vanish.showPlayer(player);
                }
                player.sendMessage(color(this.main.getConfig().getString("vanish.VanishDisabledMessage")));
            }
            if ((e.getItem().getItemMeta().hasDisplayName()) && (e.getItem().getItemMeta().getDisplayName().equals(color(this.main.getConfig().getString("vanish.VanishOnItemName"))))) {
                ItemStack vanish = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("vanish.VanishOnItem")));
                vanishMeta = vanish.getItemMeta();
                vanishMeta.setDisplayName(color(this.main.getConfig().getString("vanish.VanishOffItemName")));
                List vanishL = new ArrayList();
                for (String vanishl : this.main.getConfig().getStringList("mob.lore")) {
                    vanishL.add(ChatColor.translateAlternateColorCodes('&', vanishl));
                }
                vanishMeta.setLore(vanishL);
                vanish.setItemMeta(vanishMeta);
                player.getInventory().setItem(2, vanish);
                player.updateInventory();
                for (Player vanished : Bukkit.getServer().getOnlinePlayers()) {
                    vanished.hidePlayer(player);
                }
                player.sendMessage(color(this.main.getConfig().getString("vanish.VanishEnabledMessage")));
            }
        }

        if (((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR)) &&
                (this.players.contains(player.getName())) &&
                (player.getItemInHand().getType().equals(Material.EYE_OF_ENDER))) {
            e.setCancelled(true);
        }
        if (((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR)) &&
                (this.players.contains(player.getName())) &&
                (player.getItemInHand().getType().equals(Material.DIAMOND_SWORD))) {
            e.setCancelled(true);
        }
        if (((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR)) &&
                (this.players.contains(player.getName())) &&
                (player.getItemInHand().getType().equals(Material.IRON_SWORD))) {
            e.setCancelled(true);
        }

        if ((e.getItem().getItemMeta().hasDisplayName()) && (e.getItem().getItemMeta().getDisplayName().equals(color(this.main.getConfig().getString("randomteleport.RandomTeleportItemName"))))) {

            if (e.getItem() == null) {
                return;
            }

            if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) && (this.players.contains(player.getName()))) {

                if (e.getItem() == null) {
                    return;
                }

                Player p = e.getPlayer();
                Random r = new Random();
                ArrayList<Player> players = new ArrayList<Player>();

                for(Player online : Bukkit.getServer().getOnlinePlayers()) {

                    if(online != p){
                        if(!online.hasPermission("staffmode.nottpable")){
                            players.add(online);
                        }
                    }

                }

                int index = r.nextInt(players.size() );
                Player loc = (Player) players.get(index);
                p.teleport(loc);
                p.sendMessage(color(this.main.getConfig().getString("randomteleport.RandomTpMessage")).replace("%PLAYER%", loc.getName()));


            }
        }
    }


    @EventHandler
    public void ONE(PlayerInteractEntityEvent e) {
        if ((e.getRightClicked() instanceof Player)) {
            Player player = e.getPlayer();
            Player target = (Player) e.getRightClicked();
            ItemStack item = e.getPlayer().getItemInHand();
            if ((item != null) && (this.players.contains(player.getName()))) {
                if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(color(this.main.getConfig().getString("freeze.FreezeItemName")))) {
                    if (this.frozen.contains(target.getName())) {
                        this.frozen.remove(target.getName());
                        target.closeInventory();
                        player.sendMessage(color(this.main.getConfig().getString("freeze.PlayerUnFrozen")).replace("%PLAYER%", target.getName()).replace("%PREFIX%", this.main.prefix));
                        return;
                    }
                    this.frozen.add(target.getName());
                    for (String PlayerFrozenMSG : this.main.getConfig().getStringList("freeze.MessageSentToPlayer".replace("%PLAYER%", target.getName().replace("%PREFIX%", this.main.prefix)))) {
                        target.sendMessage(color(PlayerFrozenMSG));
                    }
                    player.sendMessage(color(this.main.getConfig().getString("freeze.PlayerFrozen")).replace("%PLAYER%", target.getName()).replace("%PREFIX%", this.main.prefix));
                    return;
                }
                if (item.getItemMeta().getDisplayName().equalsIgnoreCase(color(this.main.getConfig().getString("inventorypreview.InventoryPreviewItemName")))) {
                    player.openInventory(target.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void onPickUP(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if ((this.main.getConfig().getBoolean("features.PickUpItems") == true) &&
                (this.players.contains(player.getName()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if ((this.main.getConfig().getBoolean("features.DropItems") == true) &&
                (this.players.contains(player.getName()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlokcBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if ((this.main.getConfig().getBoolean("features.BlockBreak") == true) &&
                (this.players.contains(player.getName()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlokcBreak(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (this.players.contains(player.getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void Invin(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if ((this.main.getConfig().getBoolean("features.InventoryMove") == true) &&
                (this.players.contains(player.getName()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location loc = e.getFrom();
        if ((this.frozen.contains(player.getName())) && (
                (e.getTo().getY() != loc.getY()) || (e.getTo().getX() != loc.getX()) || (e.getTo().getZ() != loc.getZ()))) {
            loc.setPitch(e.getTo().getPitch());
            loc.setYaw(e.getTo().getYaw());
            player.teleport(loc);
        }
    }

    @EventHandler
    public void onChat(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (this.players.contains(e.getPlayer().getName())) {
            List<String> cmds = this.main.getConfig().getStringList("Staffmode.BlockedCommands");
            for (String command : cmds) {
                if (e.getMessage().equalsIgnoreCase("/" + command)) {
                    e.setCancelled(true);
                    player.sendMessage(color(this.main.getConfig().getString("Staffmode.BlockedCommandsMessage").replace("%PREFIX%", this.main.prefix)));
                }
            }
        }
    }

    @EventHandler
    public void OnDamage(EntityDamageEvent e) {
        Entity player = e.getEntity();
        if (((e.getEntity() instanceof Player)) &&
                (this.players.contains(e.getEntity().getName()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void CantDamage(EntityDamageByEntityEvent e) {
        Entity player = e.getDamager();
        if (((e.getEntity() instanceof Player)) &&
                (this.players.contains(player.getName()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        Player player = (Player) e.getEntity();
        if ((this.players.contains(player.getName())) &&
                (e.getFoodLevel() < 19.0D)) {
            e.setFoodLevel(20);
        }
    }

    @EventHandler
    public void Move(PlayerMoveEvent e) {
        Entity player = e.getPlayer();
        if (((e.getPlayer() instanceof Player)) &&
                (this.main.getConfig().getBoolean("features.FreezeGUI") == true) &&
                (this.frozen.contains(player.getName()))) {
            FreezeGUI gui = new FreezeGUI(this.main);
            gui.build((Player) player);
            gui.show((Player) player);
        }
    }
}
