package org.realcodingteam.plan9.commands.patch;

import static org.realcodingteam.plan9.commands.TxCommand.Result.ERROR;
import static org.realcodingteam.plan9.commands.TxCommand.Result.INVALID_SYNTAX;
import static org.realcodingteam.plan9.commands.TxCommand.Result.NO_PERMISSION;
import static org.realcodingteam.plan9.commands.TxCommand.Result.SUCCESS;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.commands.TxCommand;
import org.realcodingteam.plan9.patches.PatchManager;
import org.realcodingteam.plan9.patches.TxPatch;

import net.md_5.bungee.api.ChatColor;

public final class PatchEnable implements TxCommand {

    @Override
    public String usage() {
        return "<patch internal id>";
    }

    @Override
    public Result execute(CommandSender executor, String[] args) {
        if(!executor.hasPermission("ntx.admin")) {
            return NO_PERMISSION;
        }
        
        if(args.length != 1) {
            return INVALID_SYNTAX;
        }
        
        Optional<TxPatch> patch = PatchManager.getPatchById(args[0]);
        return patch.map(_patch -> {
            if(PatchManager.isEnabled(_patch)) {
                executor.sendMessage(ChatColor.RED + "This patch is already enabled!");
                return ERROR;
            }
            
            PatchManager.forceEnable(_patch);
            sendSuccess(executor, _patch);
            return SUCCESS;
        }).orElse(ERROR);
    }
    
    private void sendSuccess(CommandSender executor, TxPatch patch) {
        String format = "%sSuccess! The patch %s%s%s%s was %senabled%s!";
        executor.sendMessage(String.format(format, ChatColor.GREEN, ChatColor.DARK_GREEN, ChatColor.ITALIC, PatchManager.getPatchMetadata(patch).display_name(), ChatColor.GREEN, ChatColor.DARK_GREEN, ChatColor.GREEN));
    }
}
