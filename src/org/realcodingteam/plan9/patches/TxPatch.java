package org.realcodingteam.plan9.patches;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

public interface TxPatch extends Listener {
    
    public default Patch getMetadata() {
        Patch info = this.getClass().getAnnotation(Patch.class);
        if(info == null) {
            throw new IllegalArgumentException("Patches must be annotated by org.realcodingteam.plan9.patches.Patch");
        }
        
        return info;
    }
    
    /**
     * Load anything from configuration
     */
    public default void loadConfig(ConfigurationSection config) {}
    
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
