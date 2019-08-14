package org.realcodingteam.plan9.inv.effects;

import java.time.Instant;

import org.bukkit.Bukkit;
import org.realcodingteam.plan9.NtxPlugin;

//See Effects
public final class ExpEffects extends Effects {

    public static final ExpEffects TIER_1 = new ExpEffects(15, 60_000L);
    public static final ExpEffects TIER_2 = new ExpEffects(30, 120_000L);
    public static final ExpEffects TIER_3 = new ExpEffects(50, 180_000L);
    
    private static int taskId = -1;
    private static long DOUBLE_EXP_TIME = Instant.now().toEpochMilli();
    
    protected ExpEffects(int cost, long duration) {
        super(cost);
        
        effect = () -> {
            if(DOUBLE_EXP_TIME < Instant.now().toEpochMilli()) {
                DOUBLE_EXP_TIME = Instant.now().toEpochMilli();
            }
            
            DOUBLE_EXP_TIME += duration;
            
            if(taskId != -1) Bukkit.getScheduler().cancelTask(taskId);
            taskId = Bukkit.getScheduler().runTaskLater(NtxPlugin.instance(), () -> {
                if(((DOUBLE_EXP_TIME - Instant.now().toEpochMilli()) / 1000) <= 3) {
                    Bukkit.broadcastMessage("§e[DONOR] §cDouble experience drops has expired.");
                    Bukkit.getScheduler().cancelTask(taskId);
                    taskId = -1;
                }
            }, ((DOUBLE_EXP_TIME - Instant.now().toEpochMilli()) / 1000) * 20).getTaskId();
        };
    }
    
    public static long getTimeLeft() {
        long delta = DOUBLE_EXP_TIME - Instant.now().toEpochMilli();
        if(delta < 0) return -1;
        return delta;
    }
    
    public static boolean isDoubleExp() {
        return DOUBLE_EXP_TIME > Instant.now().toEpochMilli();
    }
    
    public static ExpEffects fromAmount(int i) {
        switch(i) {
            case 2:  return TIER_2;
            case 3:  return TIER_3;
            default: return TIER_1;
        }
    }
}
