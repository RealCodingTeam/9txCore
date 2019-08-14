package org.realcodingteam.plan9.util;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class Item {
    
    public static ItemStack makeItem(Material mat, String name, String... lore) {
        return makeItem(mat, 1, name, lore);
    }
    
    public static ItemStack makeItem(Material mat, int amount, String name, String... lore) {
        ItemStack is = new ItemStack(mat, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if(lore != null && lore.length > 0) im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return is;
    }
    
    public static String getLore(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        if(im.hasLore()) return im.getLore().get(0);
        return ChatColor.RED + "###ERROR###: " + im.getDisplayName();
    }
    
    public static ItemStack setDisplayName(ItemStack item, String name) {
        ItemStack clone = item.clone();
        ItemMeta im = clone.getItemMeta();
        im.setDisplayName(name);
        clone.setItemMeta(im);
        return clone;
    }
    
    public static ItemStack addEnchantGlow(ItemStack item) {
        ItemStack clone = item.clone();
        
        ItemMeta im = clone.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        clone.setItemMeta(im);
        clone.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        
        return clone;
    }
}
