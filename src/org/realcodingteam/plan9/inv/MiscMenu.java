package org.realcodingteam.plan9.inv;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.objects.effects.MiscEffects;

public class MiscMenu extends AbstractMenu {
    
    public MiscMenu(Player viewer) {
        super(9, ChatColor.DARK_PURPLE + "Donor - Misc", viewer, true);
        
        build();
        open(viewer);
    }

    @Override
    protected void build() {
        inv.setItem(0, makeItem(Material.COOKIE,              "§5Feed Everyone - 5 DP",             "§aSaturation"));
        inv.setItem(2, makeItem(Material.GOLDEN_APPLE,        "§5Heal Everyone - 15 DP",             "§aFull health"));
        inv.setItem(4, getTimeItem());
        inv.setItem(6, getWeatherItem());
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        MiscEffects.fromItem(item).buyAndRun(viewer, getLore(item));
    }
    
    private static ItemStack getTimeItem() {
        if(Bukkit.getWorld("world").getTime() >= 13050 || Bukkit.getWorld("world").getTime() < 1000) {
            return makeItem(Material.SUNFLOWER, "§5Set Time To Day - 10 DP", "§aDay");
        }
        
        return makeItem(Material.CLOCK, "§5Set Time To Night - 100 DP", "§aNight");
    }
    
    private static ItemStack getWeatherItem() {
        if(Bukkit.getWorld("world").hasStorm()) return makeItem(Material.BUCKET, "§5Set Weather To Clear - 10 DP", "§aClear weather");
        return makeItem(Material.WATER_BUCKET, "§5Set Weather To Stormy - 100 DP", "§aThunderstorm");
    }
}
