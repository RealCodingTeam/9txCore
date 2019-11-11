package org.realcodingteam.plan9.commands;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.realcodingteam.plan9.commands.patch.*;

public final class PatchCommand implements CommandExecutor, TabCompleter {
    
    private static final Map<String, TxCommand> commands = new HashMap<>();
    static {
        commands.put("help", new PatchHelp());
        commands.put("list", new PatchList());
        commands.put("enable", new PatchEnable());
        commands.put("disable", new PatchDisable());
        commands.put("reload", new PatchReload());
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
        
        Result status = commands.get(args[0].toLowerCase()).execute(sender, popArray(args));
        if(!status.message.isEmpty()) sender.sendMessage(status.message);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender executor, Command command, String label, String[] args) {
        switch(args.length) {
            case 0:
                return new ArrayList<>(commands.keySet());
            case 1:
                return commands.keySet()
                        .stream()
                        .map(String::toLowerCase)
                        .filter(cmd -> cmd.startsWith(args[0].toLowerCase()) || args[0].trim().isEmpty())
                        .collect(Collectors.toList());
            case 2:
                if(!commands.containsKey(args[0].toLowerCase())) break;
                
                return commands.get(args[0].toLowerCase()).getCompletions(executor, popArray(args));
        }
        
        return null;
    }
    
    private static String[] popArray(String[] original) {
        if(original == null || original.length < 1) return new String[0];
        
        String[] shifted = new String[original.length - 1];
        System.arraycopy(original, 1, shifted, 0, original.length - 1);
        return shifted;
    }
}
