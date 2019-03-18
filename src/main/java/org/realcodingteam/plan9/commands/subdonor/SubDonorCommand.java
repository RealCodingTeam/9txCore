package org.realcodingteam.plan9.commands.subdonor;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.objects.DonorPlayer;

import net.md_5.bungee.api.ChatColor;

public abstract class SubDonorCommand {
    
    public abstract void run(CommandSender sender, String[] args);
    
    protected final int getInt(String arg) {
        try {
            return (int)Double.parseDouble(arg);
        } catch(NumberFormatException e) { return 0; }
    }
    
    @SuppressWarnings("deprecation")
    protected final DonorPlayer getPlayer(String input) {
        OfflinePlayer player = null;
        if (input.matches("[0-9a-f]{8}[-]*[0-9a-f]{4}[-]*[1-5][0-9a-f]{3}[-]*[89ab][0-9a-f]{3}[-]*[0-9a-f]{12}") && input.length() == 36 || input.length() == 32) {
            if(!input.contains("-")) input = addDashesToUuid(input);
            if(input == null) return null;
            player = Bukkit.getOfflinePlayer(UUID.fromString(input));
        } else {
            player = Bukkit.getOfflinePlayer(input);
        }
        
        if(player == null) return null;
        return DonorPlayer.getDonorPlayer(player.getUniqueId());
    }

    
    protected final boolean hasPerm(CommandSender sender, String perm) {
        if(!sender.hasPermission(perm)) {
            sender.sendMessage(ChatColor.RED + "You lack permission.");
            return false;
        }
        return true;
    }
    
    public static boolean stopConsole(CommandSender sender) {
        if(sender instanceof Player) return false;
        sender.sendMessage(ChatColor.RED + "Error: You cannot run this command from console.");
        return true;
    }
    
    private static String addDashesToUuid(String input) {
        String part1 = input.substring(0, 8);
        String part2 = input.substring(8, 12);
        String part3 = input.substring(12, 16);
        String part4 = input.substring(16, 20);
        String part5 = input.substring(20, 32);
        return String.format("%s-%s-%s-%s-%s", part1, part2, part3, part4, part5);
    }
}
