package org.realcodingteam.plan9.patches;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

public interface TxPatch extends Listener {
    
    /**
     * Load anything from configuration
     */
    public void loadConfig(ConfigurationSection config);
    
    /**
     * Save any configuration options to the plugin's config
     */
    public void onDisable();
    
    /**
     * Load any configuration options from the plugin's config
     */
    public void onEnable();
    
    /**
     * Is this patch enabled?
     */
    public boolean isEnabled();
    
}
