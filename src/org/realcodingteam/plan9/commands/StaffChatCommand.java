package org.realcodingteam.plan9.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.realcodingteam.plan9.NtxPlugin;

public class StaffChatCommand implements CommandExecutor, Listener {
    
    public StaffChatCommand() {
        Bukkit.getPluginManager().registerEvents(this, NtxPlugin.instance);
    }
    
    private static final String FORMAT = "§3Staff §8>> §9%s§8: §f%s";
    private static final List<UUID> chatPlayers = new ArrayList<>();
    
    private static String getOnlineStaff() {
        String[] names = Bukkit.getOnlinePlayers().stream()
                  .filter(p -> p.hasPermission("ntx.staff"))
                  .map(Player::getDisplayName)
                  .map(n -> n + "§r")
                  .toArray(String[]::new);
        return "§bOnline staff: §r" + Arrays.toString(names);
    }
    
    //Remove a player from staff chat
    public static void takeOutOfStaffChat(Player p) {
        if(isInStaffChat(p)) chatPlayers.remove(p.getUniqueId());
    }
    
    //Put a player in staff chat
    public static void putInStaffChat(Player p) {
        if(!isInStaffChat(p)) chatPlayers.add(p.getUniqueId());
    }
    
    //Is a player in staff chat
    public static boolean isInStaffChat(Player p) {
        return chatPlayers.contains(p.getUniqueId());
    }
    
    //Format a staff chat message
    private static String formatMessage(String name, String msg) {
        if(name.trim().isEmpty()) name = "§a[DannyDefaultName]";
        if(msg.trim().isEmpty()) msg = "§aI'm cool lol!";
        return String.format(FORMAT, name, msg);
    }
    
    //Send a message in staff chat
    public static void sendStaffMessage(String name, String msg) {
        msg = ChatColor.translateAlternateColorCodes('^', msg);
        msg = msg.replace("" + ChatColor.MAGIC, "")
                 .replace("" + ChatColor.STRIKETHROUGH, "");
        String formatted = formatMessage(name, msg);
        
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.hasPermission("ntx.staff")) p.sendMessage(formatted);
        }
        
        Bukkit.getConsoleSender().sendMessage(formatted);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        String name, msg;
        
        //Console is using the command
        if(!(sender instanceof Player)) {
            if(args.length < 1) {
                sender.sendMessage(ChatColor.RED + "You cannot toggle staff chat as console. Send your message as arguments to the command.");
                return true;
            }
            
            if(args[0].equalsIgnoreCase(".") && args.length < 2) {
                sender.sendMessage(getOnlineStaff());
                return true;
            }
            
            //Send the console's message in staff chat
            name = ChatColor.RED + "" + "[CONSOLE]";
            msg = String.join(" ", args);
            
            sendStaffMessage(name, msg);
            return true;
        }

        Player p = (Player)sender;
        
        if(!p.hasPermission("ntx.staff")) {
            //If a player tries to use staff chat and they don't have permission, but they're in staff chat, remove them from staff chat.
            if(isInStaffChat(p)) {
                takeOutOfStaffChat(p);
                sender.sendMessage(ChatColor.DARK_AQUA + "You are now in public chat.");
                return true;
            }
            //Otherwise, just tell them to bugger off
            p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You lack permission.");
            return true;
        }
        
        //Toggle staff chat for the player if they only send the command, no arguments
        if(args.length < 1) {
            if(!isInStaffChat(p)) {
                p.sendMessage(ChatColor.DARK_AQUA + "You are now in staff chat.");
                putInStaffChat(p);
            } else {
                p.sendMessage(ChatColor.DARK_AQUA + "You are now in public chat.");
                takeOutOfStaffChat(p);
            }
            return true;
        }
        
        if(args[0].equalsIgnoreCase(".") && args.length < 2) {
            sender.sendMessage(getOnlineStaff());
            return true;
        }
        
        //They sent a message after the command, send it in staff chat.
        name = p.getDisplayName().replace("[CONSOLE]", "[noob]");
        msg = String.join(" ", args);
        
        sendStaffMessage(name, msg);
        return true;
    }
    
    //Handle players talking in toggled staff chat.
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        
        if(isInStaffChat(p)) {
            if(!p.hasPermission("ntx.staff")) {
                takeOutOfStaffChat(p);
                return;
            }
            event.setCancelled(true);
            sendStaffMessage(p.getDisplayName(), event.getMessage());
        }
    }
    
    //Inform players which chat they are in.
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!event.getPlayer().hasPermission("ntx.staff")) return;
        if(isInStaffChat(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "You are now in staff chat.");
        }
    }
    
}
