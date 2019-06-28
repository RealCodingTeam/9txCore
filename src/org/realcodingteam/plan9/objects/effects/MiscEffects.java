package org.realcodingteam.plan9.objects.effects;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({"deprecation", "unused"}) //java is dumb and thinks all effects except DAY are unused
public class MiscEffects extends Effects {
    
    private static final Map<Material, MiscEffects> effects = new HashMap<>();
    
    private static final MiscEffects FEED = new MiscEffects(5, Material.COOKIE, MiscEffects::feedAll);
    private static final MiscEffects HEAL = new MiscEffects(15, Material.GOLDEN_APPLE, MiscEffects::healAll);
    private static final MiscEffects SUNNY = new MiscEffects(10, Material.BUCKET, MiscEffects::clearWeather);
    private static final MiscEffects STORMY = new MiscEffects(100, Material.WATER_BUCKET, MiscEffects::stormyWeather);
    private static final MiscEffects DAY = new MiscEffects(10, Material.SUNFLOWER, MiscEffects::dayTime);
    private static final MiscEffects NIGHT = new MiscEffects(100, Material.CLOCK, MiscEffects::nightTime);
    
    protected MiscEffects(int cost, Material mat, Runnable effect) {
        super(cost);
        
        this.effect = effect;
        effects.put(mat, this);
    }
    
    public static MiscEffects fromItem(ItemStack item) {
        return effects.getOrDefault(item.getType(), DAY);
    }
    
    

    private static void feedAll() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.setSaturation(20.0f);
            p.setFoodLevel(20);
        });
    }
    
    private static void healAll() {
        Bukkit.getOnlinePlayers().forEach(p -> p.setHealth(p.getMaxHealth()));
    }
    
    private static void clearWeather() {
        Bukkit.getWorld("world").setStorm(false);
    }
    
    private static void stormyWeather() {
        Bukkit.getWorld("world").setStorm(true);
        Bukkit.getWorld("world").setThundering(true);
    }
    
    private static void dayTime() {
        Bukkit.getWorld("world").setTime(1000);
    }
    
    private static void nightTime() {
        Bukkit.getWorld("world").setTime(13050);
    }
}
