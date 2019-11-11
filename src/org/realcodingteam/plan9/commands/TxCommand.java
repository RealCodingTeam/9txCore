package org.realcodingteam.plan9.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface TxCommand  {
    
    public String usage();
    public Result execute(CommandSender executor, String[] args);
    public default List<String> getCompletions(CommandSender sender, String[] args) { return null; }
}
