package org.realcodingteam.plan9.magic;

import java.lang.reflect.Method;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.NtxPlugin;

public final class Nms {
    
    private static boolean failed = false;
    
    private static Class<?> CRAFT_ITEM_STACK; //org.bukkit.craftbukkit.VERSION.inventory.CraftItemStack
    private static Class<?> NMS_ITEM_STACK; //org.bukkit.craftbukkit.VERSION.inventory.CraftItemStack
    private static Class<?> NBT_TAG_COMPOUND; //net.minecraft.server.VERSION.ItemStack
    private static Method AS_NMS; //net.minecraft.server.VERSION.inventory.CraftItemStack#asNMSCopy(ItemStack)
    private static Method SAVE; //net.minecraft.server.VERSION.inventory.CraftItemStack#save(NBTTagCompound)
    private static Method TO_STRING; //net.minecraft.server.VERSION.NBTTagCompound#toString()
    
    
    static {
        String ver = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        String nms_path = "net.minecraft.server." + ver + ".";
        String bukkit_path = "org.bukkit.craftbukkit." + ver + ".";
        
        try {
            CRAFT_ITEM_STACK = Class.forName(bukkit_path + "inventory.CraftItemStack");
            NMS_ITEM_STACK = Class.forName(nms_path + "ItemStack");
            NBT_TAG_COMPOUND = Class.forName(nms_path + "NBTTagCompound");
            
            AS_NMS = CRAFT_ITEM_STACK.getDeclaredMethod("asNMSCopy", ItemStack.class);
            AS_NMS.setAccessible(true);
            
            SAVE = NMS_ITEM_STACK.getDeclaredMethod("save", NBT_TAG_COMPOUND.newInstance().getClass());
            SAVE.setAccessible(true);
            
            TO_STRING = NBT_TAG_COMPOUND.getDeclaredMethod("toString");
            TO_STRING.setAccessible(true);
        } catch(Exception e) {
            e.printStackTrace();
            failed = true;
            NtxPlugin.getInstance().getLogger().warning("Failed to setup NMS reflection. Item tags in chat will not work.");
        }
    }
    
    public static Optional<String> toJson(ItemStack is) {
        if(failed) return Optional.empty();
        
        try {
            Object nmsItem = AS_NMS.invoke(is, is);
            Object nbtTag = NBT_TAG_COMPOUND.newInstance();
            SAVE.invoke(nmsItem, nbtTag);
            return Optional.of((String)TO_STRING.invoke(nbtTag));
        } catch(Exception e) {
            return Optional.empty();
        }
    }
}
