package org.realcodingteam.plan9.inv;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.inv.scripts.Menu;
import org.realcodingteam.plan9.inv.scripts.ScriptedMenu;

public final class OresMenu extends ScriptedMenu {

    public OresMenu(Player viewer) {
        super(Menu.ORES, 9, ChatColor.DARK_PURPLE + "Donor - Ores", viewer, RootMenu::new);
    }

    @Override
    public boolean needsRefresh() {
        return false;
    }
}
