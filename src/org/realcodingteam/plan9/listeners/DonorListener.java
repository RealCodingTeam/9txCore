package org.realcodingteam.plan9.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.data.DonorPlayer;
import org.realcodingteam.plan9.inv.AbstractMenu;
import org.realcodingteam.plan9.inv.MiscMenu;
import org.realcodingteam.plan9.inv.SlotsMenu;
import org.realcodingteam.plan9.inv.effects.ExpEffects;
import org.realcodingteam.plan9.inv.effects.OreEffects;

public class DonorListener implements Listener {
    
    //Handles when a player clicks in an AbstractMenu inventory
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if(holder instanceof AbstractMenu) {
            event.setCancelled(true);
            AbstractMenu menu = (AbstractMenu) holder;
            
            if(menu instanceof MiscMenu && SlotsMenu.handle(event)) {
                event.setCancelled(true);
                return;
            }
            
            //Filter out what kinds of click the AbstractMenu will see
            if(event.getClick().isKeyboardClick()
            || event.isShiftClick()
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
    
    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if(holder instanceof AbstractMenu) ((AbstractMenu)holder).close((Player)event.getPlayer());
    }

    //Double ore drops (See OreEffects)
    //Fortune does increase drops by a random amount
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMineOre(BlockBreakEvent event) {
        if(!OreEffects.isDoubleSmelt()) return;
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
    
    //Double experience drops (See ExpEffects)
    @EventHandler
    public void onExperienceGained(PlayerExpChangeEvent event) {
        if(ExpEffects.isDoubleExp()) event.setAmount(event.getAmount() * 2);
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
