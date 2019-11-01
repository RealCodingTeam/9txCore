package org.realcodingteam.plan9;

import org.bukkit.plugin.java.JavaPlugin;
import org.realcodingteam.plan9.commands.StaffChatCommand;
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
        //register events
        getServer().getPluginManager().registerEvents(new DrownedDupeListener(), this);      //Disables normal vanilla functionality
        getServer().getPluginManager().registerEvents(new NtxNerfListener(), this);      //Disables normal vanilla functionality
        //register commands
        getCommand("staff").setExecutor(new StaffChatCommand());
    }
    
    private void initConfig() {
        saveDefaultConfig();
        
        //Load enderpearl delay from config
        if(!getConfig().contains("pearlcooldown") || !(getConfig().get("pearlcooldown") instanceof Long)) {
            getConfig().set("pearlcooldown", 10);
        }
        
        saveConfig();
    }

}