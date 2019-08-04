package org.realcodingteam.plan9.inv;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class RootMenu extends AbstractMenu {
    
    private static final Map<Material, Consumer<Player>> children = new HashMap<>();
    static {
        children.put(Material.EXPERIENCE_BOTTLE, ExpMenu::new);
        children.put(Material.BREWING_STAND, PotionsMenu::new);
        children.put(Material.WHITE_WOOL, NickMenu::new);
        children.put(Material.GOLD_ORE, OresMenu::new);
        children.put(Material.COOKIE, MiscMenu::new);
    }

    public RootMenu(Player viewer) {
        super(27, ChatColor.DARK_PURPLE + "Donor", viewer, null);
        
        build();
        open(viewer);
    }
    
    @Override
    protected void build() {
        inv.setItem(9,  makeItem(Material.WHITE_WOOL,            "§5Nick",             "§aChange your chat color!"));
        inv.setItem(11, makeItem(Material.EXPERIENCE_BOTTLE,     "§5XP",               "§aGive everyone online XP!"));
        inv.setItem(13, makeItem(Material.GOLD_ORE,              "§5Ores",             "§aIncrease the drop rate of ores!"));
        inv.setItem(15, makeItem(Material.BREWING_STAND,         "§5Potion Effects",   "§aGive everyone a cool effect!"));
        inv.setItem(17, makeItem(Material.COOKIE,                "§5Misc",             "§aDo other amazing stuff!"));
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        if(!children.containsKey(item.getType())) return;
        children.get(item.getType()).accept(viewer);
    }
}
