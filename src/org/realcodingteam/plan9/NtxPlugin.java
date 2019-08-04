package org.realcodingteam.plan9;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.realcodingteam.plan9.chat.ChatListener;
import org.realcodingteam.plan9.commands.DonorCommand;
import org.realcodingteam.plan9.commands.StaffChatCommand;
import org.realcodingteam.plan9.inv.AbstractMenu;
import org.realcodingteam.plan9.inv.SlotsMenu;
import org.realcodingteam.plan9.listeners.BookOverloadListener;
import org.realcodingteam.plan9.listeners.DonorListener;
import org.realcodingteam.plan9.listeners.DrownedDupeListener;
import org.realcodingteam.plan9.listeners.NtxNerfListener;
import org.realcodingteam.plan9.objects.BookProtocolBlocker;
import org.realcodingteam.plan9.objects.DonorPlayer;

public class NtxPlugin extends JavaPlugin {
    
    private static NtxPlugin instance;
    
    public static NtxPlugin getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        instance = this;
        
        initConfig();
        loadDonors();
        //register events
        getServer().getPluginManager().registerEvents(new NtxNerfListener(), this);      //Disables normal vanilla functionality
        getServer().getPluginManager().registerEvents(new DonorListener(), this);        //Triple ore effect and /donor menu clicking
        getServer().getPluginManager().registerEvents(new DrownedDupeListener(), this);  //Prevents the zombie conversion dupe
        getServer().getPluginManager().registerEvents(new BookOverloadListener(), this); //Prevents the written book chunk overload dupe
        //register commands
        getCommand("donor").setExecutor(new DonorCommand());
        getCommand("donor").setTabCompleter(new DonorCommand());
        
        getCommand("staff").setExecutor(new StaffChatCommand());
        
        enableBookChecker(); //ProtocolLib required written book chunk overload dupe prevention
        enableItemChat();    //Displaying items in chat, requires Essentials to work to prevent mute bypass
    }
    
    @Override
    public void onDisable() {
        //save all donor players
        Bukkit.getOnlinePlayers().stream()
                                 .map(Player::getUniqueId)
                                 .map(DonorPlayer::getDonorPlayer)
                                 .forEach(DonorPlayer::saveDonor);
        AbstractMenu.closeOpenInvs();
        SlotsMenu.save();
    }
    
    private void initConfig() {
        saveDefaultConfig();
        
        //Load enderpearl delay from config
        if(!getConfig().contains("pearlcooldown") || !(getConfig().get("pearlcooldown") instanceof Long)) {
            getConfig().set("pearlcooldown", 10);
        }
        
        saveConfig();
    }
    
    private void loadDonors() {
        //Grants donor points every 15 minutes
        DonorPlayer.runDonorTask();
        //This only has an effect when the server is reloaded.
        //Ensures every player is loaded as a DonorPlayer
        Bukkit.getOnlinePlayers().parallelStream().map(Player::getUniqueId).forEach(DonorPlayer::loadDonor);
    }
    
    private void enableBookChecker() {
        /*
         * This version of the book dupe mitigation required ProtocolLib because
         * it listens for PacketPlayCustomPayload with ID "MC|BSign". 
         * If ProtocolLib is not on the server, this plugin can only use the
         * event-based prevention. See BookOverloadListener.
         */
        if(getServer().getPluginManager().getPlugin("ProtocolLib") == null) { 
            getLogger().warning("ProtocolLib is missing. Can't check for book dupes without it.");
            return;
        }
        
        BookProtocolBlocker.start();
    }
    
    private void enableItemChat() {
        /*
         * Item chat allows players to display the items they have in their hotbar
         * in chat as hover-able messages. This requires Essentials to prevent
         * players from bypassing mutes because item chat doesn't actually use
         * a chat message, it just creates a faked "/me" broadcast containing
         * the text components for each item the player is displaying.
         */
        if(getServer().getPluginManager().getPlugin("Essentials") == null) {
            getLogger().warning("Essentials is missing. Can't do [item] chat without it.");
            return;
        }
        
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

}