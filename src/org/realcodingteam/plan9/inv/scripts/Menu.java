package org.realcodingteam.plan9.inv.scripts;

import java.util.ArrayList;
import java.util.List;

public enum Menu {
    POTIONS("potions_menu.yml"), MISC("misc_menu.yml");
    
    private final String path;
    private final List<MenuEntry> entries;
    private boolean loaded = false;
    
    private Menu(String path) {
        this.path = path;
        this.entries = new ArrayList<>();
    }
    
    public void addEntry(MenuEntry entry) {
        entries.add(entry);
    }
    
    public List<MenuEntry> getEntries() {
        if(!loaded) {
            MenuConfig.loadFrom(path);
            loaded = true;
        }
        
        return new ArrayList<MenuEntry>(entries);
    }
    
    public static void clearEntries() {
        for(Menu menu : values()) {
            menu.entries.clear();
            menu.loaded = false;
        }
    }
    
    public static Menu fromName(String name) {
        for(Menu menu : values()) {
            if(menu.name().equalsIgnoreCase(name)) return menu;
        }
        
        return null;
    }
}
