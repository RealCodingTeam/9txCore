package org.realcodingteam.plan9.misc.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Cvar<T> {

    private static final Map<UUID, List<Cvar<?>>> vars = new HashMap<>();
    
    protected T value;
    private final String name;
    
    protected Cvar(String name, T value) {
        this.name = name;
        this.value = value;
    }
    
    public final String getName() {
        return name;
    }
    
    public void setValue(T val) {
        this.value = val;
    }
    
    public T getValue() {
        return value;
    }
    
}
