package org.realcodingteam.plan9.objects.effects;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.NtxPlugin;

//See Effects
public final class OreEffects extends Effects {
    
    private static final Map<Material, Material> ORE_TO_RESOURCE;
    static {
        ORE_TO_RESOURCE = new HashMap<>();
        
        ORE_TO_RESOURCE.put(Material.COAL_ORE, Material.COAL);
        ORE_TO_RESOURCE.put(Material.IRON_ORE, Material.IRON_INGOT);
        ORE_TO_RESOURCE.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        ORE_TO_RESOURCE.put(Material.LAPIS_ORE, Material.LAPIS_LAZULI);
        ORE_TO_RESOURCE.put(Material.REDSTONE_ORE, Material.REDSTONE);
        ORE_TO_RESOURCE.put(Material.DIAMOND_ORE, Material.DIAMOND);
        ORE_TO_RESOURCE.put(Material.EMERALD_ORE, Material.EMERALD);
        ORE_TO_RESOURCE.put(Material.NETHER_QUARTZ_ORE, Material.QUARTZ);
    }
    
    private static int taskId = -1;
    private static long DOUBLE_ORE_TIME = Instant.now().toEpochMilli();
    
    public static final OreEffects TIER_1 = new OreEffects(50, 60_000L);
    public static final OreEffects TIER_2 = new OreEffects(100, 120_000L);
    public static final OreEffects TIER_3 = new OreEffects(175, 240_000L);
    
    protected OreEffects(int cost, long duration) {
        super(cost);
        
        effect = () -> {
            if(DOUBLE_ORE_TIME < Instant.now().toEpochMilli()) {
                DOUBLE_ORE_TIME = Instant.now().toEpochMilli();
            }
            
            DOUBLE_ORE_TIME += duration;
            
            if(taskId != -1) Bukkit.getScheduler().cancelTask(taskId);
            taskId = Bukkit.getScheduler().runTaskLater(NtxPlugin.getInstance(), () -> {
                if(((DOUBLE_ORE_TIME - Instant.now().toEpochMilli()) / 1000) <= 3) {
                    Bukkit.broadcastMessage("§e[DONOR] §cDouble ore drops have expired.");
                    Bukkit.getScheduler().cancelTask(taskId);
                    taskId = -1;
                }
            }, ((DOUBLE_ORE_TIME - Instant.now().toEpochMilli()) / 1000) * 20 + 20).getTaskId();
        };
    }
    
    public static boolean isDoubleSmelt() {
        return DOUBLE_ORE_TIME > Instant.now().toEpochMilli();
    }
    
    public static ItemStack smelt(ItemStack in, boolean fortune) {
        in.setType(ORE_TO_RESOURCE.getOrDefault(in.getType(), in.getType()));
        
        int amount = in.getAmount() * 2;
        if(fortune) amount = (int) Math.floor(amount * (Math.random() + 2));
        in.setAmount(amount);
        
        return in;
    }
}
