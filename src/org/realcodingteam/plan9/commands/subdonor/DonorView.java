package org.realcodingteam.plan9.commands.subdonor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.objects.DonorPlayer;

import net.md_5.bungee.api.ChatColor;

public class DonorView extends SubDonorCommand {

    @Override
    public void run(CommandSender sender, String[] args) {
        if(args.length < 2) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Usage: /donor view <player>");
                return;
            }
            
            DonorPlayer me = DonorPlayer.getDonorPlayer(((Player)sender).getUniqueId());
            sender.sendMessage(ChatColor.GREEN + "You have " + ChatColor.GOLD + me.getDp() + ChatColor.GREEN + " DP.");
            return;
        }
        
        if(!hasPerm(sender, "ntx.donor.view")) return;
        
        DonorPlayer dp = getPlayer(args[1]);
        if(dp == null) {
            sender.sendMessage(ChatColor.RED + "Error: Player not found.");
            return;
        }
        
        String name = dp.getPlayer().getName();
        if(dp.getPlayer().isOnline()) name = dp.getPlayer().getPlayer().getDisplayName();
        
        sender.sendMessage(ChatColor.GOLD + name + ChatColor.GREEN + ": DP Balance is " + ChatColor.GOLD + dp.getDp());
    }
    
    
    
}
