package org.realcodingteam.plan9.inv;

import static org.realcodingteam.plan9.util.Item.getLore;
import static org.realcodingteam.plan9.util.Item.makeItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.inv.effects.ExpEffects;
import org.realcodingteam.plan9.util.Item;
import org.realcodingteam.plan9.util.Time;

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
        inv.setItem(7, getEffectTimer());
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        if(item.getType() != Material.EXPERIENCE_BOTTLE) return;
        
        ExpEffects effect = ExpEffects.fromAmount(item.getAmount());
        effect.buyAndRun(viewer, getLore(item));
    }

    @Override
    public boolean needsRefresh() {
        return true;
    }
    
    private ItemStack getEffectTimer() {
        long timeleft = ExpEffects.getTimeLeft();
        String name = timeleft == -1 ? ChatColor.RED + "Double Experience is not active." : ChatColor.YELLOW + "Time left: " + ChatColor.AQUA + Time.format(timeleft);
        
        ItemStack is = new ItemStack(Material.CLOCK, 1);
        is = Item.setDisplayName(is, name);
        return is;
    }
}
