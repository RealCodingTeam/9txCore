package org.realcodingteam.plan9.chat;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public final class ChatListener implements Listener {

    private Essentials ess;
    
    public ChatListener() {
        ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        //Intial checks to make sure the player should be able to use this
        if(!event.getPlayer().hasPermission("ntx.item")) return;
        Player player = event.getPlayer();
		User user = ess.getUser(player);
        if(user.isMuted() || user.isHidden()) return;
        
        List<Integer> items = new ArrayList<>(); //Prevent players from sending the same item more than once
        TextComponent base = new TextComponent();
        base.addExtra(ItemTag.textComp("* ", ChatColor.DARK_PURPLE)); //Styled to look like /me
        base.addExtra(player.getDisplayName());
        base.addExtra(" ");
        
        char[] msg = event.getMessage().toCharArray();
        for(int i = 0; i < msg.length; i++) {
            char c = msg[i];
            
            //The item tag takes the form of "<" + slot + ">".
            //<1> <2> <3> <4> <5> <6> <7> <8> <9> <h> <c> <l> <b> <o> <i>
            if(c != '<' || msg.length - 2 <= i || msg[i + 2] != '>') {
                base.addExtra(c + "");
                continue;
            }
            
            SimpleEntry<Integer, ItemStack> entry = getItemForChar(player, msg[i + 1]);
            int slot = entry.getKey();
            ItemStack item = entry.getValue();
            
            //If the item tag is invalid, treat the character as not part of the tag and add it like normal.
            if(slot < 0 || item == null || item.getType() == Material.AIR || items.contains(slot)) {
                base.addExtra(c + "");
                continue;
            }
            
            //Convert the item to a TextComponent with hover and append it
            base.addExtra(ItemTag.toMessage(item));
            i += 2;
            items.add(slot); //Save the current slot to the list so it isn't added again later
        }
        
        if(items.isEmpty()) return; //If no item tags were found, return
        event.setCancelled(true);
        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(base));
    }
    
    //Converts a char into <Integer, ItemStack> pair.
    private static SimpleEntry<Integer, ItemStack> getItemForChar(Player player, char in) {
        int slot = -1; //-1 = invalid slot
        ItemStack item = null; //null = invalid item
        
        PlayerInventory inv = player.getInventory();
        
        switch(Character.toLowerCase(in)) {
            case 'h': //Helmet slot
                item = inv.getHelmet();
                slot = 10;
                break;
            case 'c': //Chestplate slot
                item = inv.getChestplate();
                slot = 11;
                break;
            case 'l': //Leggings slot
                item = inv.getLeggings();
                slot = 12;
                break;
            case 'b': //Boots slot
                item = inv.getBoots();
                slot = 13;
                break;
            case 'o': //Offhand
                item = inv.getItemInOffHand();
                slot = 14;
                break;
            case 'i':
                item = inv.getItemInMainHand();
                slot = inv.getHeldItemSlot();
                break;
            default:  //Hotbar index
                //Character.isDigit works with numbers other than
                //ASCII '0' to '9'.
                if(in < '1' || in > '9') {
                    //We don't care about anything else, so return an invalid pair.
                    return new SimpleEntry<>(-1, null);
                }
                
                slot = in - 48 - 1; //Ascii '0'-'9' minus 48 = number 1-9, - 1 for 0 based indexing.
                item = player.getInventory().getItem(slot);
                break;
        }
        
        return new SimpleEntry<>(slot, item);
    }
}
