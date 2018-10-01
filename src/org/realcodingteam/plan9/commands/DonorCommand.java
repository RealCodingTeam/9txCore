package org.realcodingteam.plan9.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.objects.DonorMenu;
import org.realcodingteam.plan9.objects.DonorPlayer;

public class DonorCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission("ntx.donor")) {
			sender.sendMessage(ChatColor.RED + "You lack permission!");
			return true;
		}
		
		if (!(sender instanceof Player)) {
			tryAdd(sender, args, true);
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			DonorMenu.open(player);
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
						
						player.sendMessage(ChatColor.GOLD + args[1] + ChatColor.GREEN + ": DP Balance is " + ChatColor.GOLD + dp.getDp());
						return true;
					}
				}
			}
			
			sender.sendMessage(ChatColor.RED + "§cYou lack permission");
			break;
		case "xp":
			DonorMenu.openXP(player);
			break;
		case "ores":
			DonorMenu.openOres(player);
			break;
		case "help":
			player.sendMessage(ChatColor.GREEN + "§eTry /donor [§2xp§a|§2ores§a|§2potion§a|§2nick§a|§2misc§a]");
			break;
		case "potion":
			DonorMenu.openPotion(player);
			break;
		case "misc":
			DonorMenu.openMisc(player);
			break;
		case "nick":
			DonorMenu.openNick(player);
			break;
		default:
			DonorMenu.open(player);
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
