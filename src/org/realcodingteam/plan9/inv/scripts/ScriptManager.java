package org.realcodingteam.plan9.inv.scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.NtxPlugin;
import org.realcodingteam.plan9.data.DonorPlayer;

import net.md_5.bungee.api.ChatColor;

public final class ScriptManager {

    private static final File ROOT = new File(NtxPlugin.instance().getDataFolder(), "scripts");
    
    public static void run(Player runner, MenuEntry entry) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        
        engine.put("SERVER", Bukkit.getServer());
        try {
            engine.eval(getScript(entry.getScript()));
            Invocable script = (Invocable) engine;
            script.invokeFunction("run");
        } catch(Exception exception) {
            runner.sendMessage(ChatColor.RED + "Something went wrong with this donor effect. You have been refunded your DP.");
            runner.sendMessage(ChatColor.RED + "Please inform an admin of this issue.");
            DonorPlayer dp = DonorPlayer.getDonorPlayer(runner.getUniqueId());
            dp.setDp(dp.getDp() + entry.getCost());
            
            exception.printStackTrace();
            
            if(exception instanceof ScriptException || exception instanceof FileNotFoundException) {
                NtxPlugin.instance().getLogger().severe("The script for " + ChatColor.stripColor(entry.getName()) + " generated an exception: ");
                NtxPlugin.instance().getLogger().severe(exception.getClass().getSimpleName() + ": " + exception.getMessage());
            }
            
            if(exception instanceof NoSuchMethodException) {
                NtxPlugin.instance().getLogger().severe("The script for " + ChatColor.stripColor(entry.getName()) + " has no run() function."); 
                NtxPlugin.instance().getLogger().severe("Put the effect code inside the run function to fix this error."); 
            }
        }
    }
    
    public static boolean shouldShow(MenuEntry entry) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        
        engine.put("SERVER", Bukkit.getServer());
        try {
            engine.eval(getScript(entry.getScript()));
            Invocable script = (Invocable) engine;
            return (Boolean) script.invokeFunction("shouldShow");
        } catch (ScriptException | FileNotFoundException  e) {
            e.printStackTrace();
        } catch(Exception ignored) {}
        
        //If the function isn't defined, we just imply that it should show.
        return true;
    }
    
    private static FileReader getScript(String file) throws FileNotFoundException {
        if(!ROOT.exists()) ROOT.mkdir();
        if(!file.toLowerCase().endsWith(".js")) file = file + ".js";
        
        FileReader reader = new FileReader(new File(ROOT, file));
        return reader;
    }
}
