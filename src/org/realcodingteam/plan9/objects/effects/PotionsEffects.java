package org.realcodingteam.plan9.objects.effects;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

//See Effects
public final class PotionsEffects extends Effects {
    
    private static final PotionsEffects[] SPEED = { 
            new PotionsEffects(15, PotionEffectType.SPEED, 120 * 20, 2),
            new PotionsEffects(30, PotionEffectType.SPEED, 240 * 20, 2),
            new PotionsEffects(50, PotionEffectType.SPEED, 360 * 20, 2),
    };
    
    private static final PotionsEffects[] INVIS = {
        new PotionsEffects(15, PotionEffectType.INVISIBILITY, 120 * 20, 0),
        new PotionsEffects(30, PotionEffectType.INVISIBILITY, 240 * 20, 0),
        new PotionsEffects(50, PotionEffectType.INVISIBILITY, 360 * 20, 0),
    };
    
    private static final PotionsEffects[] HASTE = {
        new PotionsEffects(50, PotionEffectType.FAST_DIGGING, 120 * 20, 1),
        new PotionsEffects(100, PotionEffectType.FAST_DIGGING, 240 * 20, 1),
        new PotionsEffects(150, PotionEffectType.FAST_DIGGING, 360 * 20, 1),
    };
    
    private static final PotionsEffects[] GLOWING = {
        new PotionsEffects(15, PotionEffectType.GLOWING, 120 * 20, 0),
        new PotionsEffects(30, PotionEffectType.GLOWING, 240 * 20, 0),
        new PotionsEffects(60, PotionEffectType.GLOWING, 360 * 20, 0),
    };
    
    private static final PotionsEffects[] REGEN = {
        new PotionsEffects(30,  PotionEffectType.REGENERATION, 30 * 20, 1),
        new PotionsEffects(60,  PotionEffectType.REGENERATION, 60 * 20, 1),
        new PotionsEffects(120, PotionEffectType.REGENERATION, 120 * 20, 1),
    };
    
    private static final PotionsEffects GRACE = new PotionsEffects(50, PotionEffectType.DOLPHINS_GRACE, 120 * 20, 0);
    private static final PotionsEffects FIRE  = new PotionsEffects(30, PotionEffectType.FIRE_RESISTANCE, 240 * 20, 0);
    private static final PotionsEffects CONDUIT = new PotionsEffects(100, PotionEffectType.CONDUIT_POWER, 240 * 20, 0);
    private static final PotionsEffects SLOW_FALL = new PotionsEffects(30, PotionEffectType.SLOW_FALLING, 180 * 20, 0);

    protected PotionsEffects(int cost, PotionEffectType type, int duration, int amp) {
        super(cost);
        
        effect = () -> Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(type, duration, amp), true));
    }
    
    private static final Map<Material, PotionsEffects[]> effects;
    static {
        effects = new HashMap<>();
        
        effects.put(Material.SUGAR, SPEED);
        effects.put(Material.GOLDEN_CARROT, INVIS);
        effects.put(Material.GOLDEN_PICKAXE, HASTE);
        effects.put(Material.GLOWSTONE, GLOWING);
        effects.put(Material.GHAST_TEAR, REGEN);
        effects.put(Material.HEART_OF_THE_SEA, new PotionsEffects[] { GRACE });
        effects.put(Material.MAGMA_CREAM, new PotionsEffects[] { FIRE});
        effects.put(Material.CONDUIT, new PotionsEffects[] { CONDUIT });
        effects.put(Material.FEATHER, new PotionsEffects[] { SLOW_FALL });
        
    }
    
    public static PotionsEffects fromItem(ItemStack item) {
        int index = item.getAmount() - 1;
        if(index < 0) index = 0;
        if(index > 2) index = 2;
        
        PotionsEffects[] arr = effects.getOrDefault(item.getType(), SPEED);
        if(index >= arr.length) index = 0;
        return arr[index];
    }
}