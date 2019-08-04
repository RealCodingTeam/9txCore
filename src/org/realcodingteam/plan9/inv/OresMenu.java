package org.realcodingteam.plan9.inv;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.objects.effects.OreEffects;

public class OresMenu extends AbstractMenu {

    public OresMenu(Player viewer) {
        super(9, ChatColor.DARK_PURPLE + "Donor - Ores", viewer, RootMenu::new);
        
        build();
        open(viewer);
    }

    @Override
    protected void build() {
        inv.setItem(1, makeItem(Material.IRON_ORE,    1,    "§5Double Ore Drops - 50 DP",      "§aDouble Ore Drops & Auto-Smelt for 1 Minute"));
        inv.setItem(3, makeItem(Material.GOLD_ORE,    2,    "§5Double Ore Drops - 100 DP",     "§aDouble Ore Drops & Auto-Smelt for 2 Minutes"));
        inv.setItem(5, makeItem(Material.DIAMOND_ORE, 3,    "§5Double Ore Drops - 175 DP",     "§aDouble Ore Drops & Auto-Smelt for 4 Minutes"));
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        OreEffects tier;
        
        if(item.getType() == Material.IRON_ORE) tier = OreEffects.TIER_1;
        else if(item.getType() == Material.GOLD_ORE) tier = OreEffects.TIER_2;
        else if(item.getType() == Material.DIAMOND_ORE) tier = OreEffects.TIER_3;
        else return;
        
        tier.buyAndRun(viewer, getLore(item));
    }
}
