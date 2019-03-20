package org.realcodingteam.plan9.chat;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_13_R2.NBTTagCompound;

public final class ItemTag {
    
    protected static TextComponent toMessage(ItemStack item) {
        TextComponent base = new TextComponent();
        HoverEvent event = getEvent(item);
        base.setHoverEvent(event);
        
        ChatColor color = getColor(item);
        
        TextComponent br1 = textComp("[", color);
        br1.setHoverEvent(event);
        TextComponent br2 = textComp("]", color);
        br2.setHoverEvent(event);
        
        String display = getDisplayName(item);
        String amount = getAmount(item.getAmount());
        String format = String.format("%s%s", display, amount);

        base.addExtra(br1);
        base.addExtra(textComp(format, color));
        base.addExtra(br2);
        return base;
    }
    
    private static HoverEvent getEvent(ItemStack item) {
        net.minecraft.server.v1_13_R2.ItemStack mcIs = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = new NBTTagCompound();
        mcIs.save(tag);
        String json = tag.toString();
        
        return new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[] { new TextComponent(json) });
    }
    
    private static String getDisplayName(ItemStack item) {
        if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return proper(item.getType());
        return item.getItemMeta().getDisplayName();
    }
    
    private static String proper(Material material) {
        //converts any item type into uppercase words
        //DIAMOND_SWORD = Diamond Sword
        //ACACIA_FENCE_GATE = Acacia Fence Gate
        String[] name = material.name().split("_");
        for(int i = 0; i < name.length; i++) {
            name[i] = name[i].charAt(0) + name[i].substring(1).toLowerCase();
        }
        return String.join(" ", name);
    }
    
    private static String getAmount(int amount) {
        return amount > 1? " x" + amount : "";
    }
    
    private static ChatColor getColor(ItemStack is) {
        return is.getEnchantments().size() > 0? ChatColor.AQUA : ChatColor.WHITE;
    }
    
    protected static TextComponent textComp(String text, ChatColor col) {
        TextComponent comp = new TextComponent(text);
        comp.setColor(col);
        return comp;
    }
}
