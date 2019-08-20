package org.realcodingteam.plan9.inv.scripts;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.inv.AbstractMenu;
import org.realcodingteam.plan9.inv.effects.Effects;

public abstract class ScriptedMenu extends AbstractMenu {

    private final Menu category;
    
    public ScriptedMenu(Menu category, int size, String title, Player viewer, Consumer<Player> parent) {
        super(size, title, viewer, parent);
        this.category = category;
    }
    
    @Override
    protected void build() {
        IntStream.range(0, inv.getSize()).forEach(i -> inv.setItem(i, new ItemStack(Material.AIR)));
        
        for(MenuEntry entry : category.getEntries()) {
            if(!ScriptManager.shouldShow(entry)) continue;
            
            inv.setItem(entry.getMenuSlot(), entry.getItem());
        }
        
        setDonorSlot(viewer, inv.getSize() - 1);
    }
    
    @Override
    public void onInventoryClick(ItemStack item) {
        for(MenuEntry entry : category.getEntries()) {
            if(!entry.getItem().equals(item)) continue;
            
            Effects.buyAndRun(viewer, entry);
            return;
        }
    }
}
