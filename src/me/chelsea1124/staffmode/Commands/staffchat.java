package me.chelsea1124.staffmode.Commands;

import me.chelsea1124.staffmode.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class staffchat implements CommandExecutor, Listener {

    private Main main;
    public staffchat(Main main) {
        this.main = main;
    }
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("staffchat")) {
                if (args.length == 0) {
                    sender.sendMessage(color(main.getConfig().getString("No-Perms")).replace("%PREFIX%", this.main.prefix));
                    return true;
                }

                if (args.length > 0) {
                    StringBuilder b = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        b.append(" ");
                        b.append(args[i]);
                    }
                    String message = b.toString();
                    for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                        if ((all.hasPermission("staffmode.staffchat.use")) || (all.isOp())) {
                            all.sendMessage(color(main.getConfig().getString("staffchat.message")).replace("%PLAYER%", player.getDisplayName()).replace("%MSG%", message));

                        }
                    }
                }
            }

        }

        return false;
    }
}


