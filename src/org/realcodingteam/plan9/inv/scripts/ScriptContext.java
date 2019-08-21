package org.realcodingteam.plan9.inv.scripts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.event.Event;
import org.realcodingteam.plan9.NtxPlugin;

public final class ScriptContext {
    
    private final Map<Class<Event>, List<Consumer<Event>>> handlers = new HashMap<>();
    private static final Map<String, Object> globalVars = new HashMap<>();
    
    protected ScriptContext() {
        EventRegistrar.register(this);
    }
    
    protected void reset() {
        handlers.clear();
        globalVars.clear();
    }
    
    protected void callEvent(Event event) {
        if(event == null) return;
        
        List<Consumer<Event>> functions = handlers.get(event.getClass());
        if(functions == null || functions.isEmpty()) return;
        
        functions.forEach(fun -> fun.accept(event));
    }

    @SuppressWarnings("unchecked")
    public void onEvent(String event_name, Consumer<Event> function) {
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
        
        List<Consumer<Event>> functions = handlers.computeIfAbsent((Class<Event>) clazz, _clazz -> new ArrayList<>());
        functions.add(function);
    }
    
    public Object getVar(String key) {
        if(key == null) return null;
        return globalVars.getOrDefault(key, null);
    }
    
    public void setVar(String key, Object val) {
        if(key == null) return;
        if(val == null) {
            if(!globalVars.containsKey(key)) return;
            
            globalVars.remove(key);
        }
        
        globalVars.put(key, val);
    }
}
