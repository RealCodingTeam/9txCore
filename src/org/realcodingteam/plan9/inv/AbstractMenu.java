package org.realcodingteam.plan9.inv;

import static org.realcodingteam.plan9.util.Item.makeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.data.DonorPlayer;

public abstract class AbstractMenu implements InventoryHolder {

    protected static final Map<Player, AbstractMenu> open_invs = new HashMap<>();
    
    public static final void closeOpenInvs() {
        open_invs.keySet().forEach(Player::closeInventory);
        open_invs.clear();
    }
    
    public static void triggerRefresh() {
        open_invs.values().stream().filter(AbstractMenu::needsRefresh).forEach(AbstractMenu::build);
    }
    
    protected Inventory inv;
    protected final Player viewer;
    protected final Consumer<Player> parent;
    
    public AbstractMenu(int size, String title, Player viewer, Consumer<Player> parent) {
        this.viewer = viewer;
        this.parent = parent;
        if(title.length() > 32) title = title.substring(0, 32);
        inv = Bukkit.createInventory(this, size, title);
        setDonorSlot(viewer, inv.getSize() - 1);
        
        viewer.closeInventory();
    }
    
    public abstract boolean needsRefresh();
    
    public final void open(Player p) {
        open_invs.put(p, this);
        p.openInventory(inv);
    }
    
    public void close(Player p) {
        open_invs.entrySet().removeIf(entry -> {
            Player player = entry.getKey();
            return player.getUniqueId().equals(p.getUniqueId());
        });
    }
    
    protected void setDonorSlot(Player player, int slot) {
        DonorPlayer dp = DonorPlayer.getDonorPlayer(player.getUniqueId());
        
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Your balance is " + ChatColor.GOLD + dp.getDp());
        if(parent != null) lore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Click me to go back a menu!");
        
        ItemStack donor_item = makeItem(Material.GOLD_INGOT, ChatColor.DARK_PURPLE + "Balance", lore.toArray(new String[0]));
        inv.setItem(slot, donor_item);
    }
    
    @Override
    public final Inventory getInventory() { return inv; }

    protected abstract void build();
    public abstract void onInventoryClick(ItemStack item);
    
    public void openParent() {
        if(parent != null) {
            parent.accept(viewer);
        }
    }
}
