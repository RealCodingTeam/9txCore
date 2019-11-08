package org.realcodingteam.plan9.patches;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

public interface TxPatch extends Listener {
    
    /**
     * Save any configuration options to the plugin's config
     */
    public void onDisable(ConfigurationSection root);
    
    /**
     * Load any configuration options from the plugin's config
     */
    public void onEnable(ConfigurationSection config);
    
    /**
     * Is this patch enabled?
     */
    public boolean isEnabled();
}
