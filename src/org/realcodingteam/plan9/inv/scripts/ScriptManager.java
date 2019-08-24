package org.realcodingteam.plan9.inv.scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.NtxPlugin;
import org.realcodingteam.plan9.data.DonorPlayer;

import net.md_5.bungee.api.ChatColor;

public final class ScriptManager {

    private static final File ROOT = new File(NtxPlugin.instance().getDataFolder(), "scripts");
    protected static final Map<MenuEntry, ScriptEngine> contexts = new HashMap<>();
    
    protected static void run(Player runner, MenuEntry entry) {
        ScriptEngine engine = getEngine(entry);
        
        try {
            engine.eval(getScript(entry.getScript()));
            Invocable script = (Invocable) engine;
            script.invokeFunction("run", runner, entry.getCost());
        } catch(Exception exception) {
            runner.sendMessage(ChatColor.RED + "Something went wrong with this donor effect. You have been refunded your DP.");
            runner.sendMessage(ChatColor.RED + "Please inform an admin of this issue.");
            DonorPlayer dp = DonorPlayer.getDonorPlayer(runner.getUniqueId());
            dp.setDp(dp.getDp() + entry.getCost());
            
            if(exception instanceof ScriptException || exception instanceof FileNotFoundException) {
                NtxPlugin.instance().getLogger().severe("The script for " + ChatColor.stripColor(entry.getName()) + " generated an exception: ");
                NtxPlugin.instance().getLogger().severe(exception.getClass().getSimpleName() + ": " + exception.getMessage());
            } else if(exception instanceof NoSuchMethodException) {
                NtxPlugin.instance().getLogger().severe("The script for " + ChatColor.stripColor(entry.getName()) + " has no run() function."); 
                NtxPlugin.instance().getLogger().severe("Put the effect code inside the run function to fix this error.");
            } else if(exception instanceof ScriptSecurityException) {
                NtxPlugin.instance().getLogger().severe("The script for " + ChatColor.stripColor(entry.getName()) + " attempted to violate security!");
                NtxPlugin.instance().getLogger().severe("ScriptSecurityException: " + exception.getMessage());
            } else {
                exception.printStackTrace();
            }
        }
    }
    
    protected static void init(MenuEntry entry) {
        ScriptEngine engine = getEngine(entry);
        
        try {
            engine.eval(getScript(entry.getScript()));
            Invocable script = (Invocable) engine;
            script.invokeFunction("init");
        } catch (ScriptException | FileNotFoundException  e) {
            e.printStackTrace();
        } catch(Exception ignored) {}
    }
    
    protected static boolean shouldShow(MenuEntry entry) {
        //The "isDynamic" flag is an optimization
        //Speeds up menu building.
        if(!entry.isDynamic()) return true;
        
        ScriptEngine engine = getEngine(entry);
        
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
    
    private static ScriptEngine getEngine(MenuEntry entry) {
        if(contexts.containsKey(entry)) return contexts.get(entry);
        
        ClassLoader loader = NtxPlugin.instance().getClass().getClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        
        Bindings bindings = engine.createBindings();
        bindings.put("SERVER", Bukkit.getServer());
        bindings.put("CTX", new ScriptContext());
        bindings.put("PLAYERS", DonorPlayer.getAllPlayers());
        bindings.put("Events", EventRegistrar.names);
        engine.setBindings(bindings, javax.script.ScriptContext.GLOBAL_SCOPE);
        
        contexts.put(entry, engine);
        return engine;
    }
    
    private static FileReader getScript(String file) throws FileNotFoundException, ScriptSecurityException {
        if(!ROOT.exists()) ROOT.mkdir();
        if(!file.toLowerCase().endsWith(".js")) file = file + ".js";
        File script = new File(ROOT, file);
        
        try {
            if(!script.getCanonicalPath().startsWith(ROOT.getAbsolutePath())) throw new ScriptSecurityException("Script patchs cannot be outside of the script root directory (" + ROOT.getAbsolutePath() + ")");
        } catch (IOException e) {
            NtxPlugin.instance().getLogger().warning("Could not check script path for " + file + ", MAKE SURE THE PATH IS CORRECT.");
        }
        
        FileReader reader = new FileReader(script);
        return reader;
    }
    
    @SuppressWarnings("serial")
    private static class ScriptSecurityException extends Exception {
        private final String msg;
        
        public ScriptSecurityException(String msg) {
            this.msg = msg;
        }
        
        @Override
        public String getMessage() {
            return msg;
        }
    }
}
