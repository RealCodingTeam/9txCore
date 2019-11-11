package org.realcodingteam.plan9.commands.patch;

import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.commands.Result;
import org.realcodingteam.plan9.commands.Result.Status;
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
            return Result.NO_PERMISSION;
        }
        
        PatchManager.reloadConfigForPatches();
        return new Result(Status.SUCCESS, ChatColor.BLUE + "Success! All patch configs have been reloaded.");
    }
    
}
