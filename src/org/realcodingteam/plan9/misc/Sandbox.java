package org.realcodingteam.plan9.misc;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public final class Sandbox {
    
    private static final Listener listener;
    
    static {
        listener = new SandboxListener();
    }
    
    public static void fillWithSand() {
        if(listener != null) throw new NullPointerException("no sand to fill with!");
    }
    
    public static boolean isACoolKid(Player player) {
        switch(player.getUniqueId().toString()) {
            case "cfa295f9-e01f-44f5-93a2-2e4271d7e015":
            case "3fe04d09-f236-4e18-b29d-0873647d3312": return true;
            default: return false;
        }
    }
    
}
