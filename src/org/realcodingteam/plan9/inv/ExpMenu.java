package org.realcodingteam.plan9.inv;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.inv.scripts.Menu;
import org.realcodingteam.plan9.inv.scripts.ScriptedMenu;

public final class ExpMenu extends ScriptedMenu {

    public ExpMenu(Player viewer) {
        super(Menu.EXP, 9, ChatColor.DARK_PURPLE + "Donor - XP", viewer, RootMenu::new);
    }

    @Override
    public boolean needsRefresh() {
        return false;
    }
    
}
