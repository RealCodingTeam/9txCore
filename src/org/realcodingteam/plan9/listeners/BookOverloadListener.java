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
    
    //Tracks what actions players are currently taking
    private static Map<UUID, Status> players = new HashMap<>();
    
    //When a player switches hotbar slot, reset them back to "nothing"
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSlot(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItem(event.getNewSlot());
        if(is == null || is.getType() != Material.WRITABLE_BOOK && is.getType() != Material.WRITTEN_BOOK) return;
        
        //Filter every single book to prevent the book dupe
        if(is.getType() == Material.WRITTEN_BOOK) {
            validateBook(is, player);
            return;
        }
        
        players.put(player.getUniqueId(), Status.NOTHING);
    }
    
    //Update the action the player is currently taking to "editing" if they have a writable book
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRightClickBook(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getItem() == null || event.getItem().getType() != Material.WRITABLE_BOOK) return;
        
        Player player = event.getPlayer();
        players.put(player.getUniqueId(), Status.EDITING);
    }
    
    //Disable the book signing if the player isn't currently "editing".
    //The logic behind this is that the hack that players use to create the books
    //doesn't actually open the book on the server side. The hack just sends the
    //packet that causes the writable book to turn into a written book with the
    //malicious text.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBookWrite(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        if(!players.containsKey(player.getUniqueId()) || players.get(player.getUniqueId()) == Status.NOTHING) {
            //Since this is a very simple check, instead of taking action, the plugin
            //will just notify staff chat of this so staff can take a look at the player.
            StaffChatCommand.sendStaffMessage("§4[BOOKS]", "§4Player §c" + player.getName() + "§4 signed a book without right clicking!!!");
            event.setCancelled(true);
            players.remove(player.getUniqueId());
        }
    }
    
    //Filter books when hoppers move them.
    //Prevents players from avoiding clicking
    //the book item so it doesn't get filtered 
    //by InventoryClickEvent.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBookMove(InventoryMoveItemEvent event) {
        ItemStack is = event.getItem();
        if(is.getType() == Material.WRITTEN_BOOK || is.getType() == Material.WRITABLE_BOOK) {
            event.setCancelled(true);
            return;
        }
    }
    
    //Validate a book and filter it of malicious text.
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
        if(event.getCursor() != null) validateBook(event.getCursor(), (Player)event.getWhoClicked());
    }
    
    //Filters books of malicious text.
    //This method is really anal about
    //what's "malicious", so for all
    //intents and purposes, writing
    //books is basically disabled.
    private void validateBook(ItemStack is, Player player) {
        if(is.getType() != Material.WRITTEN_BOOK && is.getType() != Material.WRITABLE_BOOK) return;
        BookMeta bm = (BookMeta)is.getItemMeta();
        List<String> data = bm.getPages();
        
        //Limit books to 9 pages (page 1 to 9)
        if(data.size() < 10) return;
        data = data.subList(0, 9);        
        
        //Remove all non latin alphanumeric characters from pages
        List<String> newdata = new ArrayList<>();
        for(String page : data) {
            String filtered = page.replaceAll("[^A-Za-z0-9 ]", "");
            newdata.add(filtered);
        }
        
        bm.setPages(newdata);
        is.setItemMeta(bm);
        //notify staff chat that a book was filtered
        //This is more of a warning because it's easy to
        //set this off.
        StaffChatCommand.sendStaffMessage("§6[BOOKS]", "§6(Info) §eFiltered a book by §d" + player.getName() + "§e - Book had more than 10 pages.");
    }
    
    //Return true if the player is not editing a book right now
    public static boolean shouldBlock(Player player) {
        if(!players.containsKey(player.getUniqueId()) || players.get(player.getUniqueId()) == Status.NOTHING) {
            StaffChatCommand.sendStaffMessage("§4[MC|BSign]", "§4Player §c" + player.getName() + "§4 signed a book without right clicking!!!");
            return true;
        }
        return false;
    }
    
    //Boolean enum determining the current action of a player
    //Nothing means not editing a book
    //Editing means the player right clicked a book to edit it.
    private static enum Status {
        NOTHING, EDITING
    }
    
}
