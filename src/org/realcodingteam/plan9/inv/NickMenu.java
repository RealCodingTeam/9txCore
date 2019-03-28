package org.realcodingteam.plan9.inv;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.NtxPlugin;
import org.realcodingteam.plan9.objects.DonorPlayer;
import org.realcodingteam.plan9.objects.RainbowWool;

public class NickMenu extends AbstractMenu {

    int taskid = -1;
    
    public NickMenu(Player viewer) {
        super(18, ChatColor.DARK_PURPLE + "Donor - Nick", viewer, true);
        
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
            taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(NtxPlugin.instance, () -> {
                inv.setItem(16, RainbowWool.instance.getWool());
            }, 0L, 10L);
        } else {
            inv.setItem(16, makeItem(Material.BEDROCK, "§4§nDonate for§9§l End §4§nto access multi-colored names!"));
        }
    }
    
    protected static ChatColor colorFromItem(Material mat) {
        ChatColor color = ChatColor.WHITE;
        switch(mat) {
            case BLACK_WOOL:      color = ChatColor.BLACK; break;
            case LAPIS_BLOCK:     color = ChatColor.DARK_BLUE; break;
            case GREEN_WOOL:      color = ChatColor.DARK_GREEN; break;
            case CYAN_WOOL:       color = ChatColor.DARK_AQUA; break;
            case RED_WOOL:        color = ChatColor.DARK_RED;    break;
            case PURPLE_WOOL:     color = ChatColor.DARK_PURPLE; break;
            case GOLD_BLOCK:      color = ChatColor.GOLD; break;
            case STONE:           color = ChatColor.GRAY; break;
            case GRAY_WOOL:       color = ChatColor.DARK_GRAY; break;
            case BLUE_WOOL:       color = ChatColor.BLUE; break;
            case LIME_WOOL:       color = ChatColor.GREEN; break;
            case LIGHT_BLUE_WOOL: color = ChatColor.AQUA; break;
            case RED_TERRACOTTA:  color = ChatColor.RED; break;
            case PINK_CONCRETE:   color = ChatColor.LIGHT_PURPLE; break;
            case YELLOW_WOOL:     color = ChatColor.YELLOW; break;
            case WHITE_WOOL:      color = ChatColor.WHITE; break;
            default: break;
        }
        return color;
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
    public void close(Player p) {
        open_invs.removeIf(p2 -> p2.getUniqueId().equals(p.getUniqueId()));
        Bukkit.getScheduler().cancelTask(taskid);
    }
}
