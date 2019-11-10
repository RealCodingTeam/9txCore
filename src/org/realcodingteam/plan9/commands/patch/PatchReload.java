package org.realcodingteam.plan9.commands.patch;

import static org.realcodingteam.plan9.commands.TxCommand.Result.NO_PERMISSION;
import static org.realcodingteam.plan9.commands.TxCommand.Result.SUCCESS;

import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.commands.TxCommand;
import org.realcodingteam.plan9.patches.PatchManager;

import net.md_5.bungee.api.ChatColor;

public final class PatchReload implements TxCommand {

    @Override
    public String usage() {
        return "";
    }

    @Override
    public Result execute(CommandSender executor, String[] args) {
        if(!executor.hasPermission("ntx.admin")) {
            return NO_PERMISSION;
        }
        
        PatchManager.reloadConfigForPatches();
        executor.sendMessage(ChatColor.BLUE + "Success! All patch configs have been reloaded.");
        return SUCCESS;
    }
    
}
