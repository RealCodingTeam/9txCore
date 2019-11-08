package org.realcodingteam.plan9.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.commands.TxCommand.Result;
import org.realcodingteam.plan9.commands.patch.PatchHelp;
import org.realcodingteam.plan9.commands.patch.PatchList;

import net.md_5.bungee.api.ChatColor;

public final class PatchCommand implements CommandExecutor {
    
    private static final Map<String, TxCommand> commands = new HashMap<>();
    static {
        commands.put("help", new PatchHelp());
        commands.put("list", new PatchList());
    }
    
    public static Map<String, TxCommand> getSubCommands() {
        return Collections.unmodifiableMap(commands);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1 || !commands.containsKey(args[0].toLowerCase())) {
            commands.get("list").execute(sender, args);
            return true;
        }
        
        String[] shifted = new String[args.length - 1];
        System.arraycopy(args, 1, shifted, 0, args.length - 1);
        
        Result status = commands.get(args[0].toLowerCase()).execute(sender, shifted);
        switch(status) {
            case INVALID_SYNTAX:
                sender.sendMessage(ChatColor.RED + "Invalid command syntax. See patch help command for help.");
                break;
            case NO_PERMISSION:
                sender.sendMessage(ChatColor.RED + "You lack permission.");
                break;
            case INTERNAL_ERROR:
                sender.sendMessage(ChatColor.RED + "Something went wrong executing this command. Please inform an admin of this issue.");
                break;
            case SUCCESS:
                break;
        }
        
        return true;
    }
    
}
