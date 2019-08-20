package org.realcodingteam.plan9.inv.scripts;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.realcodingteam.plan9.NtxPlugin;

public abstract class MenuConfig {

    private static final File root = new File(NtxPlugin.instance().getDataFolder(), "menus");
    
    public static void loadFrom(String path) {
        FileConfiguration yaml = getConfigFile(path);
        
        for(String name : yaml.getConfigurationSection("items").getKeys(false)) {
            MenuEntry entry = MenuEntry.deserialize(yaml.getConfigurationSection("items." + name).getValues(false));
            entry.getCategory().addEntry(entry);
        }
    }
    
    private static FileConfiguration getConfigFile(String path) {
        if(!root.exists()) root.mkdir();
        if(!path.toLowerCase().endsWith(".yml")) path += ".yml";
        
        File config = new File(root, path);
        if(!config.exists()) {
            try {
                config.createNewFile();
            } catch(IOException ignored) {}
        }
        
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(config);
        if(!yaml.contains("items")) {
            yaml.createSection("items");
            try {
                yaml.save(config);
            } catch(IOException ignored) {}
        }
        
        return yaml;
    }
    
}
