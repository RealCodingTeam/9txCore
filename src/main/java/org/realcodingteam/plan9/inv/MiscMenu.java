package org.realcodingteam.plan9.inv;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.objects.effects.MiscEffects;

public class MiscMenu extends AbstractMenu {

	public MiscMenu(Player viewer) {
		super(9, ChatColor.DARK_PURPLE + "Donor - Misc", viewer, true);
		
		build();
		open(viewer);
	}

	protected void build() {
		inv.setItem(0, makeItem(Material.COOKIE, 				"§5Feed Everyone - 5 DP", 			"§aSaturation"));
		inv.setItem(2, makeItem(Material.GOLDEN_APPLE, 			"§5Heal Everyone - 15 DP", 			"§aFull health"));
		inv.setItem(4, makeItem(Material.SUNFLOWER, 			"§5Set Time To Day - 10 DP", 		"§aDaytime"));
		if (Bukkit.getWorld("world").hasStorm()) {
			inv.setItem(6, makeItem(Material.BUCKET, 			"§5Set Weather To Clear - 10 DP", 	"§aClear weather"));
		} else {
			inv.setItem(6, makeItem(Material.WATER_BUCKET, 1, 	"§5Set Weather To Stormy - 100 DP", "§aThunderstorm"));
		}
	}

	@Override
	public void onInventoryClick(ItemStack item) {
		MiscEffects.fromItem(item).buyAndRun(viewer, getLore(item));
	}
}
