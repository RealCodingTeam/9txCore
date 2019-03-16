package org.realcodingteam.plan9;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.realcodingteam.plan9.commands.DonorCommand;
import org.realcodingteam.plan9.commands.StaffChatCommand;
import org.realcodingteam.plan9.listeners.BookOverloadListener;
import org.realcodingteam.plan9.listeners.DonorListener;
import org.realcodingteam.plan9.listeners.DrownedDupeListener;
import org.realcodingteam.plan9.listeners.NtxNerfListener;
import org.realcodingteam.plan9.objects.DonorPlayer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;

public class NtxPlugin extends JavaPlugin {
	
	public static NtxPlugin instance;
	
	@Override
	public void onEnable() {
		instance = this;
		initConfig();
		loadDonors();
		//register events
		getServer().getPluginManager().registerEvents(new NtxNerfListener(), this);
		getServer().getPluginManager().registerEvents(new DonorListener(), this);
		getServer().getPluginManager().registerEvents(new DrownedDupeListener(), this);
		getServer().getPluginManager().registerEvents(new BookOverloadListener(), this);
		//register commands
		getCommand("donor").setExecutor(new DonorCommand());
		getCommand("staff").setExecutor(new StaffChatCommand());
		
		enableBookChecker();
	}
	
	public void onDisable() {
		Bukkit.getOnlinePlayers().parallelStream()
								 .map(Player::getUniqueId)
								 .map(DonorPlayer::getDonorPlayer)
								 .forEach(DonorPlayer::saveDonor);
	}
	
	private void initConfig() {
		saveDefaultConfig();
		
		if(!getConfig().contains("pearlcooldown") || !(getConfig().get("pearlcooldown") instanceof Long)) {
			getConfig().set("pearlcooldown", 10);
		}
		
		saveConfig();
	}
	
	private void loadDonors() {
		DonorPlayer.runDonorTask();
		Bukkit.getOnlinePlayers().parallelStream().map(Player::getUniqueId).forEach(DonorPlayer::loadDonor);
	}
	
	private void enableBookChecker() {
		if(!getServer().getPluginManager().isPluginEnabled("ProtocolLib")) { 
			getLogger().warning("ProtocolLib is missing. Can't check for book dupes without it.");
			return;
		}
		
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		PacketAdapter.AdapterParameteters params = PacketAdapter.params();
		Set<PacketType> packets = new HashSet<PacketType>();
		
		packets.add(PacketType.Play.Client.CUSTOM_PAYLOAD);
		
		params.types(packets).plugin(this).gamePhase(GamePhase.PLAYING).connectionSide(ConnectionSide.CLIENT_SIDE).listenerPriority(ListenerPriority.HIGHEST);
		
		manager.addPacketListener(new PacketAdapter(params) {		
			@Override
			public void onPacketReceiving(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				try {
					if(packet.getStrings().read(0).equals("MC|BSign")) {
						if(BookOverloadListener.shouldBlock(event.getPlayer())) {
							event.setCancelled(true);
							event.getPlayer().sendMessage("§cCould not write book because you never right clicked it?!?");
						}
					}
				} catch(Exception ignored) {}
			}
		});
	}

}