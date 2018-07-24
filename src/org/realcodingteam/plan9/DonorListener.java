package org.realcodingteam.plan9;

import java.time.Instant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.commands.KSCommand;
import org.realcodingteam.plan9.objects.DonorMenu;

public class DonorListener implements Listener {
	
	public static long doubleOreTime = 0L;

	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player killer = player.getKiller();

		if (killer != null) {
			KSCommand.doKS(killer.getInventory().getItemInMainHand());
		}

	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getInventory() == null) {
			return;
		}

		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}

		if (!event.getInventory().getTitle().contains("§5Donor")) {
			return;
		}

		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		ItemStack raw = event.getCurrentItem();

		event.setResult(Result.DENY);
		event.setCancelled(true);

		switch (inv.getTitle().toLowerCase()) {
		case "§5donor - nick":
			DonorMenu.handleNick(player, raw);
			break;
		case "§5donor - xp":
		case "§5donor - ores":
		case "§5donor - potion effects":
		case "§5donor - misc":
			DonorMenu.handleSub(player, raw);
			break;
		case "§5donor":
		default:
			DonorMenu.handleDonor(player, event.getRawSlot());
			break;
		}
	}

	@EventHandler
	public void onMineOre(BlockBreakEvent event) {
		if(Instant.now().toEpochMilli() < doubleOreTime) {
			if (event.getBlock().getType().name().contains("ORE")) {
				for (ItemStack is : event.getBlock().getDrops()) {
					if (true) {
						event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), is);
					}
				}
			}
		}
	}

}
