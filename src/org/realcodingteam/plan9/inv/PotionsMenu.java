package org.realcodingteam.plan9.inv;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.objects.effects.PotionsEffects;

public class PotionsMenu extends AbstractMenu {

    public PotionsMenu(Player viewer) {
        super(36, ChatColor.DARK_PURPLE + "Donor - Potion Effects", viewer, true);
        
        build();
        open(viewer);
    }
    
    @Override
    protected void build() {
        inv.setItem(0,  makeItem(Material.SUGAR, 1, "§5Speed III - 15 DP", "§aSpeed III for 2 minutes"));
        inv.setItem(9,  makeItem(Material.SUGAR, 2, "§5Speed III - 30 DP", "§aSpeed III for 4 minutes"));
        inv.setItem(18, makeItem(Material.SUGAR, 3, "§5Speed III - 50 DP", "§aSpeed III for 6 minutes"));
        
        inv.setItem(2,  makeItem(Material.GOLDEN_CARROT, 1, "§5Invisibility - 15 DP", "§aInvisibility for 2 minutes"));
        inv.setItem(11, makeItem(Material.GOLDEN_CARROT, 2, "§5Invisibility - 30 DP", "§aInvisibility for 4 minutes"));
        inv.setItem(20, makeItem(Material.GOLDEN_CARROT, 3, "§5Invisibility - 50 DP", "§aInvisibility for 6 minutes"));
        
        inv.setItem(4,  makeItem(Material.GOLDEN_PICKAXE, 1, "§5Haste II - 50 DP",  "§aHaste II for 2 minutes"));
        inv.setItem(13, makeItem(Material.GOLDEN_PICKAXE, 2, "§5Haste II - 100 DP", "§aHaste II for 4 minutes"));
        inv.setItem(22, makeItem(Material.GOLDEN_PICKAXE, 3, "§5Haste II - 150 DP", "§aHaste II for 6 minutes"));
        
        inv.setItem(6,  makeItem(Material.GLOWSTONE, 1, "§5Glowing - 15 DP",  "§aGlowing for 2 minutes"));
        inv.setItem(15, makeItem(Material.GLOWSTONE, 2, "§5Glowing - 30 DP", "§aGlowing for 4 minutes"));
        inv.setItem(24, makeItem(Material.GLOWSTONE, 3, "§5Glowing - 60 DP", "§aGlowing for 6 minutes"));
        
        inv.setItem(8,  makeItem(Material.GHAST_TEAR, 1, "§5Regeneration II - 30 DP",   "§aRegeneration II for 30 seconds"));
        inv.setItem(17, makeItem(Material.GHAST_TEAR, 2, "§5Regeneration II - 60 DP",  "§aRegeneration II for 1 minute"));
        inv.setItem(26, makeItem(Material.GHAST_TEAR, 3, "§5Regeneration II - 120 DP", "§aRegeneration II for 2 minutes"));
        
        inv.setItem(29, makeItem(Material.HEART_OF_THE_SEA, 1, "§5Dolphin's Grace - 50 DP", "§aDolphin's grace for 2 minutes"));
        inv.setItem(31, makeItem(Material.MAGMA_CREAM, 1, "§5Fire Resistance - 30 DP", "§aFire resistance for 4 minutes"));
        inv.setItem(33, makeItem(Material.CONDUIT, 1, "§5Conduit Power - 100 DP", "§aConduit power for 4 minutes", "§aWater breathing, night vision, and haste"));
        inv.setItem(27, makeItem(Material.FEATHER, 1, "§5Slow Falling - 30 DP", "§aSlow falling for 3 minutes"));
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        PotionsEffects.fromItem(item).buyAndRun(viewer, getLore(item));
    }
    
}
