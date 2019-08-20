package org.realcodingteam.plan9;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.realcodingteam.plan9.chat.ChatListener;
import org.realcodingteam.plan9.commands.DonorCommand;
import org.realcodingteam.plan9.commands.ReloadCommand;
import org.realcodingteam.plan9.commands.StaffChatCommand;
import org.realcodingteam.plan9.data.DonorPlayer;
import org.realcodingteam.plan9.inv.AbstractMenu;
import org.realcodingteam.plan9.inv.SlotsMenu;
import org.realcodingteam.plan9.listeners.DonorListener;
import org.realcodingteam.plan9.listeners.DrownedDupeListener;
import org.realcodingteam.plan9.listeners.NtxNerfListener;

public class NtxPlugin extends JavaPlugin {
    
    private static NtxPlugin instance;
    
    public static NtxPlugin instance() {
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
        //register commands
        getCommand("donor").setExecutor(new DonorCommand());
        getCommand("donor").setTabCompleter(new DonorCommand());
        getCommand("staff").setExecutor(new StaffChatCommand());
        getCommand("reload").setExecutor(new ReloadCommand());
        
        enableItemChat();    //Displaying items in chat, requires Essentials to work to prevent mute bypass
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> AbstractMenu.triggerRefresh(), 10L, 10L);
        SlotsMenu.save();
    }
    
    @Override
    public void onDisable() {
        AbstractMenu.closeOpenInvs();
        Bukkit.getOnlinePlayers().stream()
            .map(Player::getUniqueId)
            .map(DonorPlayer::getDonorPlayer)
            .forEach(DonorPlayer::saveDonor);
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
        DonorPlayer.onEnable();
        //Grants donor points every 15 minutes
        DonorPlayer.runDonorTask();
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
            getLogger().warning("Essentials is missing. Can't do <item> chat without it.");
            return;
        }
        
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

}