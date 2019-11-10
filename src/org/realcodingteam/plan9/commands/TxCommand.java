package org.realcodingteam.plan9.commands;

import org.bukkit.command.CommandSender;

public interface TxCommand {
    
    public String usage();
    public Result execute(CommandSender executor, String[] args);
    
    public static enum Result {
        SUCCESS,
        NO_PERMISSION,
        INVALID_SYNTAX,
        ERROR,
    }
    
}
