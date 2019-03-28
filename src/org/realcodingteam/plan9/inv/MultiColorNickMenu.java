package org.realcodingteam.plan9.inv;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.objects.DonorPlayer;

public final class MultiColorNickMenu extends AbstractMenu {

    private int index = 0;
    private final StringBuilder builder = new StringBuilder();
    
    public MultiColorNickMenu(Player viewer) {
        super(27, "§5Pick your name colors", viewer, false);
        
        build();
        open(viewer);
    }

    @Override
    protected void build() {
        inv.clear();
        inv.setItem(inv.getSize() - 1, makeItem(Material.PAPER, "§eName: §r" + builder.toString()));
        
        if(index >= viewer.getName().length()) {
            inv.setItem(18, makeItem(Material.LIME_STAINED_GLASS_PANE, "§aConfirm name: §r" + builder.toString()));
            return;
        }
        
        inv.setItem(0,  makeItem(Material.BLACK_WOOL,        "Add §0Black"));
        inv.setItem(1,  makeItem(Material.LAPIS_BLOCK,       "Add §1Dark blue"));
        inv.setItem(2,  makeItem(Material.GREEN_WOOL,        "Add §2Dark green"));
        inv.setItem(3,  makeItem(Material.CYAN_WOOL,         "Add §3Dark aqua"));
        inv.setItem(4,  makeItem(Material.RED_WOOL,          "Add §4Dark red"));
        inv.setItem(5,  makeItem(Material.PURPLE_WOOL,       "Add §5Purple"));
        inv.setItem(6,  makeItem(Material.GOLD_BLOCK,        "Add §6Gold"));
        inv.setItem(7,  makeItem(Material.STONE,             "Add §7Gray"));
        inv.setItem(8,  makeItem(Material.GRAY_WOOL,         "Add §8Dark gray"));
        inv.setItem(9,  makeItem(Material.BLUE_WOOL,         "Add §9Blue"));
        inv.setItem(10, makeItem(Material.LIME_WOOL,         "Add §aGreen"));
        inv.setItem(11, makeItem(Material.LIGHT_BLUE_WOOL,   "Add §bAqua"));
        inv.setItem(12, makeItem(Material.RED_TERRACOTTA,    "Add §cRed"));
        inv.setItem(13, makeItem(Material.PINK_CONCRETE,     "Add §dLight purple"));
        inv.setItem(14, makeItem(Material.YELLOW_WOOL,       "Add §eYellow"));
        inv.setItem(15, makeItem(Material.WHITE_WOOL,        "Add §fWhite"));
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        if(item.getType() == Material.PAPER) return;
        if(item.getType() == Material.LIME_STAINED_GLASS_PANE) {
            builder.append("§r");
            
            DonorPlayer.getDonorPlayer(viewer.getUniqueId()).setNick(builder.toString());
            viewer.setDisplayName(builder.toString());
            viewer.setPlayerListName(builder.toString());
            viewer.sendMessage(ChatColor.YELLOW + "You set your name to: " + viewer.getDisplayName());
            viewer.closeInventory();
            close(viewer);
            return;
        }
        
        ChatColor color = NickMenu.colorFromItem(item.getType());
        char at = viewer.getName().charAt(index);
        builder.append("" + color + at);
        index++;
        build();
    }
    
}
