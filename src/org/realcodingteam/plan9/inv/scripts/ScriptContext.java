package org.realcodingteam.plan9.inv.scripts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.realcodingteam.plan9.NtxPlugin;

/*
 * This class provides event handling and a global variable table for scripts to use
 * Event handling is done per script, meaning each script has its own EventExecutor
 * that listens for requested events only
 */
public final class ScriptContext {
    
    private static final List<ScriptContext> contexts = new ArrayList<>();
    private static final Map<String, Object> globalVars = new HashMap<>();
    
    private final Map<Class<Event>, List<Consumer<Event>>> handlers = new HashMap<>();
    private final NtxEventExecutor executor;
    
    protected ScriptContext() {
        this.executor = new NtxEventExecutor(this);
        
        contexts.add(this);
    }
    
    protected void reset() {
        handlers.clear();
        globalVars.clear();
    }
    
    private void callEvent(Event event) {
        if(event == null) return;
        
        List<Consumer<Event>> functions = handlers.get(event.getClass());
        if(functions == null || functions.isEmpty()) return;
        
        functions.forEach(fun -> fun.accept(event));
    }
    
    @SuppressWarnings("unchecked")
    public void onEvent(String event_name, EventPriority priority, boolean ignoreCancelled, Consumer<Event> function) {
        if(event_name == null || function == null) return;
        
        Class<?> clazz;
        try {
            clazz = Class.forName(event_name, false, EventRegistrar.class.getClassLoader());
            if(!Event.class.isAssignableFrom(clazz)) {
                NtxPlugin.instance().getLogger().severe("A script attempted to register an invalid event type.");
                NtxPlugin.instance().getLogger().severe(event_name + " is not a subclass of org.bukkit.event.Event");
                return;
            }
        } catch(ClassNotFoundException exception) {
            NtxPlugin.instance().getLogger().severe("A script attempted to register an unknown class as an event handler.");
            NtxPlugin.instance().getLogger().severe("The class " + event_name + " does not exist.");
            return;
        }
        
        List<Consumer<Event>> functions = handlers.computeIfAbsent((Class<Event>) clazz, _clazz -> {
            //This is a shortcut to only register the event once per script context
            Bukkit.getServer().getPluginManager().registerEvent((Class<Event>) clazz, executor.getListener(), priority, executor, NtxPlugin.instance(), ignoreCancelled);
            return new ArrayList<>();
        });
        functions.add(function);
    }

    public void onEvent(String event_name, Consumer<Event> function) {
        onEvent(event_name, EventPriority.NORMAL, false, function);
    }
    
    public Object getVar(String key) {
        if(key == null) return null;
        return globalVars.getOrDefault(key, null);
    }
    
    public void setVar(String key, Object val) {
        if(key == null) return;
        if(val == null) {
            if(globalVars.containsKey(key)) globalVars.remove(key);
            return;
        }
        
        globalVars.put(key, val);
    }

    public static void unloadAll() {
        for(ScriptContext context : contexts) {
            HandlerList.unregisterAll(context.executor.getListener());
            context.reset();
        }
        
        contexts.clear();
    }

    private static final class NtxEventExecutor implements EventExecutor {

        private final ScriptContext context;
        private final Listener listener;
        
        private NtxEventExecutor(ScriptContext context) {
            this.context = context;
            this.listener = new Listener() {};
        }
        
        public Listener getListener() {
            return listener;
        }
        
        @Override
        public void execute(Listener listener, Event event) {
            context.callEvent(event);
        }

    }
}
