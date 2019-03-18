package org.realcodingteam.plan9.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.inv.AbstractMenu;
import org.realcodingteam.plan9.objects.DonorPlayer;
import org.realcodingteam.plan9.objects.effects.OreEffects;

public class DonorListener implements Listener {
    
    //Handles when a player clicks in an AbstractMenu inventory
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if(holder instanceof AbstractMenu) {
            event.setCancelled(true);
            AbstractMenu menu = (AbstractMenu) holder;
            
            //Filter out what kinds of click the AbstractMenu will see
            if(event.getClick().isKeyboardClick() 
            || event.getCurrentItem() == null 
            || event.getCurrentItem().getType() == Material.AIR
            || !event.getClickedInventory().equals(menu.getInventory())) 
            {
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

    //Triple ore drops (See OreEffects)
    @EventHandler
    public void onMineOre(BlockBreakEvent event) {
        if(!OreEffects.isTripleSmelt()) return;
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        
        ItemStack pick = event.getPlayer().getInventory().getItemInMainHand();
        boolean fortune = pick != null && pick.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
        
        if (event.getBlock().getType().name().contains("ORE")) {
            for (ItemStack is : event.getBlock().getDrops()) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), OreEffects.smelt(is, fortune));
            }

            event.setDropItems(false);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        //Make all players have no attack cooldown
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(
                new AttributeModifier("generic.attackSpeed", 99999999.0D, AttributeModifier.Operation.ADD_NUMBER));

        //Grant one off donor bonus for donating for a rank
        if (player.hasPermission("ntx.donor")) {
            DonorPlayer dp = DonorPlayer.loadDonor(player.getUniqueId());

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

            player.setDisplayName(ChatColor.getByChar(dp.getNick()) + player.getName() + ChatColor.RESET);
            player.setPlayerListName(ChatColor.getByChar(dp.getNick()) + player.getName() + ChatColor.RESET);
            dp.setLastLogin(System.currentTimeMillis());
        }
    }
    
    //Save donor players when they quit
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        DonorPlayer.saveDonor(DonorPlayer.getDonorPlayer(event.getPlayer().getUniqueId()));
    }

    
}
