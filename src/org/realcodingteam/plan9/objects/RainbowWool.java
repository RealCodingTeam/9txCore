package org.realcodingteam.plan9.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.realcodingteam.plan9.NtxPlugin;

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
    
    public static final RainbowWool instance = new RainbowWool();
    
    private int index;
    private final ItemStack WOOL;
    
    public ItemStack getWool() {
        return WOOL;
    }
    
    private RainbowWool() {
        index = 0;
        
        WOOL = new ItemStack(WOOL_COLORS[index], 1);
        WOOL.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        
        ItemMeta im = WOOL.getItemMeta();
        im.setDisplayName("" + CHAT_COLORS[index] + ChatColor.ITALIC + "Multi-colored name");
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        WOOL.setItemMeta(im);
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(NtxPlugin.instance, this::next, 10L, 10L);
    }
    
    private void next() {
        index++;
        index %= WOOL_COLORS.length;
        
        WOOL.setType(WOOL_COLORS[index]);
        ItemMeta im = WOOL.getItemMeta();
        im.setDisplayName("" + CHAT_COLORS[index] + ChatColor.ITALIC + "Multi-colored name");
        WOOL.setItemMeta(im);
    }
}
