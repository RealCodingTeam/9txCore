package org.realcodingteam.plan9.inv;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.objects.effects.ExpEffects;

public final class ExpMenu extends AbstractMenu {

	public ExpMenu(Player viewer) {
		super(9, ChatColor.DARK_PURPLE + "Donor - XP", viewer, true);
		
		build();
		open(viewer);
	}
	
	protected void build() {
		inv.setItem(1, makeItem(Material.EXPERIENCE_BOTTLE, 	"§5Ten XP Levels - 15 DP", 		"§e10 §aexperience levels"));
		inv.setItem(3, makeItem(Material.EXPERIENCE_BOTTLE, 2, 	"§5Twenty XP Levels - 30 DP", 	"§e20 §aexperience levels"));
		inv.setItem(5, makeItem(Material.EXPERIENCE_BOTTLE, 3, 	"§5Thirty XP Levels - 50 DP", 	"§e30 §aexperience levels"));
	}

	@Override
	public void onInventoryClick(ItemStack item) {
		ExpEffects effect = ExpEffects.fromAmount(item.getAmount());
		effect.buyAndRun(viewer, getLore(item));
	}
}
