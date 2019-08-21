package org.realcodingteam.plan9.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.realcodingteam.plan9.data.DonorPlayer;
import org.realcodingteam.plan9.inv.AbstractMenu;
import org.realcodingteam.plan9.inv.RootMenu;
import org.realcodingteam.plan9.inv.SlotsMenu;

public class DonorListener implements Listener {
    
    //Handles when a player clicks in an AbstractMenu inventory
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if(holder instanceof AbstractMenu) {
            event.setCancelled(true);
            AbstractMenu menu = (AbstractMenu) holder;
            
            if(menu instanceof RootMenu && SlotsMenu.handle(event)) {
                event.setCancelled(true);
                return;
            }
            
            //Filter out what kinds of click the AbstractMenu will see
            if(event.getClick() != ClickType.LEFT || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            
            //Hardcoded back button
            if(event.getCurrentItem().getType() == Material.GOLD_INGOT) {
                menu.openParent();
                return;
            }
            
            menu.onInventoryClick(event.getCurrentItem());
        }
    }
    
    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if(holder instanceof AbstractMenu) ((AbstractMenu)holder).close((Player)event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        //Make all players have no attack cooldown
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(
                new AttributeModifier("generic.attackSpeed", 99999999.0D, AttributeModifier.Operation.ADD_NUMBER));

        //Grant one off donor bonus for donating for a rank
        if (player.hasPermission("ntx.donor")) {
            DonorPlayer dp = DonorPlayer.getDonorPlayer(player.getUniqueId());

            if (!dp.hasReceivedDP()) {
                int recieved = 0;

                if (player.hasPermission("ntx.emperor")) {
                    recieved = 400;
                } else if (player.hasPermission("ntx.king")) {
                    recieved = 250;
                } else if (player.hasPermission("ntx.general")) {
                    recieved = 150;
                } else if (player.hasPermission("ntx.soldier")) {
                    recieved = 75;
                }

                dp.setDp(recieved);
                dp.setHasReceivedDP(true);
                player.sendMessage(ChatColor.GREEN + "You have recieved your donor points of " + ChatColor.GOLD + recieved);
            }

            String nick = "";
            if(dp.getNick().length() > 1) nick = dp.getNick();
            else nick = ChatColor.getByChar(dp.getNick()) + player.getName() + ChatColor.RESET;
            
            if(!ChatColor.stripColor(nick).equalsIgnoreCase(player.getName())) {
                nick = player.getName();
                player.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "[DONOR] " + ChatColor.YELLOW + "Your nickname has been reset because you changed your name.");
                dp.setNick("" + ChatColor.WHITE.getChar());
            }
            
            player.setDisplayName(nick);
            player.setPlayerListName(nick);
            dp.setLastLogin(System.currentTimeMillis());
        }
    }
    
    //Save donor players when they quit
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        DonorPlayer.saveDonor(DonorPlayer.getDonorPlayer(event.getPlayer().getUniqueId()));
        
        
    }

    
}
