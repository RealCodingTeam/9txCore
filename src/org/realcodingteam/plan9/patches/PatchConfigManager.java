package org.realcodingteam.plan9.patches;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.realcodingteam.plan9.NtxPlugin;

public final class PatchConfigManager {
    private static final Map<String, File> files = new HashMap<>();
    private static final Map<String, FileConfiguration> configs = new HashMap<>();
    
    private static final File ROOT_DIRECTORY;
    static {
        ROOT_DIRECTORY = new File(NtxPlugin.instance().getDataFolder(), "patches");
        ROOT_DIRECTORY.mkdir();
    }
    
    protected static ConfigurationSection getConfig(TxPatch patch) {
        return getPatchConfig(patch);
    }
    
    protected static void saveConfig(TxPatch patch) {
        File file = getFile(patch);
        FileConfiguration config = configs.getOrDefault(patch.getMetadata().internal_name(), YamlConfiguration.loadConfiguration(file));
        
        try {
            config.save(file);
        } catch(IOException ex) {
            NtxPlugin.instance().getLogger().warning("Could not save a patch config file.");
        }
        
        configs.put(patch.getMetadata().internal_name(), config);
    }
    
    private static FileConfiguration getPatchConfig(TxPatch patch) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(getFile(patch));
        configs.put(patch.getMetadata().internal_name(), config);
        
        if(!config.isSet("enabled")) {
            config.set("enabled", true);
        }
        
        saveConfig(patch);
        return config;
    }
    
    private static File getFile(TxPatch patch) {
        String name = patch.getMetadata().internal_name();
        
        if(files.containsKey(name)) {
            return files.get(name);
        }
        File file = new File(ROOT_DIRECTORY, name + ".yml");
        files.put(name, file);
        
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
        } catch(IOException ex) {
            NtxPlugin.instance().getLogger().warning("Could not create a patch config file.");
        }
        
        return file;
    }
}
