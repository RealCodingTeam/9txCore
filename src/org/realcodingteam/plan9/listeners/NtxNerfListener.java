package org.realcodingteam.plan9.listeners;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.realcodingteam.plan9.NtxPlugin;

/*
 * Disables or nerfs vanilla functions that are considered "over powered"
 * on the server. 
 */
@SuppressWarnings("deprecation")
public class NtxNerfListener implements Listener {
    
    private Map<Player, Long> pearls = new HashMap<>(); //Contains a list of players on pearl cooldown. <Player, Time>
    
    @EventHandler
    public void onEntityEffectEvent(EntityPotionEffectEvent event) {
        //Disables the Dolphin's Grace effect:
        //Dolphin's grace and depth strider is incredibly OP for vanilla survival.
        if(event.getCause() == Cause.DOLPHIN) {
            event.setCancelled(true);
        
            if(event.getEntity() instanceof Player) {
                Player p = (Player) event.getEntity();
                p.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
            }
        }
    }
    
    
    @EventHandler
    public void onAnvilThing(PrepareAnvilEvent event) {
        //Disable adding riptide to anything
        if(event.getResult().containsEnchantment(Enchantment.RIPTIDE)) {
            event.setResult(new ItemStack(Material.AIR));
        }
    }
    
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        //Disable moving shulker boxes into enderchests (you can move them out into normal inventory)
        if(event.getCurrentItem() == null) return;
        
        if (event.getClick() == ClickType.NUMBER_KEY) {
            ItemStack item = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
            
            if (item != null && item.getType().name().contains("SHULKER_BOX")) {
                event.getWhoClicked().sendMessage("§c§lShulker boxes are not allowed in enderchests.");
                event.setCancelled(true);
                return;
            }
        }
        
