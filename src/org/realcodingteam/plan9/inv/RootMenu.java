package org.realcodingteam.plan9.inv;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public final class RootMenu extends AbstractMenu {

    public RootMenu(Player viewer) {
        super(27, ChatColor.DARK_PURPLE + "Donor", viewer, false);
        
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
        
        if(item.getType() == Material.WHITE_WOOL) new NickMenu(viewer);
        else if(item.getType() == Material.EXPERIENCE_BOTTLE) new ExpMenu(viewer);
        else if(item.getType() == Material.GOLD_ORE) new OresMenu(viewer);
        else if(item.getType() == Material.BREWING_STAND) new PotionsMenu(viewer);
        else if(item.getType() == Material.COOKIE) new MiscMenu(viewer);
    }
}
