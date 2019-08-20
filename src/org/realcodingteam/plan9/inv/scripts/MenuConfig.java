package org.realcodingteam.plan9.inv.scripts;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.realcodingteam.plan9.NtxPlugin;

public abstract class MenuConfig {

    private static final File root = new File(NtxPlugin.instance().getDataFolder(), "menus");
    
    public static void loadFrom(String path) {
        File source = getConfigFile(path);
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(source);
        
        for(String name : yaml.getConfigurationSection("items").getKeys(false)) {
            MenuEntry entry = MenuEntry.deserialize(yaml.getConfigurationSection("items." + name).getValues(false));
            entry.getCategory().addEntry(entry);
        }
    }
    
    private static File getConfigFile(String path) {
        if(!root.exists()) root.mkdir();
        if(!path.toLowerCase().endsWith(".yml")) path += ".yml";
        
        return new File(root, path);
    }
    
}
