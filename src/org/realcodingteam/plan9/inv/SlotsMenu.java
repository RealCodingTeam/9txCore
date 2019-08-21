package org.realcodingteam.plan9.inv;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.realcodingteam.plan9.NtxPlugin;
import org.realcodingteam.plan9.data.DonorPlayer;
import org.realcodingteam.plan9.util.Item;

//TODO: Clean this class up, it's disgusting
public final class SlotsMenu extends AbstractMenu {
    
    private static int loc = 0; //internal
    
    private static final Material[] MATERIALS;           //The materials that'll be used in the game 
    private static final int ROLL_COST = 1;              //Cost of purchasing rolls
    private static final int ROLL_QUANTITY = 4;          //How many rolls will be purchased
    private static final double JACKPOT_RAISE_AMOUNT = .40;   //When a game is lost, the jackpot will be raised by this
    private static final int JACKPOT_START = 20;         //Initial jackpot amount
    private static final int ROLL_ANIMATION_AMOUNT = 20; //How many times the slots should roll before stopping
    private static final int ROLL_ANIMATION_SPEED = 3;   //Delay in ticks between each roll. Lower means faster
    
    private static double jackpot; //Current jackpot
    
    static {
        //Use all glazed terracotta and wool blocks in the game.
        //If I know my math, this means winning is a 1/32768 chance? (32 blocks ^ 3 in a row needed)
        MATERIALS = Arrays.stream(Material.values())
                          .filter(n -> {
                              return n.name().endsWith("GLAZED_TERRACOTTA")
                                  || n.name().endsWith("WOOL");
                          })
                          .toArray(Material[]::new);
        
        if(!NtxPlugin.instance().getConfig().contains("jackpot")) save();
        jackpot = NtxPlugin.instance().getConfig().getDouble("jackpot");
    }
    
    private boolean running = false; //Used to track a running game
    private final DonorPlayer dp; //The DonorPlayer wrapper for the viewer
    
    private SlotsMenu(Player viewer) {
        super(36, "§5Donor - Slots", viewer, null);
        dp = DonorPlayer.getDonorPlayer(viewer.getUniqueId());
        
        build();
        open(viewer);
    }

    @Override
    protected void build() {
        drawBottom(); //Set the bottom row of the inventory (start buttons and donor gold ingot)
        
        ItemStack[] items = getRandom();
        
        setInvSquare(1, items[0]);
        setInvSquare(2, items[1]);
        setInvSquare(3, items[2]);
    }

    @Override
    public void onInventoryClick(ItemStack item) {
        //only care about clicking on the start button
        if(item.getType() != Material.LIME_STAINED_GLASS_PANE || running) return;
            
        //ensure the player can play
        if(dp.getDp() < ROLL_COST && dp.getRolls() < 1) {
            String msg = String.format("§cSorry, you do not have enough donor points to play slots. You need at least §d%d§c DP.", ROLL_COST);
            viewer.sendMessage(msg);
            return;
        }
        
        //purchase rolls if the player is out and can afford it
        if(dp.getRolls() < 1) {
            dp.setRolls(ROLL_QUANTITY);
            dp.setDp(dp.getDp() - ROLL_COST);
            String msg = String.format("§ePurchased §d%d§e rolls for §d%d DP§e!", ROLL_QUANTITY, ROLL_COST);
            viewer.sendMessage(msg);
        }
        
        //take a roll from the player and start the game
        dp.setRolls(dp.getRolls() - 1);
        running = true;
        
        for(int i = ROLL_ANIMATION_AMOUNT; i > 0; i--) {
            final int index = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(NtxPlugin.instance(), () -> {
                build();
                viewer.playSound(viewer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, .2f, .2f * (index % 5 + 1));
                if(index >= ROLL_ANIMATION_AMOUNT) {
                    running = false;
                    checkWin();
                }
            }, i * ROLL_ANIMATION_SPEED);
        }
    }
    
