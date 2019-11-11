package org.realcodingteam.plan9.commands.patch;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.commands.Result;
import org.realcodingteam.plan9.commands.Result.Status;
import org.realcodingteam.plan9.commands.TxCommand;
import org.realcodingteam.plan9.patches.Patch;
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
            return Result.NO_PERMISSION;
        }
        
        if(args.length != 1) {
            return new Result(Status.INVALID_SYNTAX, ChatColor.RED + "Invalid syntax. Usage: /patch disable <internal patch id>");
        }
        
        Optional<TxPatch> patch = PatchManager.getPatchById(args[0]);
        return patch.map(_patch -> {
            if(!PatchManager.isEnabled(_patch)) {
                return new Result(Status.ERROR, ChatColor.RED + "This patch is already disabled!");
            }
            
            PatchManager.disablePatch(_patch);
            sendSuccess(executor, _patch);
            return Result.SUCCESS;
        }).orElse(new Result(Status.ERROR, ChatColor.RED + "The specified patch does not exist!"));
    }
    
    private void sendSuccess(CommandSender executor, TxPatch patch) {
        String format = "%sSuccess! The patch %s%s%s%s was %sdisabled%s!";
        executor.sendMessage(String.format(format, ChatColor.GREEN, ChatColor.DARK_RED, ChatColor.ITALIC, patch.getMetadata().display_name(), ChatColor.GREEN, ChatColor.DARK_RED, ChatColor.GREEN));
    }
    
    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        Collection<TxPatch> patches = PatchManager.getPatches();
        return patches.stream()
                .map(TxPatch::getMetadata)
                .map(Patch::internal_name)
                .filter(name -> name.startsWith(args[0].toLowerCase()) || args[0].trim().isEmpty())
                .collect(Collectors.toList());
    }
}
