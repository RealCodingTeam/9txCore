package org.realcodingteam.plan9.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.commands.subdonor.*;
import org.realcodingteam.plan9.inv.*;
import org.realcodingteam.plan9.inv.MiscMenu;
import org.realcodingteam.plan9.inv.PotionsMenu;

public class DonorCommand implements CommandExecutor, TabCompleter {

    private static Map<String, BiConsumer<CommandSender, String[]>> subcommands;
    static {
        subcommands = new HashMap<>();
        subcommands.put("xp",     (sender, args) -> menu(sender, ExpMenu::new));
        subcommands.put("ores",   (sender, args) -> menu(sender, OresMenu::new));
        subcommands.put("potion", (sender, args) -> menu(sender, PotionsMenu::new));
        subcommands.put("nick",   (sender, args) -> menu(sender, NickMenu::new));
        subcommands.put("misc",   (sender, args) -> menu(sender, MiscMenu::new));
        
        subcommands.put("help", new DonorHelp()::run);
        subcommands.put("pay",  new DonorPay()::run);
        subcommands.put("send", new DonorPay()::run);
        subcommands.put("view", new DonorView()::run);
        subcommands.put("add",  new DonorAddSet()::run);
        subcommands.put("set",  new DonorAddSet()::run);
    }

    public static int getNumberOfSubCommands() {
        return subcommands.size();
    }
    
    public static Set<String> getCommands() {
        return subcommands.keySet();
    }
    
    private static void menu(CommandSender sender, Function<Player, AbstractMenu> type) {
        if(SubDonorCommand.stopConsole(sender)) return;
        type.apply((Player)sender);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            menu(sender, RootMenu::new);
            return true;
        }
        
        BiConsumer<CommandSender, String[]> run;
        if(subcommands.containsKey(args[0].toLowerCase())) {
            run = subcommands.get(args[0].toLowerCase());
        } 
        else {
            List<String> completions = getOptions(args[0]);
            if(completions == null || completions.size() < 1 || completions.size() > 1) run = new DonorHelp()::run;
            else run = subcommands.get(completions.get(0));
        }
        
        run.accept(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) return subcommands.keySet().stream().collect(Collectors.toList());
        if(args.length > 1) return null;
        
        return getOptions(args[0]);
    }
    
    private List<String> getOptions(String arg) {
        return subcommands.keySet()
                          .stream()
                          .map(String::toLowerCase)
                          .filter(cmd -> cmd.startsWith(arg.toLowerCase()) || arg.trim().isEmpty())
                          .collect(Collectors.toList());
    }
}
