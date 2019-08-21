package org.realcodingteam.plan9.inv;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.inv.scripts.Menu;
import org.realcodingteam.plan9.inv.scripts.ScriptedMenu;

public class PotionsMenu extends ScriptedMenu {

    public PotionsMenu(Player viewer) {
        super(Menu.POTIONS, 36, ChatColor.DARK_PURPLE + "Donor - Potion Effects", viewer, RootMenu::new);
    }

    @Override
    public boolean needsRefresh() {
        return false;
    }
    
}