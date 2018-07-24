package org.realcodingteam.plan9.objects;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.realcodingteam.plan9.DonorListener;

@SuppressWarnings("deprecation")
public final class DonorEffects {
	private static final Map<Map.Entry<Material, Integer>, Runnable> registry = new HashMap<>();
	
	public static void init() {
		//Double ores
		registry.put(new SimpleEntry<Material,Integer>(Material.DIAMOND_ORE, 50), () ->  doDoubleOreTime(60_000L));
		registry.put(new SimpleEntry<Material,Integer>(Material.DIAMOND_ORE, 100), () -> doDoubleOreTime(120_000L));
		registry.put(new SimpleEntry<Material,Integer>(Material.DIAMOND_ORE, 200), () -> doDoubleOreTime(240_000L));
		
		//Speed 3
		registry.put(new SimpleEntry<Material,Integer>(Material.SUGAR, 15), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120 * 20, 2))));
		registry.put(new SimpleEntry<Material,Integer>(Material.SUGAR, 30), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 240 * 20, 2))));
		registry.put(new SimpleEntry<Material,Integer>(Material.SUGAR, 50), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 360 * 20, 2))));
		
		//Invisibility
		registry.put(new SimpleEntry<Material,Integer>(Material.GOLDEN_CARROT, 15), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120 * 20, 0))));
		registry.put(new SimpleEntry<Material,Integer>(Material.GOLDEN_CARROT, 30), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 240 * 20, 0))));
		registry.put(new SimpleEntry<Material,Integer>(Material.GOLDEN_CARROT, 50), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 360 * 20, 0))));
		
		//Haste II
		registry.put(new SimpleEntry<Material,Integer>(Material.GOLDEN_PICKAXE, 15), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 120 * 20, 1))));
		registry.put(new SimpleEntry<Material,Integer>(Material.GOLDEN_PICKAXE, 30), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 240 * 20, 1))));
		registry.put(new SimpleEntry<Material,Integer>(Material.GOLDEN_PICKAXE, 50), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 360 * 20, 1))));
				
		//Glowing
		registry.put(new SimpleEntry<Material,Integer>(Material.GLOWSTONE, 15), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 120 * 20, 0))));
		registry.put(new SimpleEntry<Material,Integer>(Material.GLOWSTONE, 30), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 240 * 20, 0))));
		registry.put(new SimpleEntry<Material,Integer>(Material.GLOWSTONE, 60), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 360 * 20, 0))));
		
		//Regeneration
		registry.put(new SimpleEntry<Material,Integer>(Material.GHAST_TEAR, 30), () ->  Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30 * 20, 1))));
		registry.put(new SimpleEntry<Material,Integer>(Material.GHAST_TEAR, 60), () ->  Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60 * 20, 1))));
		registry.put(new SimpleEntry<Material,Integer>(Material.GHAST_TEAR, 120), () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120 * 20, 1))));
		
		//Feed everyone
		registry.put(new SimpleEntry<Material,Integer>(Material.COOKIE, 5), () ->
			Bukkit.getOnlinePlayers().forEach(p -> {
				p.setSaturation(20.0f);
				p.setFoodLevel(20);
			})
		);
		
		//Heal everyone
		registry.put(new SimpleEntry<Material,Integer>(Material.GOLDEN_APPLE, 15), () -> Bukkit.getOnlinePlayers().forEach(p -> p.setHealth(p.getMaxHealth())));
		
		//Change time to day
		registry.put(new SimpleEntry<Material,Integer>(Material.SUNFLOWER, 10), () -> Bukkit.getWorlds().forEach(w -> w.setTime(1000)));
		
		//Give XP levels
		registry.put(new SimpleEntry<Material,Integer>(Material.EXPERIENCE_BOTTLE, 15), () -> Bukkit.getOnlinePlayers().forEach(p -> p.giveExp(160)));
		registry.put(new SimpleEntry<Material,Integer>(Material.EXPERIENCE_BOTTLE, 30), () -> Bukkit.getOnlinePlayers().forEach(p -> p.giveExp(550)));
		registry.put(new SimpleEntry<Material,Integer>(Material.EXPERIENCE_BOTTLE, 50), () -> Bukkit.getOnlinePlayers().forEach(p -> p.giveExp(1395)));
	}
	
	private static void doDoubleOreTime(long time) {
		if(DonorListener.doubleOreTime < Instant.now().toEpochMilli()) {
			DonorListener.doubleOreTime = Instant.now().toEpochMilli();
		}
		
		DonorListener.doubleOreTime += time;
	}

	public static Runnable getEffect(Material material, int cost) {
		return registry.get(new SimpleEntry<Material,Integer>(material, cost));
	}
}
