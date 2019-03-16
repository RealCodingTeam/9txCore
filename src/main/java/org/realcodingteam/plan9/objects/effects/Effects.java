package org.realcodingteam.plan9.objects.effects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.objects.DonorPlayer;

public abstract class Effects {

    private final int cost;
    protected Runnable effect;
    
    protected Effects(int cost) {
        this.cost = cost;
    }
    
    public final int getCost() {
        return cost;
    }
    
    private final void run(Player buyer, String display) {
        Bukkit.broadcastMessage("§e[DONOR] §r" + buyer.getDisplayName() + "§d purchased §e" + display + "§d for the server!");
        if(effect != null) effect.run();
        buyer.closeInventory();
    }
    
    public final void buyAndRun(Player buyer, String display) {
        DonorPlayer dp = DonorPlayer.getDonorPlayer(buyer.getUniqueId());    
        int bal = dp.getDp() - cost;
        
        if (bal >= 0) {
            dp.setDp(bal);
            run(buyer, display);
            return;
        }
        
        buyer.sendMessage(ChatColor.RED + "You cannot afford this! You only have " + dp.getDp() + " DP, but need at least " + cost + "!");
        return;
    }
}
