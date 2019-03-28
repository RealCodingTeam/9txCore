package org.realcodingteam.plan9.inv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.realcodingteam.plan9.objects.DonorPlayer;

public abstract class AbstractMenu implements InventoryHolder {

    protected static final List<Player> open_invs = new ArrayList<>();
    
    public static final void closeOpenInvs() {
        open_invs.forEach(Player::closeInventory);
        open_invs.clear();
    }
    
    public static final ItemStack makeItem(Material mat, String name, String... lore) {
        return makeItem(mat, 1, name, lore);
    }
    
    public static final ItemStack makeItem(Material mat, int amount, String name, String... lore) {
        ItemStack is = new ItemStack(mat, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if(lore != null && lore.length > 0) im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return is;
    }
    
    public static final String getLore(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        if(im.hasLore()) return im.getLore().get(0);
        return ChatColor.RED + "###ERROR###: " + im.getDisplayName();
    }
    
    protected Inventory inv;
    protected final Player viewer;
    protected final boolean has_parent;
    
    public AbstractMenu(int size, String title, Player viewer, boolean has_parent) {
        this.viewer = viewer;
        this.has_parent = has_parent;
        if(title.length() > 32) title = title.substring(0, 32);
        inv = Bukkit.createInventory(this, size, title);
        setDonorSlot(viewer, inv.getSize() - 1);
        
        viewer.closeInventory();
    }
    
    public final void open(Player p) {
        open_invs.add(p);
        p.openInventory(inv);
    }
    
    public void close(Player p) {
        open_invs.removeIf(p2 -> p2.getUniqueId().equals(p.getUniqueId()));
    }
    
    private void setDonorSlot(Player player, int slot) {
        DonorPlayer dp = DonorPlayer.getDonorPlayer(player.getUniqueId());
        
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Your balance is " + ChatColor.GOLD + dp.getDp());
        if(has_parent) lore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Click me to go back a menu!");
        
        ItemStack donor_item = makeItem(Material.GOLD_INGOT, ChatColor.DARK_PURPLE + "Balance", lore.toArray(new String[0]));
        inv.setItem(slot, donor_item);
    }
    
    @Override
    public final Inventory getInventory() { return inv; }

    protected abstract void build();
    public abstract void onInventoryClick(ItemStack item);
    
    public void openParent() {
        if(has_parent) {
            new RootMenu(viewer);
        }
    }
}
