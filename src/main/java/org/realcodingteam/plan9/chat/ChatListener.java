package org.realcodingteam.plan9.chat;

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

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatListener implements Listener {

    private Essentials ess;
    
    public ChatListener() {
        ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if(!event.getPlayer().hasPermission("ntx.item")) return;
        Player player = event.getPlayer();
		User user = ess.getUser(player);
        if(user.isMuted() || user.isHidden()) {
			event.setCancelled(true);
			return;
		}
        
        boolean foundItem = false;
        List<Integer> items = new ArrayList<>();
        TextComponent base = new TextComponent();
        base.addExtra(ItemTag.textComp("* ", ChatColor.DARK_PURPLE));
        base.addExtra(player.getDisplayName());
        base.addExtra(" ");
        
        
        char[] msg = event.getMessage().toCharArray();
        for(int i = 0; i < msg.length; i++) {
            char c = msg[i];
            
            if(c != '<' || msg.length - 2 <= i) {
                base.addExtra(c + "");
                continue;
            }
            
            //<1> <2> <3> <4> <5> <6> <7> <8> <9>
            if(msg[i + 2] != '>') {
                base.addExtra(c + "");
                continue;
            }
            
            ItemStack item;
            int slot = 0;
            
            switch(msg[i + 1]) {
                case 'h': //Helmet slot
                case 'H':
                    item = player.getInventory().getHelmet();
                    slot = 10;
                    break;
                case 'c': //Chestplate slot
                case 'C':
                    item = player.getInventory().getChestplate();
                    slot = 11;
                    break;
                case 'l': //Leggings slot
                case 'L':
                    item = player.getInventory().getLeggings();
                    slot = 12;
                    break;
                case 'b': //Boots slot
                case 'B':
                    item = player.getInventory().getBoots();
                    slot = 13;
                    break;
                case 'o': //Offhand
                case 'O':
                    item = player.getInventory().getItemInOffHand();
                    slot = 14;
                    break;
                case 'i':
                case 'I':
                    item = player.getInventory().getItemInMainHand();
                    slot = player.getInventory().getHeldItemSlot();
                    break;
                default:  //Hotbar index
                    //Character.isDigit works with numbers other than
                    //ASCII '0' to '9'.
                    if(msg[i + 1] < '1' || msg[i + 1] > '9') {
                        base.addExtra(c + "");
                        continue;
                    }
                    slot = msg[i + 1] - 48 - 1; //Ascii '0'-'9' minus 48 = number 1-9, - 1 for 0 based indexing.
                    item = player.getInventory().getItem(slot);
                    break;
            }
            
            if(item == null || item.getType() == Material.AIR || items.contains(slot)) {
                base.addExtra(c + "");
                continue;
            }
            
            base.addExtra(ItemTag.toMessage(item));
            i += 2;
            items.add(slot);
            foundItem = true;
        }
        
        if(!foundItem) return;
        event.setCancelled(true);
        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(base));
    }
}
