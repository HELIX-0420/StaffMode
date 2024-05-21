package me.chelsea1124.staffmode;

import java.io.File;

import me.chelsea1124.staffmode.Commands.StaffMode;
import me.chelsea1124.staffmode.Commands.staffchat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
        extends JavaPlugin
        implements Listener
{
    public String prefix = color(getConfig().getString("prefix"));
    public static StaffMode staff;

    public static String color(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void onEnable()
    {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }
        Bukkit.getConsoleSender().sendMessage("�d�m--------------------------------------");
        Bukkit.getConsoleSender().sendMessage("�eStaffMode has bean ENABLED!");
        Bukkit.getConsoleSender().sendMessage("�d�m--------------------------------------");

        staff = new StaffMode(this);
        Bukkit.getPluginManager().registerEvents(staff, this);

        getCommand("StaffMode").setExecutor(staff);
        getCommand("staffchat").setExecutor(new staffchat(this));

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }
}
