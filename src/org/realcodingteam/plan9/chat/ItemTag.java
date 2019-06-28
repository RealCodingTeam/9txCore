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
    
    //Converts an input ItemStack into a TextComponent with hover event
    protected static TextComponent toMessage(ItemStack item) {
        TextComponent base = new TextComponent();
        HoverEvent event = getEvent(item); //The hover event for the TextComponent
        base.setHoverEvent(event);
        
        //Returns aqua or white depending on if the item is enchanted
        ChatColor color = getColor(item);
        
        //"[Item name]"
        //The brackets are done this way to ensure they maintain 
        //proper formatting, for example if the item's display name
        //has color, doing brackets without setting their color would
        //make the second bracket a different color.
        TextComponent br1 = textComp("[", color);
        br1.setHoverEvent(event);
        TextComponent br2 = textComp("]", color);
        br2.setHoverEvent(event);
        
        String display = getDisplayName(item); //What to display between the brackets
        String amount = getAmount(item.getAmount()); //Either "" or " xAmount", eg " x3"
        String format = String.format("%s%s", display, amount);

        base.addExtra(br1); //left bracket
        base.addExtra(textComp(format, color)); //item display
        base.addExtra(br2); //right bracket
        return base;
    }
    
    //Takes the item and creates the SHOW_ITEM hover event for it
    private static HoverEvent getEvent(ItemStack item) {
        net.minecraft.server.v1_13_R2.ItemStack mcIs = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = new NBTTagCompound();
        mcIs.save(tag); //Converts the item to JSON
        String json = tag.toString();
        
        return new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[] { new TextComponent(json) });
    }
    
    //Will either return the item's custom name (anvil name) or the
    //properly formatted material name if the item has no custom name
    private static String getDisplayName(ItemStack item) {
        if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return proper(item.getType());
        return item.getItemMeta().getDisplayName();
    }
    
    //converts any item type into uppercase words
    //DIAMOND_SWORD = Diamond Sword
    //ACACIA_FENCE_GATE = Acacia Fence Gate
    private static String proper(Material material) {
        String[] name = material.name().split("_");
        for(int i = 0; i < name.length; i++) {
            name[i] = name[i].charAt(0) + name[i].substring(1).toLowerCase();
        }
        return String.join(" ", name);
    }
    
    private static String getAmount(int amount) {
        return amount > 1? 
                (" x" + amount) //amount > 1 
              : ("");           //amount <= 1
    }
    
    private static ChatColor getColor(ItemStack is) {
        return is.getEnchantments().size() > 0? 
                    ChatColor.AQUA   //has 1 or more enchantments 
                  : ChatColor.WHITE; //has no enchantments
    }
    
    //helper method to create a TextComponent with color
    protected static TextComponent textComp(String text, ChatColor col) {
        TextComponent comp = new TextComponent(text);
        comp.setColor(col);
        return comp;
    }
}
