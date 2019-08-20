package org.realcodingteam.plan9.inv.scripts;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.util.Item;

public final class MenuEntry {
    
    private final Menu category;
    private final String name;
    private final ItemStack item;
    private final int cost;
    private final int slot;
    private final String script;
    
    public MenuEntry(Menu category, String name, ItemStack item, int cost, int slot, String script) {
        category.addEntry(this);
        
        this.category = category;
        this.name = name;
        this.item = item;
        this.cost = cost;
        this.slot = slot;
        this.script = script;
    }
    
    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getCost() {
        return cost;
    }

    public int getMenuSlot() {
        return slot;
    }

    public String getScript() {
        return script;
    }
    
    public Menu getCategory() {
        return category;
    }
    
    public static MenuEntry deserialize(Map<String, Object> args) {
        Menu category = Menu.fromName((String)args.get("category"));
        int slot = (Integer)args.get("slot");
        int cost = (Integer)args.get("cost");
        String script = (String)args.get("script");
        
        String name = (String)args.get("name");
        Material material = Material.matchMaterial((String)args.get("material"));
        @SuppressWarnings("unchecked")
        List<String> lore = (List<String>)args.get("lore");
        int amount = (Integer)args.get("amount");
        ItemStack item = Item.makeItem(material, amount, name, lore.toArray(new String[0]));
        
        return new MenuEntry(category, name, item, cost, slot, script);
        
    }
    
}
