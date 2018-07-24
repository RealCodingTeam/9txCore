package org.realcodingteam.plan9;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;
import org.realcodingteam.plan9.commands.DonorCommand;
import org.realcodingteam.plan9.commands.KSCommand;
import org.realcodingteam.plan9.objects.DonorEffects;
import org.realcodingteam.plan9.objects.DonorMenu;

public class NtxPlugin extends JavaPlugin {
	
	public static NtxPlugin instance;
	
	@Override
	public void onEnable() {//young man we need to follow one style
		instance = this;
		DonorEffects.init();
		initConfig();
		//register events
		getServer().getPluginManager().registerEvents(new NtxNerfListener(), this);
		getServer().getPluginManager().registerEvents(new DonorListener(), this);
		//register commands
		getCommand("addks").setExecutor(new KSCommand());
		getCommand("donor").setExecutor(new DonorCommand());
	}
	
	public void onDisable() {
		savePoints();
	}
	
	private void initConfig() {
		saveDefaultConfig();
		if(!getConfig().contains("pearlcooldown") || !(getConfig().get("pearlcooldown") instanceof Long)) {
			getConfig().set("pearlcooldown", 10);
		}
		
		if(getConfig().contains("points")) {
			List<String> list = getConfig().getStringList("points");
			
			for (String s : list) {
				String[] args = s.split(":");
				DonorMenu.points.put(UUID.fromString(args[0]), Integer.parseInt(args[1]));
			}
		}
		saveConfig();
	}
	
	private void savePoints() {
		List<String> list = new ArrayList<>();
		
		for (UUID id : DonorMenu.points.keySet()) {
			list.add(id + ":" + DonorMenu.points.get(id));
		}
		
		getConfig().set("points", list);
		saveConfig();
	}
}