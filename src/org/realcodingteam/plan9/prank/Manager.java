package org.realcodingteam.plan9.prank;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.NtxPlugin;

public final class Manager {
    
    public static void setup() {
        Bukkit.getPluginManager().registerEvents(new JeredsListener(), NtxPlugin.instance());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(NtxPlugin.instance(), new DontHugMeImScared(), 0L, 20 * 20L);
    }
    
    public static boolean isTarget(Player player) {
        return player.getUniqueId().toString().equalsIgnoreCase("938f190b-6a34-42af-bb7a-e75e4c3e21ae");
    }
    
}
