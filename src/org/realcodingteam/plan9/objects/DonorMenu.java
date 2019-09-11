package org.realcodingteam.plan9.objects;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DonorMenu {
	
	public static void open(Player player) {
		Inventory inv = Bukkit.createInventory(player, 27, "§5Donor");
		DonorPlayer dp = DonorPlayer.getDonorPlayer(player.getUniqueId());
		
		inv.setItem(9, buildItem(Material.WHITE_WOOL, 1, "§5Nick", Arrays.asList("§aChange your chat color!")));
		inv.setItem(11, buildItem(Material.EXPERIENCE_BOTTLE, 1, "§5XP", Arrays.asList("§aGive everyone online XP!")));
		inv.setItem(13, buildItem(Material.IRON_ORE, 1, "§5Ores", Arrays.asList("§aIncrease the drop rate of ores!")));
		inv.setItem(15, buildItem(Material.BREWING_STAND, 1, "§5Potion Effects", Arrays.asList("§aGive everyone a cool effect!")));
		inv.setItem(17, buildItem(Material.COOKIE, 1, "§5Misc", Arrays.asList("§aDo other amazing stuff!")));
		inv.setItem(26, buildItem(Material.GOLD_INGOT, 1, "§5Balance", Arrays.asList("§aYour balance is " + "§6" + dp.getDp())));
		
		player.openInventory(inv);
		
	}
	
	public static void openNick(Player player) {
		Inventory inv = Bukkit.createInventory(player, 18, "§5Donor - Nick");
		
		if (!player.hasPermission("ntx.donor.nick")) {
			player.sendMessage(ChatColor.RED + "You lack permission");
			player.getOpenInventory().close();
			return;
		}
		
		inv.setItem(0, buildItem(Material.BLACK_WOOL, 1, "§0Black §fname", null));
		inv.setItem(1, buildItem(Material.LAPIS_BLOCK, 1, "§1Dark blue §fname", null));
		inv.setItem(2, buildItem(Material.GREEN_WOOL, 1, "§2Dark green §fname", null));
		inv.setItem(3, buildItem(Material.CYAN_WOOL, 1, "§3Dark aqua §fname", null));
		inv.setItem(4, buildItem(Material.RED_WOOL, 1, "§4Dark red §fname", null));
		inv.setItem(5, buildItem(Material.PURPLE_WOOL, 1, "§5Purple §fname", null));
		inv.setItem(6, buildItem(Material.GOLD_BLOCK, 1, "§6Gold §fname", null));
		inv.setItem(7, buildItem(Material.STONE, 1, "§7Gray §fname", null));
		inv.setItem(8, buildItem(Material.GRAY_WOOL, 1, "§8Dark gray §fname", null));
		inv.setItem(9, buildItem(Material.BLUE_WOOL, 1, "§9Blue §fname", null));
		inv.setItem(10, buildItem(Material.LIME_WOOL, 1, "§aGreen §fname", null));
		inv.setItem(11, buildItem(Material.LIGHT_BLUE_WOOL, 1, "§bAqua §fname", null));
		inv.setItem(12, buildItem(Material.RED_TERRACOTTA, 1, "§cRed §fname", null));
		inv.setItem(13, buildItem(Material.PINK_CONCRETE, 1, "§dLight purple §fname", null));
		inv.setItem(14, buildItem(Material.YELLOW_WOOL, 1, "§eYellow §fname", null));
		inv.setItem(15, buildItem(Material.WHITE_WOOL, 1, "§fWhite §fname", null));
		
		player.openInventory(inv);
	}
	
	public static void openXP(Player player) {
		Inventory inv = Bukkit.createInventory(player, 9, "§5Donor - XP");
		DonorPlayer dp = DonorPlayer.getDonorPlayer(player.getUniqueId());
		
		inv.setItem(1, buildItem(Material.EXPERIENCE_BOTTLE, 1, "§5Ten XP Levels - 15 DP", Arrays.asList("§e10 §aexperience levels")));
		inv.setItem(3, buildItem(Material.EXPERIENCE_BOTTLE, 2, "§5Twenty XP Levels - 30 DP", Arrays.asList("§e20 §aexperience levels")));
		inv.setItem(5, buildItem(Material.EXPERIENCE_BOTTLE, 3, "§5Thirty XP Levels - 50 DP", Arrays.asList("§e30 §aexperience levels")));
		inv.setItem(7, buildItem(Material.GOLD_INGOT, 1, "§5Balance", Arrays.asList("§aYour balance is " + "§6" + dp.getDp())));
		
		player.openInventory(inv);
	}
	
	public static void openOres(Player player) {
		Inventory inv = Bukkit.createInventory(player, 9, "§5Donor - Ores");	
		DonorPlayer dp = DonorPlayer.getDonorPlayer(player.getUniqueId());
		
		inv.setItem(1, buildItem(Material.DIAMOND_ORE, 1, "§5Triple Ore Drops - 50 DP", Arrays.asList("§aTriple Ore Drops & Auto-Smelt for 1 Minute")));
		inv.setItem(3, buildItem(Material.DIAMOND_ORE, 2, "§5Triple Ore Drops - 100 DP", Arrays.asList("§aTriple Ore Drops & Auto-Smelt for 2 Minutes")));
		inv.setItem(5, buildItem(Material.DIAMOND_ORE, 3, "§5Triple Ore Drops - 200 DP", Arrays.asList("§aTriple Ore Drops & Auto-Smelt for 4 Minutes")));
		inv.setItem(7, buildItem(Material.GOLD_INGOT, 1, "§5Balance", Arrays.asList("§aYour balance is " + "§6" + dp.getDp())));
		
		player.openInventory(inv);
	}
	
	public static void openPotion(Player player) {
		Inventory inv = Bukkit.createInventory(player, 36, "§5Donor - Potion Effects");	
		DonorPlayer dp = DonorPlayer.getDonorPlayer(player.getUniqueId());
		
		inv.setItem(0, buildItem(Material.SUGAR, 1, "§5Speed III - 15 DP", Arrays.asList("§aSpeed III for 2 minutes")));
		inv.setItem(9, buildItem(Material.SUGAR, 2, "§5Speed III - 30 DP", Arrays.asList("§aSpeed III for 4 minutes")));
		inv.setItem(18, buildItem(Material.SUGAR, 3, "§5Speed III - 50 DP", Arrays.asList("§aSpeed III for 6 minutes")));
		inv.setItem(2, buildItem(Material.GOLDEN_CARROT, 1, "§5Invisibility - 15 DP", Arrays.asList("§aInvisibility for 2 minutes")));
		inv.setItem(11, buildItem(Material.GOLDEN_CARROT, 2, "§5Invisibility - 30 DP", Arrays.asList("§aInvisibility for 4 minutes")));
		inv.setItem(20, buildItem(Material.GOLDEN_CARROT, 3, "§5Invisibility - 50 DP", Arrays.asList("§aInvisibility for 6 minutes")));
		inv.setItem(4, buildItem(Material.GOLDEN_PICKAXE, 1, "§5Haste II - 15 DP",  Arrays.asList("§aHaste II for 2 minutes")));
		inv.setItem(13, buildItem(Material.GOLDEN_PICKAXE, 2, "§5Haste II - 30 DP", Arrays.asList("§aHaste II for 4 minutes")));
		inv.setItem(22, buildItem(Material.GOLDEN_PICKAXE, 3, "§5Haste II - 50 DP", Arrays.asList("§aHaste II for 6 minutes")));
		inv.setItem(6, buildItem(Material.GLOWSTONE, 1, "§5Glowing - 15 DP",  Arrays.asList("§aGlowing for 2 minutes")));
		inv.setItem(15, buildItem(Material.GLOWSTONE, 2, "§5Glowing - 30 DP", Arrays.asList("§aGlowing for 4 minutes")));
		inv.setItem(24, buildItem(Material.GLOWSTONE, 3, "§5Glowing - 60 DP", Arrays.asList("§aGlowing for 6 minutes")));
		inv.setItem(8, buildItem(Material.GHAST_TEAR, 1, "§5Regeneration II - 30 DP",   Arrays.asList("§aRegeneration II for 30 seconds")));
		inv.setItem(17, buildItem(Material.GHAST_TEAR, 2, "§5Regeneration II - 60 DP",  Arrays.asList("§aRegeneration II for 1 minute")));
		inv.setItem(26, buildItem(Material.GHAST_TEAR, 3, "§5Regeneration II - 120 DP", Arrays.asList("§aRegeneration II for 2 minutes")));
		inv.setItem(31, buildItem(Material.GOLD_INGOT, 1, "§5Balance", Arrays.asList("§aYour balance is " + "§6" + dp.getDp())));
		
		player.openInventory(inv);
	}
	
	public static void openMisc(Player player) {
		Inventory inv = Bukkit.createInventory(player, 9, "§5Donor - Misc");	
		DonorPlayer dp = DonorPlayer.getDonorPlayer(player.getUniqueId());
		
		inv.setItem(0, buildItem(Material.COOKIE, 1, "§5Feed Everyone - 5 DP", Arrays.asList("§aSaturation")));;
		inv.setItem(2, buildItem(Material.GOLDEN_APPLE, 1, "§5Heal Everyone - 15 DP", Arrays.asList("§aFull health")));
		inv.setItem(4, buildItem(Material.SUNFLOWER, 1, "§5Set Time To Day - 10 DP", Arrays.asList("§aDaytime")));
		if (Bukkit.getWorld("world").hasStorm())
			inv.setItem(6, buildItem(Material.BUCKET, 1, "§5Set Weather To Clear - 10 DP", Arrays.asList("§aClear weather")));
		else
			inv.setItem(6, buildItem(Material.WATER_BUCKET, 1, "§5Set Weather To Stormy - 100 DP", Arrays.asList("§aThunderstorm")));
		inv.setItem(8, buildItem(Material.GOLD_INGOT, 1, "§5Balance", Arrays.asList("§aYour balance is " + "§6" + dp.getDp())));
		
		player.openInventory(inv);
	}
	
	public static void handleDonor(Player player, int click) {
		switch (click) {
		case 9:
			openNick(player);
			break;
		case 11:
			openXP(player);
			break;
		case 13:
			openOres(player);
			break;
		case 15:
			openPotion(player);
			break;
		case 17:
			openMisc(player);
			break;
		default:
		}
	}
	
	public static void handleNick(Player player, ItemStack item) {
			char code = item.getItemMeta().getDisplayName().charAt(1);
			if(code == 'l') code = '0';
			player.setDisplayName(ChatColor.getByChar(code) + player.getName() + ChatColor.RESET);
			player.setPlayerListName(ChatColor.getByChar(code) + player.getName() + ChatColor.RESET);
			DonorPlayer.getDonorPlayer(player.getUniqueId()).setNick("" + code);
			player.sendMessage(ChatColor.YELLOW + "You set your name to: " + player.getDisplayName());
	}
	
	public static void handleSub(Player player, ItemStack item) {
		if(item == null || item.getType() == Material.AIR || item.getType() == Material.GOLD_INGOT) return;
		int dp = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getDisplayName()).replaceAll("[^0-9]", ""));
		Material mat = item.getType();
		DonorPlayer ddp = DonorPlayer.getDonorPlayer(player.getUniqueId());
		
		if (canAfford(player.getUniqueId(), dp)) {
			String effect = item.getItemMeta().getLore().get(0);
			Bukkit.broadcastMessage("§e[DONOR] " + "§r" + player.getDisplayName() + "§d purchased §e" + effect + "§d for the server!");
			DonorEffects.getEffect(mat, dp).run();
			player.closeInventory();
		} else {
			player.sendMessage(ChatColor.RED + "You cannot afford this! You only have " + ddp.getDp() + " DP, but need at least " + dp + "!");
		}
	}
	
	public static boolean canAfford(UUID id, int cost) {
		DonorPlayer dp = DonorPlayer.getDonorPlayer(id);	
		int bal = dp.getDp() - cost;
		
		if (bal >= 0) {
			dp.setDp(bal);
			return true;
		}
		
		return false;
	}
	
	public static ItemStack buildItem(Material type, int amount, String name, List<String> lore) {
		ItemStack item = new ItemStack(type, amount);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
}
