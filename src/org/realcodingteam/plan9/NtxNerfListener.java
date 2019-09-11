package org.realcodingteam.plan9;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Player;
import org.bukkit.entity.Stray;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;


@SuppressWarnings("deprecation")
public class NtxNerfListener implements Listener {
	
	private Map<Player, Long> pearls = new HashMap<>(); //Contains a list of players on pearl cooldown. <Player, Time>
	
	@EventHandler
	public void onEntityEffectEvent(EntityPotionEffectEvent event) {
		//Disables the Dolphin's Grace effect:
		//Dolphin's grace and depth strider is incredibly OP for vanilla survival.
		if(event.getCause() == Cause.DOLPHIN) {
			event.setCancelled(true);
		
			if(event.getEntity() instanceof Player) {
				Player p = (Player) event.getEntity();
				p.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
			}
		}
	}
	
	
	@EventHandler
	public void onAnvilThing(PrepareAnvilEvent event) {
		//Disable adding riptide to anything
		if(event.getResult().containsEnchantment(Enchantment.RIPTIDE)) {
			event.setResult(new ItemStack(Material.AIR));
		}
	}
	
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent event) {
		//Disable moving shulker boxes into enderchests (you can move them out into normal inventory)
		if(event.getCurrentItem() == null) return;
		
		if (event.getClick() == ClickType.NUMBER_KEY) {
			ItemStack item = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
			
			if (item != null && item.getType().name().contains("SHULKER_BOX")) {
				event.getWhoClicked().sendMessage("�c�lShulker boxes are not allowed in enderchests.");
				event.setCancelled(true);
				return;
			}
		}
		
		if(event.getCurrentItem().getType().name().contains("SHULKER_BOX") && event.getView().getType() == InventoryType.ENDER_CHEST) {
			if(event.getClickedInventory().getType() == InventoryType.ENDER_CHEST) return;
			
			event.setCancelled(true);
			Player player = (Player) event.getWhoClicked();
			player.sendMessage("�c�lShulker boxes are not allowed in enderchests.");
		}
		
		//Convert tipped arrows to normal arrows
		if(event.getCurrentItem().getType() == Material.TIPPED_ARROW) {
			event.getCurrentItem().setType(Material.ARROW);
			event.getCurrentItem().setItemMeta(null);
		}
	}
	
	@EventHandler
	public void onPotionDrink(PlayerInteractEvent event) {
		//Disable turtle master potions completely, along with debuff splashes and all lingering
		if(event.getItem() == null) return;
		
		if(event.getItem().getType() == Material.TRIDENT) {
			if(event.getItem().containsEnchantment(Enchantment.RIPTIDE)) {
				event.setCancelled(true);
				return;
			}
		}
		
		if(event.getItem().getType() == Material.LINGERING_POTION) {
			event.getPlayer().sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Lingering potions are disabled.");
			event.setCancelled(true);
			return;
		}
		
		if(event.getItem().getType() == Material.POTION || event.getItem().getType() == Material.SPLASH_POTION) {
			PotionMeta pm = (PotionMeta) event.getItem().getItemMeta();
			PotionType type = pm.getBasePotionData().getType();
			if(type.equals(PotionType.INSTANT_DAMAGE)
			|| type.equals(PotionType.POISON)
			|| type.equals(PotionType.SLOWNESS)
			|| type.equals(PotionType.WEAKNESS) 
			|| type.equals(PotionType.TURTLE_MASTER)) {
				event.setCancelled(true);
				event.getPlayer().sendMessage("" + ChatColor.RED + ChatColor.BOLD + "This potion is disabled.");
			}
		}
	}
	
	@EventHandler
	public void onPlayerProjectile(ProjectileLaunchEvent event) {
		//Disable tipped arrows from being fired, except from strays.
		if(event.getEntity() instanceof TippedArrow) {
			if(event.getEntity().getShooter() instanceof Stray) return;
			event.setCancelled(true);
			event.getEntity().remove();
			return;
		} else if(event.getEntity() instanceof ThrownPotion) {
			if(event.getEntity() instanceof LingeringPotion) {
				event.setCancelled(true);
				event.getEntity().remove();
			}
			ThrownPotion tp = (ThrownPotion) event.getEntity();
			for(PotionEffect pe : tp.getEffects()) {
				PotionEffectType type = pe.getType();
				if(type.equals(PotionEffectType.HARM)
				|| type.equals(PotionEffectType.POISON)
				|| type.equals(PotionEffectType.SLOW)
				|| type.equals(PotionEffectType.WEAKNESS)) {
					event.setCancelled(true);
					event.getEntity().remove();
				}
				return;
			}
		}
		
		//Enderpearl specifc code
		if(!(event.getEntity().getShooter() instanceof Player) || !(event.getEntity() instanceof EnderPearl)) return;
		
		//Add a cooldown to enderpearls to prevent spamming
		Player player = (Player) event.getEntity().getShooter();
		if(pearls.containsKey(player)) {
			Instant now = Instant.now();
			if(now.toEpochMilli() - pearls.get(player) < (NtxPlugin.instance.getConfig().getLong("pearlcooldown") * 1000)) {
				long future = pearls.get(player) + (NtxPlugin.instance.getConfig().getLong("pearlcooldown") * 1000);
				long delta = future - now.toEpochMilli();
				double delay = Math.floor(delta / 100) / 10;
				player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You cannot use this for another " + delay + " seconds!");
				event.setCancelled(true);
			}
		} else {
			pearls.put(player, Instant.now().toEpochMilli());
			Bukkit.getScheduler().scheduleAsyncDelayedTask(NtxPlugin.instance, new BukkitRunnable() {
				@Override
				public void run() {
					pearls.remove(player);
				}
				
			}, 20 * NtxPlugin.instance.getConfig().getLong("pearlcooldown"));
		}
	}
	
	@EventHandler
	public void onPlayerBuild(BlockPlaceEvent event) {
		World world = event.getBlock().getWorld();
		
		if(event.getBlock().getType().name().endsWith("_BED")) {
			if(world.getEnvironment() == Environment.NETHER || world.getEnvironment() == Environment.THE_END) {
				event.setCancelled(true);
				return;
			}
		}
		
		//Disable building on the nether roof, unless the player is /op or has the permission "ntx.build"
		if(event.getPlayer().isOp() || event.getPlayer().hasPermission("ntx.build")) return;
		
		if(event.getBlock().getWorld().getEnvironment() == Environment.NETHER && event.getBlock().getLocation().getY() > 127.0d) {
			event.getPlayer().sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You are not allowed to build here.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerMine(BlockBreakEvent event) {
		//Disable building on the nether roof, unless the player is /op or has the permission "ntx.build"
		if(event.getPlayer().isOp() || event.getPlayer().hasPermission("ntx.build")) return;
		
		if(event.getBlock().getWorld().getEnvironment() == Environment.NETHER && event.getBlock().getLocation().getY() > 127.0d) {
			event.getPlayer().sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You are not allowed to build here.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerEnchantEvent(EnchantItemEvent event) {
		//Disable getting riptide in enchantments.
		
		for(Enchantment e : event.getEnchantsToAdd().keySet()) {
			if(e.getName().equals(Enchantment.RIPTIDE.getName())) {
				if(event.getEnchantsToAdd().size() == 1) {
					event.setCancelled(true);
				} else {
					event.getEnchantsToAdd().remove(e);
				}
				return;
			}
		}
	}
	
	@EventHandler
	public void onDMG(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		
		Player player = (Player) event.getDamager();
		
		if (player.getInventory().getItemInMainHand().getType().name().contains("_AXE")) {
			event.setDamage(event.getDamage() - 5.5);
		}
	}
}