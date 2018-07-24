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

public class DonorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission("ntx.donor")) {
			sender.sendMessage(ChatColor.RED + "You lack permission!");
			return true;
		}
		
		if (!(sender instanceof Player)) {
			tryAdd(sender, args);
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			DonorMenu.open(player);
			return true;
		}
		
		switch (args[0].toLowerCase()) {
		case "add":
			tryAdd(sender, args);
			break;
		case "xp":
			DonorMenu.openXP(player);
			break;
		case "ores":
			DonorMenu.openOres(player);
			break;
		case "help":
			player.sendMessage(ChatColor.GREEN + "Try /donor [§2xp§a|§2ores§a|§2potion§a|§2nick§a|§2misc§a]");
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

	public boolean tryAdd(CommandSender sender, String[] args) {
		
		if (args.length > 2) {
			if (args[0].equalsIgnoreCase("add")) {
				if (sender.hasPermission("ntx.donor.add")) {
					UUID id = UUID.fromString(args[1]);				
					OfflinePlayer player = Bukkit.getOfflinePlayer(id);
					int amount = 0; 
					
					if (player == null) {
						sender.sendMessage("That player does not exist!");
						return false;
					}
					
					try {
						amount = Integer.parseInt(args[2]) + DonorMenu.points.getOrDefault(id, 0);
						
						DonorMenu.points.put(id, amount);
						sender.sendMessage("Success! " + player.getName() + " now has " + amount);
						if (player.isOnline()) {
							Player target = (Player) player;
							target.sendMessage(ChatColor.GREEN + "You now have " + ChatColor.GOLD + amount + ChatColor.GREEN + "DP");
						}
						return true;
					} catch (NumberFormatException e) {
						sender.sendMessage("Format is /donor add <uuid> <amount>");
						return false;
					}
				}
			}
		}
		
		return false;
	}
}
