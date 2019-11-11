package org.realcodingteam.plan9.commands.patch;

import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.commands.PatchCommand;
import org.realcodingteam.plan9.commands.Result;
import org.realcodingteam.plan9.commands.TxCommand;

import net.md_5.bungee.api.ChatColor;

public final class PatchHelp implements TxCommand {

    @Override
    public String usage() {
        return "";
    }
    
    @Override
    public Result execute(CommandSender executor, String[] args) {
        if(!executor.hasPermission("ntx.patch.help")) {
            return Result.NO_PERMISSION;
        }
        
        PatchCommand.getSubCommands().forEach((label, command) -> {
            executor.sendMessage(ChatColor.YELLOW + "/patch " + label + " " + command.usage());
        });
        return Result.SUCCESS;
    }
    
}
