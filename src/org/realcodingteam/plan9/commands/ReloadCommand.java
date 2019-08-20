package org.realcodingteam.plan9.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.inv.scripts.Menu;

import net.md_5.bungee.api.ChatColor;

public final class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("ntx.admin")) {
            sender.sendMessage(ChatColor.RED + "You lack permission.");
            return true;
        }
        
        Menu.clearEntries();
        sender.sendMessage(ChatColor.YELLOW + "Menus reloaded.");
        return true;
    }
    
}
