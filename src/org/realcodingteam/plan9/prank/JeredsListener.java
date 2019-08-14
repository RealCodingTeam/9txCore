package org.realcodingteam.plan9.prank;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class JeredsListener implements Listener {

    @EventHandler
    public void onJesterSpeak(AsyncPlayerChatEvent event) {
        if(Math.random() > 0.02f) return;
        
        String msg = ChatColor.stripColor(event.getMessage());
        StringBuilder sb = new StringBuilder();
        for(char c : msg.toCharArray()) {
            if(Math.random() >= .6f) sb.append(random().toString());
            
            if(Math.random() >= .6f) {
                sb.append(Character.toUpperCase(c));
                continue;
            }
            
            sb.append(c);
        }
        
        event.setMessage(sb.toString());
    }
    
    private ChatColor random() {
        ChatColor choice = ChatColor.values()[(int) Math.floor(Math.random() * ChatColor.values().length)];
        if(choice == ChatColor.RESET) choice = ChatColor.MAGIC;
        return choice;
    }
    
}
