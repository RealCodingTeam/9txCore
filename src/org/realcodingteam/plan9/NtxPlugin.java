package org.realcodingteam.plan9;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.realcodingteam.plan9.commands.DonorCommand;
import org.realcodingteam.plan9.commands.StaffChatCommand;
import org.realcodingteam.plan9.listeners.DonorListener;
import org.realcodingteam.plan9.listeners.NtxNerfListener;
import org.realcodingteam.plan9.objects.DonorPlayer;

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
		//register commands
		getCommand("donor").setExecutor(new DonorCommand());
		getCommand("staff").setExecutor(new StaffChatCommand());
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

}