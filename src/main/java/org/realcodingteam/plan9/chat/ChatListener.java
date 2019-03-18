package org.realcodingteam.plan9.chat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.Essentials;

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
        if(ess.getUser(event.getPlayer()).isMuted()) return;
        
        boolean foundItem = false;
        boolean[] items = new boolean[9];
        TextComponent base = new TextComponent();
        base.addExtra(ItemTag.textComp("* ", ChatColor.DARK_PURPLE));
        base.addExtra(event.getPlayer().getDisplayName());
        base.addExtra(" ");
        
        
        char[] msg = event.getMessage().toCharArray();
        for(int i = 0; i < msg.length; i++) {
            char c = msg[i];
            
            if(c != '[' || msg.length - 3 <= i) {
                base.addExtra(c + "");
                continue;
            }
            
            //[i1] [i2] [i3] [i4] [i5] [i6] [i7] [i8] [i9]
            if(msg[i + 1] != 'i' || msg[i + 3] != ']' || !Character.isDigit(msg[i + 2])) {
                base.addExtra(c + "");
                continue;
            }
            
            int slot = msg[i + 2] - 48; //Ascii '0'-'9' minus 48 = number 1-9
            ItemStack item = event.getPlayer().getInventory().getItem(slot - 1);
            if(item == null || item.getType() == Material.AIR || items[slot - 1]) {
                base.addExtra(c + "");
                continue;
            }
            
            items[slot - 1] = true;
            base.addExtra(ItemTag.toMessage(item));
            i += 3;
            foundItem = true;
        }
        
        if(!foundItem) return;
        event.setCancelled(true);
        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(base));
    }
}
