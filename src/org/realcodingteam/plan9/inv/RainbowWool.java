package org.realcodingteam.plan9.inv;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.NtxPlugin;
import org.realcodingteam.plan9.util.Item;

import net.md_5.bungee.api.ChatColor;

public final class RainbowWool {

    private static final Material[] WOOL_COLORS = {
            Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL,
            Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL,
            Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL,
            Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BROWN_WOOL,
            Material.GREEN_WOOL, Material.RED_WOOL, Material.BLACK_WOOL
    };
    
    private static final ChatColor[] CHAT_COLORS = {
            ChatColor.WHITE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
            ChatColor.BLUE, ChatColor.YELLOW, ChatColor.GREEN,
            ChatColor.LIGHT_PURPLE, ChatColor.DARK_GRAY, ChatColor.GRAY,
            ChatColor.DARK_AQUA, ChatColor.DARK_PURPLE, ChatColor.GOLD,
            ChatColor.DARK_GREEN, ChatColor.DARK_RED, ChatColor.BLACK
    };
    
    private static final RainbowWool instance = new RainbowWool();
    private int index;
    private ItemStack WOOL;
    
    public static ItemStack getWool() {
        return instance.WOOL;
    }
    
    private RainbowWool() {
        index = 0;
        WOOL = Item.makeItem(WOOL_COLORS[index], 1, "" + CHAT_COLORS[index] + ChatColor.ITALIC + "Multi-colored name");
        WOOL = Item.addEnchantGlow(WOOL);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(NtxPlugin.instance(), this::next, 10L, 10L);
    }
    
    private void next() {
        index++;
        index %= WOOL_COLORS.length;
        
        WOOL.setType(WOOL_COLORS[index]);
        WOOL = Item.setDisplayName(WOOL, "" + CHAT_COLORS[index] + ChatColor.ITALIC + "Multi-colored name");
    }
}
