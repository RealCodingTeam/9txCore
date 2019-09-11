package org.realcodingteam.plan9;

import java.time.Instant;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.realcodingteam.plan9.commands.KSCommand;
import org.realcodingteam.plan9.objects.DonorMenu;
import org.realcodingteam.plan9.objects.DonorPlayer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_13_R1.NBTTagCompound;

public class DonorListener implements Listener {

	public static long doubleOreTime = 0L;

	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player killer = player.getKiller();

		if (killer == null)
			return;

		ItemStack item = killer.getInventory().getItemInMainHand();

		if (item != null) {
			KSCommand.doKS(item);
		}

		if (new Random().nextInt(100) > 75) {
			ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
			SkullMeta meta = (SkullMeta) head.getItemMeta();
			meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
			meta.setDisplayName(ChatColor.RESET + player.getName());
			head.setItemMeta(meta);
			event.getDrops().add(head);
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
		if (Instant.now().toEpochMilli() < doubleOreTime) {
			if (event.getBlock().getType().name().contains("ORE")) {
				for (ItemStack is : event.getBlock().getDrops()) {
					if (true) {
						event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), smelt(is));
					}
				}

				event.setDropItems(false);
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(
				new AttributeModifier("generic.attackSpeed", 99999999.0D, AttributeModifier.Operation.ADD_NUMBER));

		if (player.hasPermission("ntx.donor")) {
			DonorPlayer dp = DonorPlayer.loadDonor(player.getUniqueId());

			if (!dp.hasReceivedDP()) {
				int recieved = 0;

				if (player.hasPermission("ntx.emperor")) {
					recieved = 400;
				} else if (player.hasPermission("ntx.king")) {
					recieved = 250;
				} else if (player.hasPermission("ntx.general")) {
					recieved = 150;
				} else if (player.hasPermission("ntx.soldier")) {
					recieved = 75;
				}

				dp.setDp(recieved);
				dp.setHasReceivedDP(true);
				player.sendMessage(ChatColor.GREEN + "You have recieved your donor points of " + ChatColor.GOLD + recieved);
			}

			player.setDisplayName(org.bukkit.ChatColor.getByChar(dp.getNick()) + player.getName() + ChatColor.RESET);
			player.setPlayerListName(org.bukkit.ChatColor.getByChar(dp.getNick()) + player.getName() + ChatColor.RESET);
			dp.setLastLogin(System.currentTimeMillis());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		DonorPlayer.saveDonor(DonorPlayer.getDonorPlayer(event.getPlayer().getUniqueId()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		ItemStack old = player.getInventory().getItemInMainHand();
		
		if (event.getMessage().contains("[item]")) {
			if (old == null || old.getType() == Material.AIR) {
				return;
			}
			
			if (event.isCancelled()) {
				return;
			}
			
			String display = formatProper(old.getType().name());
			char prefix = 'r';
			String amount = "";
			if(old.hasItemMeta()) {
				if(old.getItemMeta().hasDisplayName()) display = old.getItemMeta().getDisplayName();
				if(old.getItemMeta().hasEnchants()) prefix = 'b';
			}
			
			if(old.getAmount() > 1) amount = " x" + old.getAmount(); 
			
			net.minecraft.server.v1_13_R1.ItemStack item = CraftItemStack.asNMSCopy(old);//fucking spigot
			NBTTagCompound tag = new NBTTagCompound();
			
			item.save(tag);
			
			String raw = tag.toString();			
			HoverEvent e = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[] {new TextComponent(raw)});
			TextComponent base = new TextComponent();
			base.setHoverEvent(e);
			base.setText("[" + display + amount + "]§r"); //display name and amount
			base.setColor(ChatColor.getByChar(prefix));
			event.setCancelled(true);
			
			TextComponent msg = new TextComponent();
			String evtmsg = event.getMessage();
			char[] chars = evtmsg.toCharArray();
			
			for(int i = 0; i < chars.length; i++) {
				char c = chars[i];
				if(c != '[' || evtmsg.length() - 5 <= i) {
					msg.addExtra(c + "");
					continue;
				}
				//lol
				if(chars[i+1] != 'i' || chars[i+2] != 't' || chars[i+3] != 'e' || chars[i+4] != 'm' || chars[i+5] != ']') {
					msg.addExtra(c + "");
					continue;
				}
				i += 5;
				msg.addExtra(base);
			}
			
			msg.setText(String.format(event.getFormat(), event.getPlayer().getDisplayName(), msg.getText().replaceAll("[item]", "")));
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.spigot().sendMessage(msg);
			}
		}
		
	}

	private ItemStack smelt(ItemStack item) {
		switch (item.getType()) {
		case DIAMOND_ORE:
			item.setType(Material.DIAMOND);
			break;
		case EMERALD_ORE:
			item.setType(Material.EMERALD);
			break;
		case GOLD_ORE:
			item.setType(Material.GOLD_INGOT);
			break;
		case IRON_ORE:
			item.setType(Material.IRON_INGOT);
			break;
		case LAPIS_ORE:
			item.setType(Material.LAPIS_LAZULI);
			break;
		case REDSTONE_ORE:
			item.setType(Material.REDSTONE);
			break;
		case COAL_ORE:
			item.setType(Material.COAL);
			break;
		case NETHER_QUARTZ_ORE:
			item.setType(Material.QUARTZ);
			break;
		default:
			break;
		}
		
		item.setAmount(item.getAmount() * 3);
		
		return item;
	}
	
	private static String formatProper(String name) {
		String formatted = "";
		String[] words = name.split("_");
		for(String s : words) {
			formatted += s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase() + " ";
		}
		return formatted.trim();
	}                                                                               

}
