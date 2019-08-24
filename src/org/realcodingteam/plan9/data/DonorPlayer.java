package org.realcodingteam.plan9.data;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.realcodingteam.plan9.NtxPlugin;

//Wrapper over UUID (basically OfflinePlayer) for use with /donor
//Written by Luz
public class DonorPlayer {
    
    private static final Map<UUID, DonorPlayer> donors = new HashMap<>();
    
    public static Collection<DonorPlayer> getAllPlayers() {
        return donors.values();
    }

    private UUID id;
    private int dp;
    private String nick;
    private boolean hasReceivedDP, receiveEffects;
    private long lastLogin;
    private int rolls;
    
    public DonorPlayer(UUID id) {
        this.id = id;
        this.dp = 0;
        this.hasReceivedDP = false;
        this.nick = "f";
        this.rolls = 0;
        this.receiveEffects = true;
        this.setLastLogin(System.currentTimeMillis());
    }
    
    public DonorPlayer(UUID id, int dp, boolean hasReceivedDP, String nick, int rolls, boolean effects) {
        this.id = id;
        this.dp = dp;
        this.hasReceivedDP = hasReceivedDP;
        this.nick = nick;
        this.rolls = rolls;
        this.receiveEffects = effects;
        this.setLastLogin(System.currentTimeMillis());
    }
    
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(id);
    }
    
    public int getRolls() {
        return rolls;
    }
    
    public void setRolls(int rolls) {
        this.rolls = rolls;
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
    
    public boolean receivesEffects() {
        return receiveEffects;
    }
    
    public void setShouldReceiveEffects(boolean receiveEffects) {
        this.receiveEffects = receiveEffects;
    }
    
    public static void onEnable() {
        if(!donors.isEmpty()) return;
        Bukkit.getOnlinePlayers().stream().map(player -> player.getUniqueId()).forEach(DonorPlayer::loadDonor);
    }

    public static DonorPlayer getDonorPlayer(UUID id) {
        if (!donors.containsKey(id)) {
            return loadDonor(id);            
        }
        
        return donors.get(id);
    }
    
    private static DonorPlayer loadDonor(UUID id) {
        File playerFile = new File(NtxPlugin.instance().getDataFolder(), "Players/" + id + ".txt");
        
        if (!playerFile.exists()) {
            DonorPlayer d = new DonorPlayer(id);
            
            donors.put(id, d);
            return d;
        }
        
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(playerFile);
        int dp = yaml.getInt("points");
        boolean has = yaml.getBoolean("hasReceived");
        String c = yaml.getString("nick");
        
        if(!yaml.contains("rolls")) yaml.set("rolls", 0);
        int rolls = yaml.getInt("rolls");
        
        if(!yaml.contains("receive_effects")) yaml.set("receive_effects", true);
        boolean effects = yaml.getBoolean("receive_effects");
        
        DonorPlayer donor = new DonorPlayer(id, dp, has, c, rolls, effects);
        
        donors.put(id, donor);
        return donor;
    }
    
    public static void saveDonor(DonorPlayer... players) {
        for(DonorPlayer dp : players) {
            File playerFile = new File(NtxPlugin.instance().getDataFolder(), "Players/" + dp.getId() + ".txt");
            FileConfiguration yaml = YamlConfiguration.loadConfiguration(playerFile);
            
            yaml.set("points", dp.getDp());
            yaml.set("hasReceived", dp.hasReceivedDP()); 
            yaml.set("nick", dp.getNick());
            yaml.set("rolls", dp.getRolls());
            yaml.set("receive_effects", dp.receivesEffects());
            donors.remove(dp.getId());
            
            try {
                yaml.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void runDonorTask() {
        BukkitScheduler scheduler = NtxPlugin.instance().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(NtxPlugin.instance(), new Runnable() {
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
                            int amount = 1;
                            if(p.hasPermission("ntx.end")) amount = 5;
                            else if(p.hasPermission("ntx.emperor") || p.hasPermission("ntx.king")) amount = 3;
                            else if(p.hasPermission("ntx.general") || p.hasPermission("ntx.knight") || p.hasPermission("ntx.soldier")) amount = 2;
                            
                            dp.setLastLogin(dp.getLastLogin() + 900000);
                            dp.setDp(dp.getDp() + amount);
                            p.sendMessage(ChatColor.GREEN + "You just recieved " + amount + " donor point(s) for being online 15 minutes!");
                        }
                    }
                }
            }
        }, 0L, 300 * 20L);
    }

}