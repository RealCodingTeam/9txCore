package org.realcodingteam.plan9.commands.subdonor;

import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.objects.DonorPlayer;

import net.md_5.bungee.api.ChatColor;

public class DonorView extends SubDonorCommand {

    public void run(CommandSender sender, String[] args) {
        if(!hasPerm(sender, "ntx.donor.view")) return;
        if(args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /donor view <player>");
            return;
        }
        
        DonorPlayer dp = getPlayer(args[1]);
        if(dp == null) {
            sender.sendMessage(ChatColor.RED + "Error: Player not found.");
            return;
        }
        
        sender.sendMessage(ChatColor.GOLD + dp.getPlayer().getName() + ChatColor.GREEN + ": DP Balance is " + ChatColor.GOLD + dp.getDp());
    }
    
}
