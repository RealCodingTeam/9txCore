package org.realcodingteam.plan9.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.realcodingteam.plan9.NtxPlugin;

public class StaffChatCommand implements CommandExecutor, Listener {
    
    private static final String FORMAT = "§3Staff §8>> §9%s§8: §f%s";
    
    public StaffChatCommand() {
        Bukkit.getPluginManager().registerEvents(this, NtxPlugin.instance());
    }
    
    private static String getOnlineStaff() {
        String[] names = Bukkit.getOnlinePlayers().stream()
                  .filter(p -> p.hasPermission("ntx.staff"))
                  .map(Player::getDisplayName)
                  .map(n -> n + "§r")
                  .toArray(String[]::new);
        return "§bOnline staff: §r" + Arrays.toString(names);
    }
    
    //Format a staff chat message
    private static String formatMessage(String name, String msg) {
        if(name.trim().isEmpty()) name = "§a[DannyDefaultName]";
        if(msg.trim().isEmpty()) msg = "§aI'm cool lol!";
        return String.format(FORMAT, name, msg);
    }
    
    //Send a message in staff chat
    public static void sendStaffMessage(String name, String msg) {
        String formatted = formatMessage(name, msg);
        
        List<Player> staff = Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("ntx.staff")).collect(Collectors.toList());
        for(Player p : staff) {
            
            //play an effect if the message contains their name
            if(ChatColor.stripColor(msg).toLowerCase().contains(p.getName().toLowerCase())) {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, .5f, 1.0f);
                String edited = msg.replace(p.getName(), ChatColor.YELLOW + "" + ChatColor.BOLD + "@" + p.getName() + ChatColor.RESET);
                p.sendMessage(formatMessage(name, edited));
            } else {
                p.sendMessage(formatted);
            }
        }
        
        Bukkit.getConsoleSender().sendMessage(formatted);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("ntx.staff")) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You lack permission.");
            return true;
        }
        
        if(args.length < 1) {
            sender.sendMessage(getOnlineStaff());
            return true;
        }
        
        String name;
        String msg = String.join(" ", args);
        
        if(!(sender instanceof Player)) {
            
            //Send the console's message in staff chat
            name = ChatColor.RED + "" + "[CONSOLE]";
        } else {
            Player p = (Player)sender;
            
            name = p.getDisplayName().replace("[CONSOLE]", "§4[§6n§2o§1o§5b§0]");
        }
        
        sendStaffMessage(name, msg);
        return true;
    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player who = event.getPlayer();
        if(!who.hasPermission("ntx.staff")) return;
        
        String[] msg = event.getMessage().split(" ");
        if(!msg[0].toLowerCase().startsWith("-sc")) return;
        
        event.setCancelled(true);
        
        if(msg.length < 2) {
            who.sendMessage(getOnlineStaff());
            return;
        }
        
        String content = String.join(" ", Arrays.copyOfRange(msg, 1, msg.length));
        content = ChatColor.translateAlternateColorCodes('^', content);
        content = content.replace("" + ChatColor.MAGIC, "")
                 .replace("" + ChatColor.STRIKETHROUGH, "");
        
        sendStaffMessage(who.getDisplayName().replace("[CONSOLE]", "§4[§6n§2o§1o§5b§0]"), content);
    }
    
}
