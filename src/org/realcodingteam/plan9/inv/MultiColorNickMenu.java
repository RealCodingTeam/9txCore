package org.realcodingteam.plan9.inv;

import java.util.ArrayDeque;
import java.util.Deque;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.objects.DonorPlayer;

public final class MultiColorNickMenu extends AbstractMenu {

    private int index = 0;
    private Deque<String> letters = new ArrayDeque<String>();
    
    public MultiColorNickMenu(Player viewer) {
        super(27, "§5Pick your name colors", viewer, false);
        
        build();
    }

    @Override
    protected void build() {
        if(!letters.isEmpty()) {
            inv = Bukkit.createInventory(this, 27, asString(letters));
            inv.setItem(19, makeItem(Material.RED_STAINED_GLASS_PANE, "§cDelete last letter"));
        }
        
        inv.setItem(inv.getSize() - 1, makeItem(Material.PAPER, "§eName: §r" + asString(letters)));
        
        if(index < viewer.getName().length()) {
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
        } else {
            inv.setItem(18, makeItem(Material.LIME_STAINED_GLASS_PANE, "§aConfirm name: §r" + asString(letters)));
        }
        
        open(viewer);
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        //I changed back to if statements because
        //the switch statement bloated the compiled size.
        //This code compiles to about 5KB as is, but with the
        //switch statement, blew up to about 70KB. The entire
        //Material enum was copied into the class file.
        
        if(item.getType() == Material.PAPER) return;
        
        if(item.getType() == Material.LIME_STAINED_GLASS_PANE) {
            letters.add("§r");
            
            String name = asString(letters);
            
            DonorPlayer.getDonorPlayer(viewer.getUniqueId()).setNick(name);
            viewer.setDisplayName(name);
            viewer.setPlayerListName(name);
            viewer.sendMessage(ChatColor.YELLOW + "You set your name to: " + viewer.getDisplayName());
            viewer.closeInventory();
            close(viewer);
            return;
        }
        
        if(item.getType() == Material.RED_STAINED_GLASS_PANE) {
            if(letters.isEmpty() || index < 1) return;
            letters.removeLast();
            letters.removeLast();
            index--;
            inv = Bukkit.createInventory(this, 27, "§5Pick your name colors");
            build();
            return;
        }
        
        ChatColor color = NickMenu.colorFromItem(item.getType());
        char at = viewer.getName().charAt(index);
        letters.add("" + color);
        letters.add("" + at);
        index++;
        build();
    }
    
    private String asString(Deque<String> list) {
        return String.join("", list);
    }
}
