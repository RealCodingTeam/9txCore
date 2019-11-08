package org.realcodingteam.plan9;

import org.bukkit.plugin.java.JavaPlugin;
import org.realcodingteam.plan9.commands.PatchCommand;
import org.realcodingteam.plan9.commands.StaffChatCommand;

public final class NtxPlugin extends JavaPlugin {
    
    private static NtxPlugin instance;
    
    public static NtxPlugin instance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        instance = this;
        
        initConfig();
        //register commands
        getCommand("staff").setExecutor(new StaffChatCommand());
        getCommand("patch").setExecutor(new PatchCommand());
    }
    
    private void initConfig() {
        saveDefaultConfig();
        saveConfig();
    }

}