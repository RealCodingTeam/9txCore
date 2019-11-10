package org.realcodingteam.plan9.commands.patch;

import static org.realcodingteam.plan9.commands.TxCommand.Result.*;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.commands.TxCommand;
import org.realcodingteam.plan9.patches.PatchManager;
import org.realcodingteam.plan9.patches.TxPatch;

import net.md_5.bungee.api.ChatColor;

public final class PatchDisable implements TxCommand {

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
            if(!PatchManager.isEnabled(_patch)) {
                executor.sendMessage(ChatColor.RED + "This patch is already disabled!");
                return ERROR;
            }
            
            PatchManager.disablePatch(_patch);
            sendSuccess(executor, _patch);
            return SUCCESS;
        }).orElse(ERROR);
    }
    
    private void sendSuccess(CommandSender executor, TxPatch patch) {
        String format = "%sSuccess! The patch %s%s%s%s was %sdisabled%s!";
        executor.sendMessage(String.format(format, ChatColor.GREEN, ChatColor.DARK_RED, ChatColor.ITALIC, PatchManager.getPatchMetadata(patch).display_name(), ChatColor.GREEN, ChatColor.DARK_RED, ChatColor.GREEN));
    }
}
