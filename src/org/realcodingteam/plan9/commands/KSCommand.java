package org.realcodingteam.plan9.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KSCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission("ntx.ks")) {
			sender.sendMessage(ChatColor.RED + "You lack permission!");
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("This is a player command!");
			return true;
		}
		
		Player player = (Player) sender;
		ItemStack item = player.getInventory().getItemInMainHand();
		
		if (item.getType().name().contains("AXE") || item.getType().name().contains("SWORD") || item.getType().name().contains("BOW") || item.getType().name().contains("TRIDENT")) {
			if (applyKS(item))
				player.sendMessage(ChatColor.GREEN + "A killstreak has been applied!");
			else
				player.sendMessage(ChatColor.RED + "A killstreak already exists!");
			
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "You can only apply killsteaks to a sword, axe, trident, or a bow!");
			return true;
		}
		
	}
	
	public boolean applyKS(ItemStack item) {
		ItemMeta meta = item.getItemMeta(); 
		List<String> lore = meta.getLore();
		
		if (!meta.hasLore()) {
			meta.setLore(Arrays.asList(ChatColor.GOLD + "Kills: 0"));
			item.setItemMeta(meta);
		} else {
			for(String s : lore) {
				if (s.contains("Kills: ")) return false;
			}
			
			lore.add(ChatColor.GOLD + "Kills: 0");
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		
		return true;
	}
	
	public static void doKS(ItemStack item) {
		ItemMeta meta = item.getItemMeta(); 
		
		if(!meta.hasLore()) return;
		
		List<String> lore = meta.getLore();
		
		for(int i = 0; i < lore.size(); i++) {
			String line = ChatColor.stripColor(lore.get(i));
			if(line.startsWith("Kills: ")) {
				int kills = Integer.parseInt(line.replaceAll("[^0-9]", "")) + 1;
				lore.set(i, ChatColor.GOLD + "Kills: " + kills++);
				break;
			}
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

}
