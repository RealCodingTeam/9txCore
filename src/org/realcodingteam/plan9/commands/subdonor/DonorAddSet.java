package org.realcodingteam.plan9.commands.subdonor;

import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.objects.DonorPlayer;

import net.md_5.bungee.api.ChatColor;

public final class DonorAddSet extends SubDonorCommand {

    @Override
    public void run(CommandSender sender, String[] args) {
        givePoints(sender, args, args[0].equalsIgnoreCase("add"));
    }
    
    private void givePoints(CommandSender sender, String[] args, boolean add) {
        if(!hasPerm(sender, "ntx.donor.add")) return;
        if(args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /donor <add/set> <player> <amount>");
            return;
        }
        
        DonorPlayer dp = getPlayer(args[1]);
        if(dp == null) {
            sender.sendMessage(ChatColor.RED + "Error: Player not found.");
            return;
        }
        
        int amount = getInt(args[2]);
        if(add) amount += dp.getDp();
        
        dp.setDp(amount);
        
        String name = dp.getPlayer().getName();
        if(dp.getPlayer().isOnline()) name = dp.getPlayer().getPlayer().getDisplayName();
        
        sender.sendMessage("§aSuccess! §6" + name + " §anow has §e" + amount + "§a points.");
    }
    
}
