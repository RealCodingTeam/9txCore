package org.realcodingteam.plan9.patches;

public abstract class BasePatch implements TxPatch {

    protected boolean enabled = false;
    
    @Override
    public void onEnable() {
        enabled = true;
    }
    
    @Override
    public void onDisable() {
        if(!enabled) return;
        
        enabled = false;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
}
