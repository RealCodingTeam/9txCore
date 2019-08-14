package org.realcodingteam.plan9.prank;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.realcodingteam.plan9.NtxPlugin;

import net.md_5.bungee.api.ChatColor;

public final class DontHugMeImScared implements Runnable, Listener {

    private static final ItemStack PUMPKIN;
    static {
        PUMPKIN = new ItemStack(Material.PUMPKIN, 1);
        PUMPKIN.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        PUMPKIN.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        
        ItemMeta im = PUMPKIN.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        im.setDisplayName(ChatColor.BLACK + "spooked");
        im.setLore(Arrays.asList("", ChatColor.GOLD + "The wearer of this hat is a scaredy-cat!", ChatColor.DARK_RED + "❤" + ChatColor.BLUE + " Danny"));
        PUMPKIN.setItemMeta(im);
    }
    
    protected DontHugMeImScared() {
        Bukkit.getPluginManager().registerEvents(this, NtxPlugin.instance());
    }
    
    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!Manager.isTarget(player)) continue;
            if(player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() != Material.AIR) continue;
            if(Math.random() < .6) continue;
            
            player.getInventory().setHelmet(PUMPKIN.clone());
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 2, 0), true);
            player.sendMessage(ChatColor.GOLD + "You've suddenly become spooked and decided to hide inside a pumpkin.");
        }
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ItemStack helm = event.getPlayer().getInventory().getHelmet();
        if(helm == null || helm.getType() != PUMPKIN.getType()) return;
        if(!helm.hasItemMeta() || !helm.getItemMeta().hasDisplayName()) return;
        
        String name = helm.getItemMeta().getDisplayName();
        if(!PUMPKIN.getItemMeta().getDisplayName().equalsIgnoreCase(name)) return;
        
        event.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
    }
    
}
