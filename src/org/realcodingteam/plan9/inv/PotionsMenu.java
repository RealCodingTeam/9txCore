package org.realcodingteam.plan9.inv;

import static org.realcodingteam.plan9.util.Item.getLore;
import static org.realcodingteam.plan9.util.Item.makeItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.inv.effects.PotionsEffects;

public class PotionsMenu extends AbstractMenu {

    public PotionsMenu(Player viewer) {
        super(36, ChatColor.DARK_PURPLE + "Donor - Potion Effects", viewer, RootMenu::new);
        
        build();
        open(viewer);
    }
    
    @Override
    protected void build() {
        inv.setItem(0,  makeItem(Material.SUGAR, 1, "§5Speed III - 20 DP", "§aSpeed III for 2 minutes"));
        inv.setItem(9,  makeItem(Material.SUGAR, 2, "§5Speed III - 40 DP", "§aSpeed III for 4 minutes"));
        inv.setItem(18, makeItem(Material.SUGAR, 3, "§5Speed III - 55 DP", "§aSpeed III for 6 minutes"));
        
        inv.setItem(2,  makeItem(Material.GOLDEN_CARROT, 1, "§5Invisibility - 20 DP", "§aInvisibility for 2 minutes"));
        inv.setItem(11, makeItem(Material.GOLDEN_CARROT, 2, "§5Invisibility - 40 DP", "§aInvisibility for 4 minutes"));
        inv.setItem(20, makeItem(Material.GOLDEN_CARROT, 3, "§5Invisibility - 55 DP", "§aInvisibility for 6 minutes"));
        
        inv.setItem(4,  makeItem(Material.GOLDEN_PICKAXE, 1, "§5Haste II - 65 DP",  "§aHaste II for 2 minutes"));
        inv.setItem(13, makeItem(Material.GOLDEN_PICKAXE, 2, "§5Haste II - 115 DP", "§aHaste II for 4 minutes"));
        inv.setItem(22, makeItem(Material.GOLDEN_PICKAXE, 3, "§5Haste II - 160 DP", "§aHaste II for 6 minutes"));
        
        inv.setItem(6,  makeItem(Material.GLOWSTONE, 1, "§5Glowing - 15 DP",  "§aGlowing for 2 minutes"));
        inv.setItem(15, makeItem(Material.GLOWSTONE, 2, "§5Glowing - 30 DP", "§aGlowing for 4 minutes"));
        inv.setItem(24, makeItem(Material.GLOWSTONE, 3, "§5Glowing - 60 DP", "§aGlowing for 6 minutes"));
        
        inv.setItem(8,  makeItem(Material.GHAST_TEAR, 1, "§5Regeneration II - 20 DP",   "§aRegeneration II for 30 seconds"));
        inv.setItem(17, makeItem(Material.GHAST_TEAR, 2, "§5Regeneration II - 45 DP",  "§aRegeneration II for 1 minute"));
        inv.setItem(26, makeItem(Material.GHAST_TEAR, 3, "§5Regeneration II - 100 DP", "§aRegeneration II for 2 minutes"));
        
        inv.setItem(29, makeItem(Material.HEART_OF_THE_SEA, 1, "§5Dolphin's Grace - 50 DP", "§aDolphin's grace for 2 minutes"));
        inv.setItem(31, makeItem(Material.MAGMA_CREAM, 1, "§5Fire Resistance - 50 DP", "§aFire resistance for 6 minutes"));
        inv.setItem(33, makeItem(Material.CONDUIT, 1, "§5Conduit Power - 85 DP", "§aConduit power for 4 minutes", "§aWater breathing, night vision, and haste"));
        inv.setItem(27, makeItem(Material.FEATHER, 1, "§5Slow Falling - 45 DP", "§aSlow falling for 3 minutes"));
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        PotionsEffects.fromItem(item).buyAndRun(viewer, getLore(item));
    }

    @Override
    public boolean needsRefresh() {
        return false;
    }
    
}