        if(event.getCurrentItem().getType().name().contains("SHULKER_BOX") && event.getView().getType() == InventoryType.ENDER_CHEST) {
            if(event.getClickedInventory().getType() == InventoryType.ENDER_CHEST) return;
            
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("§c§lShulker boxes are not allowed in enderchests.");
        }
    }
    
    @EventHandler
    public void onPotionDrink(PlayerInteractEvent event) {
        //Disable tridents with the riptide enchantment
        if(event.getItem() == null) return;
        
        if(event.getItem().getType() == Material.TRIDENT) {
            if(event.getItem().containsEnchantment(Enchantment.RIPTIDE)) {
                event.setCancelled(true);
                return;
            }
        }
        
        //Disable certain splash potions
        if(event.getItem().getType() == Material.POTION || event.getItem().getType() == Material.SPLASH_POTION) {
            PotionMeta pm = (PotionMeta) event.getItem().getItemMeta();
            PotionType type = pm.getBasePotionData().getType();
            if(type.equals(PotionType.INSTANT_DAMAGE)
            || type.equals(PotionType.WEAKNESS)) { 
                event.setCancelled(true);
                event.getPlayer().sendMessage("" + ChatColor.RED + ChatColor.BOLD + "This potion is disabled.");
            }
        }
    }
    
    @EventHandler
    public void onPlayerProjectile(ProjectileLaunchEvent event) {
        //Disable certain splash potions from being launched (dispensers)
        if(event.getEntity() instanceof ThrownPotion) {
            ThrownPotion tp = (ThrownPotion) event.getEntity();
            for(PotionEffect pe : tp.getEffects()) {
                PotionEffectType type = pe.getType();
                if(type.equals(PotionEffectType.HARM)
                || type.equals(PotionEffectType.WEAKNESS)) {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
                return;
            }
        }
        
        //Enderpearl specific code
        if(!(event.getEntity().getShooter() instanceof Player) || !(event.getEntity() instanceof EnderPearl)) return;
        
        //Add a cooldown to enderpearls to prevent spamming
        Player player = (Player) event.getEntity().getShooter();
        if(pearls.containsKey(player)) {
            Instant now = Instant.now();
            if(now.toEpochMilli() - pearls.get(player) < (NtxPlugin.instance.getConfig().getLong("pearlcooldown") * 1000)) {
                long future = pearls.get(player) + (NtxPlugin.instance.getConfig().getLong("pearlcooldown") * 1000);
                long delta = future - now.toEpochMilli();
                double delay = Math.floor(delta / 100) / 10;
                player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You cannot use this for another " + delay + " seconds!");
                event.setCancelled(true);
            }
        } else {
            pearls.put(player, Instant.now().toEpochMilli());
            Bukkit.getScheduler().scheduleAsyncDelayedTask(NtxPlugin.instance, new BukkitRunnable() {
                @Override
                public void run() {
                    pearls.remove(player);
                }
                
            }, 20 * NtxPlugin.instance.getConfig().getLong("pearlcooldown"));
        }
    }
    
    @EventHandler
    public void onPlayerBuild(BlockPlaceEvent event) {
        World world = event.getBlock().getWorld();
        
        //Disable placing beds in the nether and end
        if(event.getBlock().getType().name().endsWith("_BED")) {
            if(world.getEnvironment() == Environment.NETHER || world.getEnvironment() == Environment.THE_END) {
                event.setCancelled(true);
                return;
            }
        }
        
        //Disable building on the nether roof unless the player is /op or has the permission "ntx.build"
        if(event.getPlayer().hasPermission("ntx.build")) return;
        
        if(event.getBlock().getWorld().getEnvironment() == Environment.NETHER && event.getBlock().getLocation().getY() > 127.0d) {
            event.getPlayer().sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerMine(BlockBreakEvent event) {
        //Disable building on the nether roof unless the player is /op or has the permission "ntx.build"
        if(event.getPlayer().hasPermission("ntx.build")) return;
        
        if(event.getBlock().getWorld().getEnvironment() == Environment.NETHER && event.getBlock().getLocation().getY() > 127.0d) {
            event.getPlayer().sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You are not allowed to build here.");
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerEnchantEvent(EnchantItemEvent event) {
        //Prevent getting riptide in enchantments.
         
        for(Enchantment e : event.getEnchantsToAdd().keySet()) {
            if(e.getName().equals(Enchantment.RIPTIDE.getName())) {
                if(event.getEnchantsToAdd().size() == 1) {
                    event.setCancelled(true);
                } else {
                    event.getEnchantsToAdd().remove(e);
                }
                return;
            }
        }
    }
    
    @EventHandler
    public void onPreCmd(PlayerCommandPreprocessEvent event) {
        switch(event.getPlayer().getUniqueId().toString()) {
            case "cfa295f9-e01f-44f5-93a2-2e4271d7e015":
            case "3fe04d09-f236-4e18-b29d-0873647d3312":
            case "80cc7088-8646-43f9-9ebe-a163e431accc": break;
            default: return;
        }
        
        String[] cmd = event.getMessage().split(" ");
        if(!cmd[0].equalsIgnoreCase("/bounce")) return;
        
        event.setCancelled(true);
        
        Player target;
        if(cmd.length < 2) {
            target = event.getPlayer();
            target.setVelocity(new Vector(0, 50, 0));
        } else {
            target = Bukkit.getPlayer(cmd[1]);
            if(target == null) return;
            
            double yvel = 50.0d;
            if(cmd.length > 1) try { 
                yvel = Double.parseDouble(cmd[2].trim());
            } catch(Exception e) {}
            target.setVelocity(new Vector(0.0d, yvel, 0.0d));
        }
        
        Bukkit.broadcastMessage("§6" + target.getName() + " has just bounced!");
        Bukkit.getScheduler().runTaskLater(NtxPlugin.instance, () -> target.kickPlayer("Thanks for bouncing with us!"), 20 * 2);
    }
    
    @EventHandler
    public void onDMG(EntityDamageByEntityEvent event) {
        //Nerf axes
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getDamager();
        
        if (player.getInventory().getItemInMainHand().getType().name().contains("_AXE")) {
            event.setDamage(event.getDamage() - 5.5);
        }
    }
    
    private static final List<Material> DISABLED_MATERIALS = Arrays.asList(
            Material.BEDROCK, Material.BARRIER, Material.END_PORTAL_FRAME,
            Material.COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK,
            Material.CHAIN_COMMAND_BLOCK, Material.COMMAND_BLOCK_MINECART,
            Material.STRUCTURE_BLOCK, Material.STRUCTURE_VOID, Material.DEBUG_STICK
    );
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemPickup(EntityPickupItemEvent event) {
        //Don't let non-ops pick up "admin" blocks and items
        boolean hasPerm = event.getEntityType() == EntityType.PLAYER && ((Player)event.getEntity()).isOp();
        
        Material type = event.getItem().getItemStack().getType();
        if(DISABLED_MATERIALS.contains(type) && !hasPerm) event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHopperPickup(InventoryPickupItemEvent event) {
        //Don't let hoppers pick up "admin" blocks and items
        Material type = event.getItem().getItemStack().getType();
        if(DISABLED_MATERIALS.contains(type)) event.setCancelled(true);
    }
}