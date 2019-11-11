package org.realcodingteam.plan9.patches;

import java.util.*;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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
        patch.loadConfig(PatchConfigManager.getConfig(patch));
        PatchConfigManager.saveConfig(patch);
    }
    
    /**
     * Calls {@link TxPatch#loadConfig(ConfigurationSection)} on all registered patches
     */
    public static void reloadConfigForPatches() {
        for(TxPatch patch : getPatches()) {
            patch.loadConfig(PatchConfigManager.getConfig(patch));
            PatchConfigManager.saveConfig(patch);
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
        
        if(!PatchConfigManager.getConfig(patch).getBoolean("enabled")) return;
        patch.onEnable();
        Bukkit.getPluginManager().registerEvents(patch, NtxPlugin.instance());
    }
    
    public static void forceEnable(TxPatch patch) {
        Validate.notNull(patch);
        loadPatch(patch);
        PatchConfigManager.getConfig(patch).set("enabled", true);
        PatchConfigManager.saveConfig(patch);
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
        PatchConfigManager.getConfig(patch).set("enabled", false);
        PatchConfigManager.saveConfig(patch);
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
}
