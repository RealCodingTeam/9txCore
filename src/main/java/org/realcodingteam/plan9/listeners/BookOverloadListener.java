package org.realcodingteam.plan9.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.realcodingteam.plan9.commands.StaffChatCommand;

//Tries to prevent the written book chunk overload
public class BookOverloadListener implements Listener {
    
    private static Map<UUID, Status> players = new HashMap<>();
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSlot(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItem(event.getNewSlot());
        if(is == null || is.getType() != Material.WRITABLE_BOOK) return;
        
        players.put(player.getUniqueId(), Status.NOTHING);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRightClickBook(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getItem() == null || event.getItem().getType() != Material.WRITABLE_BOOK) return;
        
        Player player = event.getPlayer();
        players.put(player.getUniqueId(), Status.EDITING);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBookWrite(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        if(!players.containsKey(player.getUniqueId()) || players.get(player.getUniqueId()) == Status.NOTHING) {
            StaffChatCommand.sendStaffMessage("§4[BOOKS]", "§4Player §c" + player.getName() + "§4 signed a book without right clicking!!!");
            event.setCancelled(true);
            players.remove(player.getUniqueId());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBookMove(InventoryMoveItemEvent event) {
        ItemStack is = event.getItem();
        if(is.getType() == Material.WRITTEN_BOOK || is.getType() == Material.WRITABLE_BOOK) {
            event.setCancelled(true);
            return;
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBookClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;
        
        if (event.getClick() == ClickType.NUMBER_KEY) {
            ItemStack item = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
            if (item != null) {
                validateBook(item, (Player)event.getWhoClicked());
                return;
            }
        }
        
        validateBook(event.getCurrentItem(), (Player)event.getWhoClicked());
    }
    
    private void validateBook(ItemStack is, Player player) {
        if(is.getType() != Material.WRITTEN_BOOK && is.getType() != Material.WRITABLE_BOOK) return;
        BookMeta bm = (BookMeta)is.getItemMeta();
        List<String> data = bm.getPages();
        if(data.size() <= 10) return;
        data = data.subList(0, 9);        
        
        List<String> newdata = new ArrayList<>();
        for(String page : data) {
            String filtered = page.replaceAll("[^A-Za-z0-9 ]", "");
            newdata.add(filtered);
        }
        
        bm.setPages(newdata);
        StaffChatCommand.sendStaffMessage("§4[BOOKS]", "§eFiltered a book by §d" + player.getName() + "§e - Book had more than 10 pages.");
        is.setItemMeta(bm);
    }
    
    public static boolean shouldBlock(Player player) {
        if(!players.containsKey(player.getUniqueId()) || players.get(player.getUniqueId()) == Status.NOTHING) {
            StaffChatCommand.sendStaffMessage("§4[MC|BSign]", "§4Player §c" + player.getName() + "§4 signed a book without right clicking!!!");
            return true;
        }
        return false;
    }
    
    private static enum Status {
        NOTHING, EDITING
    }
    
}
