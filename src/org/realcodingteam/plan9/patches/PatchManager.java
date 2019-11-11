package org.realcodingteam.plan9.patches;

import java.util.*;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.realcodingteam.plan9.NtxPlugin;

public final class PatchManager {
    private static final Map<String, TxPatch> patches = new HashMap<>();
    
    /**
     * Return a registered patch by internal ID, or {@link Optional#empty()}
     */
    public static Optional<TxPatch> getPatchById(String internal_id) {
        Validate.notNull(internal_id);
        
        return Optional.ofNullable(patches.get(internal_id));
    }
    
    /**
     * Load a patch into the system. This is called as a side effect
     * of {@link PatchManager#enablePatch(TxPatch)}
     */
    public static void loadPatch(TxPatch patch) {
        Validate.notNull(patch);
        if(isEnabled(patch)) return;
        
        Patch info = patch.getMetadata();
        patches.put(info.internal_name(), patch);
        patch.loadConfig(getRootConfigNode(patch));
    }
    
    /**
     * Calls {@link TxPatch#loadConfig(ConfigurationSection)} on all registered patches
     */
    public static void reloadConfigForPatches() {
        for(TxPatch patch : getPatches()) {
            patch.loadConfig(getRootConfigNode(patch));
        }
    }
    
    /**
     * Unload a patch from the system. This should be called
     * when your plugin's onDisable is called to remove inactive patches.
     */
    public static void unloadPatch(TxPatch patch) {
        Validate.notNull(patch);
        if(!isEnabled(patch)) return;
        
        patch.onDisable();
        patches.remove(patch.getMetadata().internal_name());
        unregisterEvents(patch);
    }
    
    /**
     * Enables a patch and registers event handlers.
     * Calls {@link PatchManager#loadPatch(TxPatch)} as a side effect.
     */
    public static void enablePatch(TxPatch patch) {
        Validate.notNull(patch);
        if(isEnabled(patch)) return;
        loadPatch(patch);
        
        if(!getRootConfigNode(patch).getBoolean("enabled")) return;
        patch.onEnable();
        Bukkit.getPluginManager().registerEvents(patch, NtxPlugin.instance());
    }
    
    public static void forceEnable(TxPatch patch) {
        Validate.notNull(patch);
        loadPatch(patch);
        getRootConfigNode(patch).set("enabled", true);
        NtxPlugin.instance().saveConfig();
        enablePatch(patch);
    }
    
    /**
     * Disables a patch, but keeps it in the system.
     * Unregisters event handlers and calls {@link TxPatch#onDisable(ConfigurationSection)} 
     */
    public static void disablePatch(TxPatch patch) {
        Validate.notNull(patch);
        if(!isEnabled(patch)) return;
        
        unregisterEvents(patch);
        patch.onDisable();
        getRootConfigNode(patch).set("enabled", false);
        NtxPlugin.instance().saveConfig();
    }
    
    public static boolean isEnabled(TxPatch patch) {
        Validate.notNull(patch);
        Patch info = patch.getMetadata();
        return patches.containsKey(info.internal_name()) && patches.get(info.internal_name()).isEnabled();
    }
    
    public static Collection<TxPatch> getPatches() {
        return Collections.unmodifiableCollection(patches.values());
    }
    
    private static void unregisterEvents(TxPatch patch) {
        Validate.notNull(patch);
        HandlerList.unregisterAll(patch);
    }
    
    private static ConfigurationSection getRootConfigNode(TxPatch patch) {
        Validate.notNull(patch);
        FileConfiguration config = NtxPlugin.instance().getConfig();
        Patch info = patch.getMetadata();
        
        if(config.getConfigurationSection(info.internal_name()) == null) {
            config.createSection(info.internal_name());
            NtxPlugin.instance().saveConfig();
        }
        
        ConfigurationSection root = config.getConfigurationSection(info.internal_name());
        
        if(!root.isSet("enabled")) {
            root.set("enabled", true);
            NtxPlugin.instance().saveConfig();
        }
        
        return root;
    }
}
