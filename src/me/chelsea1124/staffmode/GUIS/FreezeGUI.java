package me.chelsea1124.staffmode.GUIS;

import java.util.ArrayList;
import java.util.List;
import me.chelsea1124.staffmode.Commands.StaffMode;
import me.chelsea1124.staffmode.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class FreezeGUI
        implements Listener
{
    private Main main;

    public FreezeGUI(Main main)
    {
        this.main = main;
    }

    public static Inventory inv = Bukkit.createInventory(null, 9, StaffMode.color("&cYou have been Frozen!"));

    public void build(Player p)
    {
        ItemStack fill = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
        ItemMeta fillmeta = fill.getItemMeta();
        fillmeta.setDisplayName("");
        fill.setItemMeta(fillmeta);

        ItemStack rules = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("FreezeGUI.RulesItem")));
        ItemMeta rulesmeta = rules.getItemMeta();
        rulesmeta.setDisplayName(StaffMode.color(this.main.getConfig().getString("FreezeGUI.RulesName")));
        List RulesL = new ArrayList();
        for (String Rulesl : this.main.getConfig().getStringList("FreezeGUI.Rules")) {
            RulesL.add(ChatColor.translateAlternateColorCodes('&', Rulesl));
        }
        rulesmeta.setLore(RulesL);
        rules.setItemMeta(rulesmeta);

        ItemStack instructions = new ItemStack(Material.getMaterial(this.main.getConfig().getInt("FreezeGUI.InstructionsItem")));
        ItemMeta instructionsmeta = instructions.getItemMeta();
        instructionsmeta.setDisplayName(StaffMode.color(this.main.getConfig().getString("FreezeGUI.InstructionsName")));
        List InstructionsL = new ArrayList();
        for (String Instructionsl : this.main.getConfig().getStringList("FreezeGUI.instructions")) {
            InstructionsL.add(ChatColor.translateAlternateColorCodes('&', Instructionsl));
        }
        instructionsmeta.setLore(InstructionsL);
        instructions.setItemMeta(instructionsmeta);

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta headmeta = (SkullMeta)head.getItemMeta();
        headmeta.setOwner(p.getName());
        ArrayList<String> lore = new ArrayList();
        lore.add(p.getName());
        headmeta.setLore(lore);
        headmeta.setDisplayName(p.getDisplayName());
        head.setItemMeta(headmeta);

        inv.setItem(0, fill);
        inv.setItem(1, instructions);
        inv.setItem(2, fill);
        inv.setItem(3, fill);
        inv.setItem(4, head);
        inv.setItem(5, fill);
        inv.setItem(6, fill);
        inv.setItem(7, rules);
        inv.setItem(8, fill);
    }

    public void show(Player p)
    {
        p.openInventory(inv);
    }
}
