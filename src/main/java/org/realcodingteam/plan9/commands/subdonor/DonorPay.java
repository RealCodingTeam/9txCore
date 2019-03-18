package org.realcodingteam.plan9.commands.subdonor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.objects.DonorPlayer;

import net.md_5.bungee.api.ChatColor;

public class DonorPay extends SubDonorCommand {

    @Override
    public void run(CommandSender sender, String[] args) {
        if(stopConsole(sender)) return;
        if(args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /donor " + args[1] + " <player> <amount>");
            return;
        }
        
        DonorPlayer donor = DonorPlayer.getDonorPlayer(((Player)sender).getUniqueId());
        DonorPlayer reciever = getPlayer(args[1]);
        if(reciever == null || !reciever.getPlayer().isOnline()) {
            sender.sendMessage(ChatColor.RED + "Error: Player not found.");
            return;
        }
        
        Player donorP = (Player)sender;
        Player recieverP = reciever.getPlayer().getPlayer();
        
        int amount = getInt(args[2]);
        if(amount < 1) {
            sender.sendMessage(ChatColor.RED + "Error: You must send at least 1 point.");
            return;
        }
        
        if(donor.getDp() - amount < 0) {
            sender.sendMessage(ChatColor.RED + "Error: You cannot donate more points than you have!");
            return;
        }
        
        donor.setDp(donor.getDp() - amount);
        reciever.setDp(reciever.getDp() + amount);
        DonorPlayer.saveDonor(donor, reciever);
        
        String to = "§aYou have donated %d donor points to %s§a!";
        String from = "§aYou recieved %d donor points from %s§a!";
        sender.sendMessage(String.format(to, amount, recieverP.getDisplayName()));
        recieverP.sendMessage(String.format(from, amount, donorP.getDisplayName()));
    }

}
