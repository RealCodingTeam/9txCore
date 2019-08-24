package org.realcodingteam.plan9.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.inv.scripts.Menu;
import org.realcodingteam.plan9.inv.scripts.ScriptContext;

import net.md_5.bungee.api.ChatColor;

public final class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("ntx.admin")) {
            sender.sendMessage(ChatColor.RED + "You lack permission.");
            return true;
        }
        
        ScriptContext.unloadAll();
        Menu.clearEntries();
        sender.sendMessage(ChatColor.YELLOW + "Menus and scripts reloaded.");
        return true;
    }
    
}
