package org.realcodingteam.plan9.inv.effects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.data.DonorPlayer;
import org.realcodingteam.plan9.inv.AbstractMenu;
import org.realcodingteam.plan9.inv.scripts.MenuEntry;
import org.realcodingteam.plan9.inv.scripts.ScriptManager;
import org.realcodingteam.plan9.util.Item;

/*
 * This class and subclasses of this class are used to hard-code the
 * effects a player may purchase for the server using the /donor menu.
 * Effects are split up into respective sub-menus, and respectively
 * subclasses.
 * 
 * Typically, each subclass will define some form of "from" to allow
 * the inventory class access to the correct effect for an itemstack
 * or material.
 */
public abstract class Effects {

    private final int cost;
    protected Runnable effect; //what will happen when this effect is run
    
    protected Effects(int cost) {
        this.cost = cost;
    }
    
    public final int getCost() {
        return cost;
    }
    
    private final static void announce(Player buyer, String display) {
        Bukkit.broadcastMessage("§e[DONOR] §r" + buyer.getDisplayName() + "§d purchased §e" + display + "§d for the server!");
    }
    
    //Run the effect and broadcast to the server what was bought
    private final void run(Player buyer, String display) {
        announce(buyer, display);
        if(effect != null) effect.run();
        buyer.closeInventory();
    }
    
    //Check if the player buying the effect has enough donor points (DP)
    //If they do, take the cost from their DP balance and then run the effect
    //Otherwise, notify them that they don't have enough DP to purchase the effect
    public final void buyAndRun(Player buyer, String display) {
        DonorPlayer dp = DonorPlayer.getDonorPlayer(buyer.getUniqueId());    
        int bal = dp.getDp() - cost;
        
        if (bal >= 0) {
            dp.setDp(bal);
            AbstractMenu.triggerRefresh();
            run(buyer, display);
            return;
        }
        
        buyer.sendMessage(ChatColor.RED + "You cannot afford this! You only have " + dp.getDp() + " DP, but need at least " + cost + "!");
        return;
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
