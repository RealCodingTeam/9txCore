package org.realcodingteam.plan9.inv;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.objects.effects.ExpEffects;

public final class ExpMenu extends AbstractMenu {

    public ExpMenu(Player viewer) {
        super(9, ChatColor.DARK_PURPLE + "Donor - XP", viewer, RootMenu::new);
        
        build();
        open(viewer);
    }
    
    @Override
    protected void build() {
        inv.setItem(1, makeItem(Material.EXPERIENCE_BOTTLE,     "§52x Experience (1 minute) - 15 DP",  "§eDouble experience drops for 1 minute"));
        inv.setItem(3, makeItem(Material.EXPERIENCE_BOTTLE, 2,  "§52x Experience (2 minutes) - 30 DP", "§eDouble experience drops for 2 minutes"));
        inv.setItem(5, makeItem(Material.EXPERIENCE_BOTTLE, 3,  "§52x Experience (3 minutes) - 50 DP", "§eDouble experience drops for 3 minutes"));
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        ExpEffects effect = ExpEffects.fromAmount(item.getAmount());
        effect.buyAndRun(viewer, getLore(item));
    }
}
