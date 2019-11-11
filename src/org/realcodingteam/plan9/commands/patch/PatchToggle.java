package org.realcodingteam.plan9.commands.patch;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.commands.Result;
import org.realcodingteam.plan9.commands.Result.Status;
import org.realcodingteam.plan9.commands.TxCommand;
import org.realcodingteam.plan9.patches.Patch;
import org.realcodingteam.plan9.patches.PatchManager;
import org.realcodingteam.plan9.patches.TxPatch;

import net.md_5.bungee.api.ChatColor;

public final class PatchToggle implements TxCommand {

    private final CommandMode mode;
    
    public PatchToggle(CommandMode mode) {
        this.mode = mode;
    }
    
    @Override
    public String usage() {
        return "<internal patch id>";
    }

    @Override
    public Result execute(CommandSender executor, String[] args) {
        if(!executor.hasPermission("ntx.admin")) {
            return Result.NO_PERMISSION;
        }
        
        if(args.length != 1) {
            return new Result(Status.INVALID_SYNTAX, ChatColor.RED + "Invalid syntax. Usage: /patch " + mode.name().toLowerCase() + " <internal patch id>");
        }
        
        Optional<TxPatch> patch = PatchManager.getPatchById(args[0]);
        return patch.map(_patch -> {
            Consumer<TxPatch> function = getModeFunction(mode, _patch);
            String mode_message = ChatColor.DARK_GREEN + "enabled";
            if(mode == CommandMode.DISABLE || (mode == CommandMode.TOGGLE && PatchManager.isEnabled(_patch))) 
                mode_message = ChatColor.DARK_RED + "disabled";
            
            if(function == null) {
                return new Result(Status.ERROR, ChatColor.RED + "This patch is already " + mode_message + ChatColor.RED + "!");
            }
            
            function.accept(_patch);
            String format = ChatColor.GREEN + "Success! The patch %s %swas %s%s!";
            return new Result(Status.SUCCESS, String.format(format, "" + ChatColor.YELLOW + ChatColor.ITALIC + _patch.getMetadata().internal_name(), ChatColor.GREEN, mode_message, ChatColor.GREEN));
        }).orElse(new Result(Status.ERROR, ChatColor.RED + "The specified patch does not exist!"));
    }
    
    private static Consumer<TxPatch> getModeFunction(CommandMode mode, TxPatch patch) {
        switch(mode) {
            case ENABLE:
                if(!PatchManager.isEnabled(patch)) return PatchManager::forceEnable;
                break;
            case DISABLE:
                if(PatchManager.isEnabled(patch)) return PatchManager::disablePatch;
                break;
            default:
            case TOGGLE:
                if(PatchManager.isEnabled(patch)) return PatchManager::disablePatch;
                return PatchManager::forceEnable;
        }
        
        return null;
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
    
    public static enum CommandMode {
        ENABLE,
        DISABLE,
        TOGGLE,
    }
}
