package org.realcodingteam.plan9.objects.effects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MiscEffects extends Effects {

    public static final MiscEffects FEED = new MiscEffects(5, Material.COOKIE);
    public static final MiscEffects HEAL = new MiscEffects(15, Material.GOLDEN_APPLE);
    public static final MiscEffects SUNNY = new MiscEffects(10, Material.BUCKET);
    public static final MiscEffects STORMY = new MiscEffects(100, Material.WATER_BUCKET);
    public static final MiscEffects DAY = new MiscEffects(10, Material.SUNFLOWER);
    
    @SuppressWarnings("deprecation")
    protected MiscEffects(int cost, Material mat) {
        super(cost);
        
        switch(mat) {
            case COOKIE:
                effect = () -> {
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.setSaturation(20.0f);
                        p.setFoodLevel(20);
                    });
                };
                break;
            case GOLDEN_APPLE:
                effect = () -> Bukkit.getOnlinePlayers().forEach(p -> p.setHealth(p.getMaxHealth()));
                break;
            case BUCKET:
                effect = () -> Bukkit.getWorld("world").setStorm(false);
                break;
            case WATER_BUCKET:
                effect = () -> {
                    Bukkit.getWorld("world").setStorm(true);
                    Bukkit.getWorld("world").setThundering(true);
                };
                break;
            case SUNFLOWER:
            default:
                effect = () -> Bukkit.getWorld("world").setTime(1000);
        }
    }
    
    public static MiscEffects fromItem(ItemStack item) {
        switch(item.getType()) {
            case COOKIE: return FEED;
            case GOLDEN_APPLE: return HEAL;
            case BUCKET: return SUNNY;
            case WATER_BUCKET: return STORMY;
            case SUNFLOWER: 
            default: return DAY;
        }
    }

}
