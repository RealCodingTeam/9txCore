package org.realcodingteam.plan9.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.inv.ExpMenu;
import org.realcodingteam.plan9.inv.MiscMenu;
import org.realcodingteam.plan9.inv.NickMenu;
import org.realcodingteam.plan9.inv.OresMenu;
import org.realcodingteam.plan9.inv.PotionsMenu;
import org.realcodingteam.plan9.inv.RootMenu;
import org.realcodingteam.plan9.objects.DonorPlayer;

public class DonorCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            tryAdd(sender, args, true);
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            new RootMenu(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "add":
                tryAdd(sender, args, true);
                break;
            case "set":
                tryAdd(sender, args, false);
                break;
            case "view":
                if (player.hasPermission("ntx.donor.view")) {
                    if (args.length > 1) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        
                        if (target != null) {
                            DonorPlayer dp = DonorPlayer.getDonorPlayer(target.getUniqueId());
                            
                            player.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.GREEN + ": DP Balance is " + ChatColor.GOLD + dp.getDp());
                            return true;
                        }
                    }
                }
                
                sender.sendMessage(ChatColor.RED + "§cYou lack permission");
                break;
            case "xp":
                new ExpMenu(player);
                break;
            case "ores":
                new OresMenu(player);
                break;
            case "potion":
                new PotionsMenu(player);
                break;
            case "misc":
                new MiscMenu(player);
                break;
            case "nick":
                new NickMenu(player);
                break;
            case "help":
                player.sendMessage(ChatColor.GREEN + "§eTry /donor [§2xp§a|§2ores§a|§2potion§a|§2nick§a|§2misc§a|§2pay§a]");
                break;
            case "pay":
            case "send":
                if(args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /donor " + args[0] + " <player> <amount>");
                    return true;
                }
                
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null) {
                    player.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                
                int amount = 0;
                try {
                    amount = (int)Double.parseDouble(args[2]);
                } catch(Exception e) {
                    player.sendMessage(ChatColor.RED + "That is not a valid payment.");
                    return true;
                }
                
                if(amount <= 0) {
                    player.sendMessage(ChatColor.RED + "You must send at least 1 donor point.");
                    return true;
                }
                
                DonorPlayer donor = DonorPlayer.getDonorPlayer(player.getUniqueId());
                DonorPlayer reciever = DonorPlayer.getDonorPlayer(target.getUniqueId());
                
                if(donor.getDp() < amount) {
                    player.sendMessage(ChatColor.RED + "You do not have enough donor points to do this.");
                    return true;
                }
                
                donor.setDp(donor.getDp() - amount);
                reciever.setDp(reciever.getDp() + amount);
                DonorPlayer.saveDonor(donor);
                DonorPlayer.saveDonor(reciever);
                
                String to = "§aYou have donated %d donor points to %s§a!";
                String from = "§aYou recieved %d donor points from %s§a!";
                player.sendMessage(String.format(to, amount, target.getDisplayName()));
                target.sendMessage(String.format(from, amount, player.getDisplayName()));
                break;
            default:
                new RootMenu(player);
                break;
        }
        
        return true;
    }

    @SuppressWarnings("deprecation")
    public boolean tryAdd(CommandSender sender, String[] args, boolean add) {
        
        if (args.length > 2) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("ntx.donor.add")) {
                    OfflinePlayer player = null;
                    int amount = 0;
                    
                    if (args[1].matches("[0-9a-f]{8}[-]*[0-9a-f]{4}[-]*[1-5][0-9a-f]{3}[-]*[89ab][0-9a-f]{3}[-]*[0-9a-f]{12}")) {
                        player = Bukkit.getOfflinePlayer(UUID.fromString(args[1]));
                    } else {
                        player = Bukkit.getOfflinePlayer(args[1]);
                    }
                
                    if (player == null) {
                        sender.sendMessage("§cThat player does not exist!");
                        return false;
                    }
                    
                    try {
                        DonorPlayer tp = DonorPlayer.getDonorPlayer(player.getUniqueId());
                        amount = Integer.parseInt(args[2]);
                        
                        if (add) {
                            amount += tp.getDp();
                        } 
                        
                        tp.setDp(amount);
                        sender.sendMessage("§aSuccess! " + player.getName() + " now has " + amount);
                        return true;
                    } catch (NumberFormatException e) {
                        sender.sendMessage("§cFormat is /donor add <uuid> <amount>");
                        return false;
                    }
                }
            }
        }
        
        return false;
    }
}
