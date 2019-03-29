package org.realcodingteam.plan9.objects.effects;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

//See Effects
public class MiscEffects extends Effects {
    
    private static final Map<Material, MiscEffects> effects = new HashMap<>();
    
    public static final MiscEffects FEED = new MiscEffects(5, Material.COOKIE);
    public static final MiscEffects HEAL = new MiscEffects(15, Material.GOLDEN_APPLE);
    public static final MiscEffects SUNNY = new MiscEffects(10, Material.BUCKET);
    public static final MiscEffects STORMY = new MiscEffects(100, Material.WATER_BUCKET);
    public static final MiscEffects DAY = new MiscEffects(10, Material.SUNFLOWER);
    
    @SuppressWarnings("deprecation")
    protected MiscEffects(int cost, Material mat) {
        super(cost);
        
        //I changed back to if statements because
        //the switch statement bloated the compiled size.
        //This code compiles to about 5KB as is, but with the
        //switch statement, blew up to about 77KB. The entire
        //Material enum was copied into the class file.
        
        if(mat == Material.COOKIE) {
            effect = () -> {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.setSaturation(20.0f);
                    p.setFoodLevel(20);
                });
            };
        }
        else if(mat == Material.GOLDEN_APPLE) {
            effect = () -> Bukkit.getOnlinePlayers().forEach(p -> p.setHealth(p.getMaxHealth()));
        }
        else if(mat == Material.BUCKET) {
            effect = () -> Bukkit.getWorld("world").setStorm(false);
        } 
        else if(mat == Material.WATER_BUCKET) {
            effect = () -> {
                Bukkit.getWorld("world").setStorm(true);
                Bukkit.getWorld("world").setThundering(true);
            };
        } else {
            effect = () -> Bukkit.getWorld("world").setTime(1000);
        }
        
        effects.put(mat, this);
    }
    
    public static MiscEffects fromItem(ItemStack item) {
        return effects.getOrDefault(item.getType(), DAY);
    }

}
