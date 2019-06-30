package org.realcodingteam.plan9.misc.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.entity.Player;

public final class ChatCommand {
    
    private static final Map<String, BiConsumer<Player, String[]>> cmds = new HashMap<>(); 
    
    public static boolean register(String handle, BiConsumer<Player, String[]> runner) {
        handle = handle.toLowerCase();
        
        return cmds.putIfAbsent(handle, runner) == null;
    }
    
    public static boolean run(String handle, Player who, String[] args) {
        handle = handle.toLowerCase();
        
        if(!cmds.containsKey(handle)) return false;
        cmds.get(handle).accept(who, args);
        return true;
    }

    private ChatCommand() {}
}
