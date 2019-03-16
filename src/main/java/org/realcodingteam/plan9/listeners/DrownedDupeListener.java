package org.realcodingteam.plan9.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DrownedDupeListener implements Listener {

	//Prevents the drowned zombie dupe event
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDrownedConvert(CreatureSpawnEvent event) {
		if(event.getSpawnReason() != SpawnReason.DROWNED) return;
		if(event.getEntityType() != EntityType.DROWNED && event.getEntityType() != EntityType.HUSK) return;
		
		Zombie mob = (Zombie)event.getEntity();
		
		mob.getEquipment().setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
		mob.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
		
		ItemStack hand = new ItemStack(Material.ROTTEN_FLESH);
		ItemMeta im = hand.getItemMeta();
		im.setDisplayName("No dupe for you :)");
		hand.setItemMeta(im);
		mob.getEquipment().setItemInMainHand(hand);
	}
	
}
