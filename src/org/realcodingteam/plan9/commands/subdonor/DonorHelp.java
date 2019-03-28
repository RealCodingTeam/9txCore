package org.realcodingteam.plan9.commands.subdonor;

import org.bukkit.command.CommandSender;
import org.realcodingteam.plan9.commands.DonorCommand;

import net.md_5.bungee.api.ChatColor;

public class DonorHelp extends SubDonorCommand {

    private static String help_string = null;
    
    @Override
    public void run(CommandSender sender, String[] args) {
        if(help_string == null) {
            int cmds = DonorCommand.getNumberOfSubCommands();
            String fmt = "§a%s§e";
            StringBuilder sb = new StringBuilder();
            sb.append("§e[");
            for(int i = 0; i < cmds; i++) {
                sb.append(fmt);
                
                if(i + 1 < cmds) sb.append(" | ");
            }
            sb.append("§e]");
            
            help_string = String.format(sb.toString(), DonorCommand.getCommands().toArray());
        }
        
        sender.sendMessage(ChatColor.GREEN + "§eTry /donor " + help_string);
    }
    
}
