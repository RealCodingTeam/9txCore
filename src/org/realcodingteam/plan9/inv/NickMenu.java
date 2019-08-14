package org.realcodingteam.plan9.inv;

import static org.realcodingteam.plan9.util.Item.makeItem;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.data.DonorPlayer;

public class NickMenu extends AbstractMenu {
    
    public NickMenu(Player viewer) {
        super(18, ChatColor.DARK_PURPLE + "Donor - Nick", viewer, RootMenu::new);
        
        build();
        open(viewer);
    }

    @Override
    protected void build() {
        inv.setItem(0,  makeItem(Material.BLACK_WOOL,        "§0Black §fname"));
        inv.setItem(1,  makeItem(Material.LAPIS_BLOCK,       "§1Dark blue §fname"));
        inv.setItem(2,  makeItem(Material.GREEN_WOOL,        "§2Dark green §fname"));
        inv.setItem(3,  makeItem(Material.CYAN_WOOL,         "§3Dark aqua §fname"));
        inv.setItem(4,  makeItem(Material.RED_WOOL,          "§4Dark red §fname"));
        inv.setItem(5,  makeItem(Material.PURPLE_WOOL,       "§5Purple §fname"));
        inv.setItem(6,  makeItem(Material.GOLD_BLOCK,        "§6Gold §fname"));
        inv.setItem(7,  makeItem(Material.STONE,             "§7Gray §fname"));
        inv.setItem(8,  makeItem(Material.GRAY_WOOL,         "§8Dark gray §fname"));
        inv.setItem(9,  makeItem(Material.BLUE_WOOL,         "§9Blue §fname"));
        inv.setItem(10, makeItem(Material.LIME_WOOL,         "§aGreen §fname"));
        inv.setItem(11, makeItem(Material.LIGHT_BLUE_WOOL,   "§bAqua §fname"));
        inv.setItem(12, makeItem(Material.RED_TERRACOTTA,    "§cRed §fname"));
        inv.setItem(13, makeItem(Material.PINK_CONCRETE,     "§dLight purple §fname"));
        inv.setItem(14, makeItem(Material.YELLOW_WOOL,       "§eYellow §fname"));
        inv.setItem(15, makeItem(Material.WHITE_WOOL,        "§fWhite §fname"));
        
        if(viewer.hasPermission("ntx.end")) {
            inv.setItem(16, RainbowWool.getWool());
        } else {
            inv.setItem(16, makeItem(Material.BEDROCK, "§4§nDonate for§9§l End §4§nto access multi-colored names!"));
        }
    }
    
    private static final Map<Material, ChatColor> COLOR_MAP;
    static {
        COLOR_MAP = new HashMap<>();
        
        COLOR_MAP.put(Material.BLACK_WOOL, ChatColor.BLACK);
        COLOR_MAP.put(Material.LAPIS_BLOCK, ChatColor.DARK_BLUE);
        COLOR_MAP.put(Material.GREEN_WOOL, ChatColor.DARK_GREEN);
        COLOR_MAP.put(Material.CYAN_WOOL, ChatColor.DARK_AQUA);
        COLOR_MAP.put(Material.RED_WOOL, ChatColor.DARK_RED);
        COLOR_MAP.put(Material.PURPLE_WOOL, ChatColor.DARK_PURPLE);
        COLOR_MAP.put(Material.GOLD_BLOCK, ChatColor.GOLD);
        COLOR_MAP.put(Material.STONE, ChatColor.GRAY);
        COLOR_MAP.put(Material.GRAY_WOOL, ChatColor.DARK_GRAY);
        COLOR_MAP.put(Material.BLUE_WOOL, ChatColor.BLUE);
        COLOR_MAP.put(Material.LIME_WOOL, ChatColor.GREEN);
        COLOR_MAP.put(Material.LIGHT_BLUE_WOOL, ChatColor.AQUA);
        COLOR_MAP.put(Material.RED_TERRACOTTA, ChatColor.RED);
        COLOR_MAP.put(Material.PINK_CONCRETE, ChatColor.LIGHT_PURPLE);
        COLOR_MAP.put(Material.YELLOW_WOOL, ChatColor.YELLOW);
        COLOR_MAP.put(Material.WHITE_WOOL, ChatColor.WHITE);
    }
    
    protected static ChatColor colorFromItem(Material mat) {
        return COLOR_MAP.getOrDefault(mat, ChatColor.WHITE);
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        if(item.hasItemMeta() && item.getItemMeta().getDisplayName().contains("Multi-color")) {
            close(viewer);
            new MultiColorNickMenu(viewer);
            return;
        }
        
        ChatColor color = colorFromItem(item.getType());
        
        if(!viewer.hasPermission("ntx.donor.nick")) {
            viewer.sendMessage(ChatColor.YELLOW + "Donate for a rank to change your name color!");
            viewer.sendMessage(ChatColor.GREEN + "Store link: " + ChatColor.ITALIC + "http://donate.0tx.org");
            viewer.closeInventory();
            return;
        }
        
        viewer.setDisplayName(color + viewer.getName() + ChatColor.RESET);
        viewer.setPlayerListName(color + viewer.getName() + ChatColor.RESET);
        DonorPlayer.getDonorPlayer(viewer.getUniqueId()).setNick("" + color.getChar());
        viewer.sendMessage(ChatColor.YELLOW + "You set your name to: " + viewer.getDisplayName());
    }

    @Override
    public boolean needsRefresh() {
        return true;
    }
}
