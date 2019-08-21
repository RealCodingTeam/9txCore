package org.realcodingteam.plan9.inv;

import static org.realcodingteam.plan9.util.Item.makeItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.data.DonorPlayer;
import org.realcodingteam.plan9.util.Item;

public final class RootMenu extends AbstractMenu {
    
    private final DonorPlayer dp;
    private static final Map<Material, Consumer<Player>> children = new HashMap<>();
    static {
        children.put(Material.EXPERIENCE_BOTTLE, ExpMenu::new);
        children.put(Material.BREWING_STAND, PotionsMenu::new);
        children.put(Material.WHITE_WOOL, NickMenu::new);
        children.put(Material.GOLD_ORE, OresMenu::new);
        children.put(Material.COOKIE, MiscMenu::new);
    }

    public RootMenu(Player viewer) {
        super(27, ChatColor.DARK_PURPLE + "Donor", viewer, null);
        
        dp = DonorPlayer.getDonorPlayer(viewer.getUniqueId());
        build();
        open(viewer);
    }
    
    @Override
    protected void build() {
        inv.setItem(9, makeItem(Material.WHITE_WOOL,            "§5Nick",             "§aChange the color of your name!"));
        inv.setItem(11, makeItem(Material.EXPERIENCE_BOTTLE,     "§5XP",               "§aGive everyone online XP!"));
        inv.setItem(13, makeItem(Material.GOLD_ORE,              "§5Ores",             "§aIncrease the drop rate of ores!"));
        inv.setItem(15, makeItem(Material.BREWING_STAND,         "§5Potion Effects",   "§aGive everyone a cool effect!"));
        inv.setItem(17, makeItem(Material.COOKIE,                "§5Misc",             "§aDo other amazing stuff!"));
        
        inv.setItem(inv.getSize() - 2, getEffectsEnabledButton());
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        if(item.getType() == Material.BELL) {
            dp.setShouldReceiveEffects(!dp.receivesEffects());
            String inner = dp.receivesEffects()? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled";
            viewer.sendMessage(ChatColor.YELLOW + "You have " + inner + ChatColor.YELLOW + " potion effects for yourself.");
            build();
            return;
        }
        
        if(!children.containsKey(item.getType())) return;
        children.get(item.getType()).accept(viewer);
    }

    @Override
    public boolean needsRefresh() {
        return false;
    }
    
    private ItemStack getEffectsEnabledButton() {
        String enabled = " > ";
        String disabled = ChatColor.DARK_GRAY + "   ";
        
        ItemStack base = new ItemStack(Material.BELL, 1);
        base = Item.setDisplayName(base, ChatColor.LIGHT_PURPLE + "Potion Effects");
        base = Item.setLore(base, 
                (dp.receivesEffects()? ChatColor.DARK_GREEN + enabled : disabled) + "Enabled",
                (!dp.receivesEffects()? ChatColor.DARK_RED + enabled : disabled) + "Disabled"
        );
        
        if(dp.receivesEffects()) base = Item.addEnchantGlow(base);
        return base;
    }
}
