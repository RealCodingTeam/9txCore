package org.realcodingteam.plan9.inv.scripts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.data.DonorPlayer;
import org.realcodingteam.plan9.inv.AbstractMenu;
import org.realcodingteam.plan9.util.Item;

public final class Effects {

    private Effects() {}
    
    private final static void announce(Player buyer, String display) {
        Bukkit.broadcastMessage("§e[DONOR] §r" + buyer.getDisplayName() + "§d purchased §e" + display + "§d for the server!");
    }
    
    public static final void buyAndRun(Player buyer, MenuEntry effect) {
        DonorPlayer dp = DonorPlayer.getDonorPlayer(buyer.getUniqueId());
        int bal = dp.getDp() - effect.getCost();
        
        if(bal >= 0) {
            dp.setDp(bal);
            AbstractMenu.triggerRefresh();
            announce(buyer, Item.getLore(effect.getItem()));
            buyer.closeInventory();
            ScriptManager.run(buyer, effect);
            return;
        }
    }
}
