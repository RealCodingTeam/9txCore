package org.realcodingteam.plan9.patches;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.realcodingteam.plan9.NtxPlugin;

public final class PatchManager {
    private static final Map<String, TxPatch> patches = new HashMap<>();
    
    public static void enablePatch(TxPatch patch) {
        Patch info = getPatchMetadata(patch);
        
        if(patches.containsKey(info.internal_name())) {
            throw new IllegalArgumentException("This patch has already been registered: " + info.internal_name());
        }
        
        patches.put(info.internal_name(), patch);
        patch.onEnable(getRootConfigNode(info));
        Bukkit.getPluginManager().registerEvents(patch, NtxPlugin.instance());
    }
    
    public static void disablePatch(TxPatch patch) {
        if(!patches.containsKey(getPatchMetadata(patch).internal_name())) return;
        
        patch.onDisable(getRootConfigNode(getPatchMetadata(patch)));
        patches.remove(getPatchMetadata(patch).internal_name());
        unregisterEvents(patch);
    }
    
    public static boolean isEnabled(TxPatch patch) {
        Patch info = getPatchMetadata(patch);
        return patches.containsKey(info.internal_name()) && patches.get(info.internal_name()).isEnabled();
    }
    
    public static TxPatch[] getPatches() {
        return patches.values().toArray(new TxPatch[0]);
    }
    
    private static void unregisterEvents(TxPatch patch) {
        HandlerList.unregisterAll(patch); //not-null handled by spigot methods
    }
    
    public static Patch getPatchMetadata(TxPatch patch) {
        Patch info = patch.getClass().getAnnotation(Patch.class);
        if(info == null) {
            throw new IllegalArgumentException("Patches must be annotated by org.realcodingteam.plan9.patches.Patch");
        }
        
        return info;
    }
    
    private static ConfigurationSection getRootConfigNode(Patch info) {
        FileConfiguration config = NtxPlugin.instance().getConfig();
        
        if(config.getConfigurationSection(info.internal_name()) == null) {
            config.createSection(info.internal_name());
            NtxPlugin.instance().saveConfig();
        }
        
        ConfigurationSection root = config.getConfigurationSection(info.internal_name());
        root.set("enabled", true);
        NtxPlugin.instance().saveConfig();
        return root;
    }
}
