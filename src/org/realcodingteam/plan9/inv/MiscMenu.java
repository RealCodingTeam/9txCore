package org.realcodingteam.plan9.inv;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.inv.scripts.Menu;
import org.realcodingteam.plan9.inv.scripts.ScriptedMenu;

public class MiscMenu extends ScriptedMenu {
    
    public MiscMenu(Player viewer) {
        super(Menu.MISC, 9, ChatColor.DARK_PURPLE + "Donor - Misc", viewer, RootMenu::new);
        
        open(viewer);
        build();
    }

    @Override
    public boolean needsRefresh() {
        return true;
    }
}
