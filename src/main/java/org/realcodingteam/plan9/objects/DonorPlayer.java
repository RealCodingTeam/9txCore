package org.realcodingteam.plan9.objects;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.realcodingteam.plan9.NtxPlugin;

public class DonorPlayer {
	
	public static Map<UUID, DonorPlayer> donors = new HashMap<>();

	private UUID id;
	private int dp;
	private String nick;
	private boolean hasReceivedDP;
	private long lastLogin;
	
	public DonorPlayer(UUID id) {
		this.id = id;
		this.dp = 0;
		this.hasReceivedDP = false;
		this.nick = "f";
		this.setLastLogin(System.currentTimeMillis());
	}
	
	public DonorPlayer(UUID id, int dp, boolean hasReceivedDP, String nick) {
		this.id = id;
		this.dp = dp;
		this.hasReceivedDP = hasReceivedDP;
		this.nick = nick;
		this.setLastLogin(System.currentTimeMillis());
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getDp() {
		return dp;
	}

	public void setDp(int dp) {
		this.dp = dp;
	}

	public boolean hasReceivedDP() {
		return hasReceivedDP;
	}

	public void setHasReceivedDP(boolean hasReceivedDP) {
		this.hasReceivedDP = hasReceivedDP;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public static DonorPlayer getDonorPlayer(UUID id) {
		if (!donors.containsKey(id)) {
			return loadDonor(id);			
		}
		
		return donors.get(id);
	}
	
	public static DonorPlayer loadDonor(UUID id) {
		File playerFile = new File(NtxPlugin.instance.getDataFolder(), "Players/" + id + ".txt");
		
		if (!playerFile.exists()) {
			DonorPlayer d = new DonorPlayer(id);
			
			donors.put(id, d);
			return d;
		}
		
		FileConfiguration yaml = YamlConfiguration.loadConfiguration(playerFile);
		int dp = yaml.getInt("points");
		boolean has = yaml.getBoolean("hasReceived");
		String c = yaml.getString("nick");
		DonorPlayer donor = new DonorPlayer(id, dp, has, c);
		
		donors.put(id, donor);
		return donor;
	}
	
	public static void saveDonor(DonorPlayer dp) {
		File playerFile = new File(NtxPlugin.instance.getDataFolder(), "Players/" + dp.getId() + ".txt");
		FileConfiguration yaml = YamlConfiguration.loadConfiguration(playerFile);
		
		yaml.set("points", dp.getDp());
		yaml.set("hasReceived", dp.hasReceivedDP()); 
		yaml.set("nick", dp.getNick());
		donors.remove(dp.getId());
		
		try {
			yaml.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void runDonorTask() {
		BukkitScheduler scheduler = NtxPlugin.instance.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(NtxPlugin.instance, new Runnable() {
			@Override
			public void run() {
				for (DonorPlayer dp : donors.values()) {
					Player p = Bukkit.getPlayer(dp.getId());
					
					if (p == null) {
						saveDonor(dp);
						continue;
					}
					
					if (p.hasPermission("ntx.donor.time")) {
						if (System.currentTimeMillis() - dp.lastLogin > 900000) {
							dp.setLastLogin(dp.getLastLogin() + 900000);
							dp.setDp(dp.getDp() + 1);
							p.sendMessage(ChatColor.GREEN + "You just recieved 1 donor point for being online 15 minutes!");
						}
					}
				}
			}
		}, 0L, 300 * 20L);
	}

}
