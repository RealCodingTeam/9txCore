package org.realcodingteam.plan9;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.realcodingteam.plan9.chat.ChatListener;
import org.realcodingteam.plan9.commands.DonorCommand;
import org.realcodingteam.plan9.commands.StaffChatCommand;
import org.realcodingteam.plan9.listeners.BookOverloadListener;
import org.realcodingteam.plan9.listeners.DonorListener;
import org.realcodingteam.plan9.listeners.DrownedDupeListener;
import org.realcodingteam.plan9.listeners.NtxNerfListener;
import org.realcodingteam.plan9.objects.BookProtocolBlocker;
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
        getServer().getPluginManager().registerEvents(new DrownedDupeListener(), this);
        getServer().getPluginManager().registerEvents(new BookOverloadListener(), this);
        //register commands
        getCommand("donor").setExecutor(new DonorCommand());
        getCommand("staff").setExecutor(new StaffChatCommand());
        
        enableBookChecker();
        enableItemChat();
    }
    
    @Override
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
        if(getServer().getPluginManager().getPlugin("ProtocolLib") == null) { 
            getLogger().warning("ProtocolLib is missing. Can't check for book dupes without it.");
            return;
        }
        
        BookProtocolBlocker.start();
    }
    
    private void enableItemChat() {
        if(getServer().getPluginManager().getPlugin("Essentials") == null) {
            getLogger().warning("Essentials is missing. Can't do [item] chat without it.");
            return;
        }
        
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

}