    //Checks if the player has won by comparing the types of the items
    private void checkWin() {
        ItemStack slot1, slot2, slot3;
        slot1 = inv.getItem(0);
        slot2 = inv.getItem(3);
        slot3 = inv.getItem(6);
        
        if(slot1 == null || slot2 == null || slot3 == null
        || slot1.getType() != slot2.getType() || slot2.getType() != slot3.getType()) {
            viewer.playSound(viewer.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            viewer.sendMessage("§cSorry, you did not win this game.");
            jackpot += JACKPOT_RAISE_AMOUNT;
            drawBottom();
            return;
        }
        
        updateItems(); //update the winning items for cosmetic effect
        
        //Announce the player's win and grant them their winnings.
        //Then reset the jackpot to the initial amount
        Bukkit.broadcastMessage("§e[DONOR] " + viewer.getDisplayName() + "§e won slots! They won §d" + (int)jackpot + " DP§e!");
        dp.setDp(dp.getDp() + (int)jackpot);
        viewer.getWorld().playSound(viewer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        viewer.sendMessage("§aCongratulations! You just won " + (int)jackpot + " DP for winning slots!");
        jackpot = JACKPOT_START;
        drawBottom();
        save();
    }
    
    //sets a 3x3 square in the inventory to the item.
    //i = 1, 2, or 3. (Divide the inventory into 3 sections)
    private void setInvSquare(int i, ItemStack is) {
        if(i < 1) i = 1;
        if(i > 3) i = 3;
        
        int start = (i - 1) * 3;
        int end = i * 3;
        
        for(int slot = start; slot < end; slot++) {
            inv.setItem(slot, is);
            inv.setItem(slot + 9, is);
            inv.setItem(slot + 18, is);
        }
    }

    //generate the slots
    private ItemStack[] getRandom() {
        ItemStack[] gen = new ItemStack[3];
        for(int i = 0; i < gen.length; i++) {
            Material mat = MATERIALS[(int) Math.floor(Math.random() * MATERIALS.length)];
            gen[i] = Item.makeItem(mat, " ");
        }
        
        return gen;
    }
    
    //Sets the bottom row of the inventory
    private void drawBottom() {
        ItemStack START_BUTTON = Item.makeItem(
                Material.LIME_STAINED_GLASS_PANE, 
                "§aStart!", 
                "§eClick me to start playing!", 
                String.format("§d%d DP§e = §d%d rolls§e.", ROLL_COST, ROLL_QUANTITY),
                " ",
                "§eThe current jackpot is §d" + (int)jackpot + " DP§e!",
                " ",
                String.format("§e§oYou have §d§o%d§e§o roll%s left.", dp.getRolls(), dp.getRolls() > 1 || dp.getRolls() == 0? "s" : "")
        );
        
        //if not running, place the start button. otherwise, place an "in progress" item
        ItemStack is = !running? START_BUTTON : Item.makeItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "§9Good luck!", "", "§eThe current jackpot is §d" + (int)jackpot + " DP§e!");
        
        IntStream.range(inv.getSize() - 9, inv.getSize()).forEach(i -> inv.setItem(i, is));
        setDonorSlot(viewer, inv.getSize() - 5); //gold ingot with donor player's information
    }
    
    //save the jackpot 
    public static void save() {
        double toSave = JACKPOT_START;
        if(jackpot != 0) toSave = jackpot; //unitialized jackpot value is 0 
        
        NtxPlugin.instance().getConfig().set("jackpot", toSave);
        NtxPlugin.instance().saveConfig();
    }
    
    //give the winning items a cool effect and name
    private void updateItems() {
        for(int i = 0; i < inv.getSize() - 9; i++) {
            ItemStack is = inv.getItem(i);
            
            if(i % 2 == 0) is = Item.addEnchantGlow(is);
            is = Item.setDisplayName(is, "§6Winner!");
        }
    }
    
    //internal
    public static boolean handle(InventoryClickEvent event) {
        loc++;
        loc %= event.getInventory().getSize();
        
        if(!event.getClick().isShiftClick() || event.getSlot() != loc) return false;
        new SlotsMenu((Player)event.getWhoClicked());
        return true;
    }

    @Override
    public boolean needsRefresh() {
        return false;
    }
}
