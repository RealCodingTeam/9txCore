package org.realcodingteam.plan9.objects.effects;

import java.time.Instant;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.NtxPlugin;

//See Effects
public class OreEffects extends Effects {
    
    private static int taskId = -1;
    private static long TRIPLE_ORE_TIME = Instant.now().toEpochMilli();
    
    public static final OreEffects TIER_1 = new OreEffects(50, 60_000L);
    public static final OreEffects TIER_2 = new OreEffects(100, 120_000L);
    public static final OreEffects TIER_3 = new OreEffects(200, 240_000L);
    
    protected OreEffects(int cost, long duration) {
        super(cost);
        
        effect = () -> {
            if(TRIPLE_ORE_TIME < Instant.now().toEpochMilli()) {
                TRIPLE_ORE_TIME = Instant.now().toEpochMilli();
            }
            
            TRIPLE_ORE_TIME += duration;
            
            if(taskId != -1) Bukkit.getScheduler().cancelTask(taskId);
            taskId = Bukkit.getScheduler().runTaskLater(NtxPlugin.instance, () -> {
                if(((TRIPLE_ORE_TIME - Instant.now().toEpochMilli()) / 1000) <= 3) {
                    Bukkit.broadcastMessage("§e[DONOR] §cTriple ore drops have expired.");
                    Bukkit.getScheduler().cancelTask(taskId);
                    taskId = -1;
                }
            }, ((TRIPLE_ORE_TIME - Instant.now().toEpochMilli()) / 1000) * 20 + 20).getTaskId();
        };
    }
    
    public static boolean isTripleSmelt() {
        return TRIPLE_ORE_TIME > Instant.now().toEpochMilli();
    }
    
    public static ItemStack smelt(ItemStack in, boolean fortune) {
        switch (in.getType()) {
            case DIAMOND_ORE:
                in.setType(Material.DIAMOND);
                break;
            case EMERALD_ORE:
                in.setType(Material.EMERALD);
                break;
            case GOLD_ORE:
                in.setType(Material.GOLD_INGOT);
                break;
            case IRON_ORE:
                in.setType(Material.IRON_INGOT);
                break;
            case LAPIS_ORE:
                in.setType(Material.LAPIS_LAZULI);
                break;
            case REDSTONE_ORE:
                in.setType(Material.REDSTONE);
                break;
            case COAL_ORE:
                in.setType(Material.COAL);
                break;
            case NETHER_QUARTZ_ORE:
                in.setType(Material.QUARTZ);
                break;
            default:
                break;
        }
        
        int amount = in.getAmount() * 3;
        if(fortune) amount = (int) Math.floor(amount * 1.8d);
        in.setAmount(amount);
        
        return in;
    }
}
