package org.realcodingteam.plan9.commands.patch;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.commands.Result;
import org.realcodingteam.plan9.commands.TxCommand;
import org.realcodingteam.plan9.patches.Patch;
import org.realcodingteam.plan9.patches.PatchManager;
import org.realcodingteam.plan9.patches.TxPatch;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public final class PatchList implements TxCommand {

    @Override
    public String usage() {
        return "[basic]";
    }
    
    @Override
    public Result execute(CommandSender executor, String[] args) {
        if(!executor.hasPermission("ntx.patch.list")) {
            return Result.NO_PERMISSION;
        }
        
        Collection<TxPatch> patches = PatchManager.getPatches();
        if(!(executor instanceof Player) || args.length >= 1) {
            sendBoring(executor, patches);
        } else {
            sendFancy((Player) executor, patches);
        }
        return Result.SUCCESS;
    }
    
    private static void sendBoring(CommandSender executor, Collection<TxPatch> patches) {
        executor.sendMessage(ChatColor.BLUE + "Patches:");
        String format = "%s%s %s(%s by %s) %s- %s";
        for(TxPatch patch : patches) {
            Patch info = patch.getMetadata();
            ChatColor color = PatchManager.isEnabled(patch)? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
            executor.sendMessage(color + String.format(format, color, info.display_name(), ChatColor.WHITE, info.internal_name(), info.author(), ChatColor.GRAY, info.description()));
        }
        executor.sendMessage("");
    }
    
    private static void sendFancy(Player player, Collection<TxPatch> patches) {
        boolean isAdmin = player.hasPermission("ntx.admin");
        
        player.sendMessage(ChatColor.BLUE + "Patches (hover over for info):");
        for(TxPatch patch : patches) {
            Patch info = patch.getMetadata();
            ChatColor color = PatchManager.isEnabled(patch)? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
            BaseComponent base = new TextComponent(info.display_name());
            base.setColor(color);
            
            BaseComponent hoverText = createPatchHover(patch);
            
            if(isAdmin) {
                BaseComponent[] extra = TextComponent.fromLegacyText(
                        ChatColor.YELLOW + "\nClick me to " + 
                        (patch.isEnabled()? ChatColor.RED + "disable" : ChatColor.GREEN + "enable") + 
                        ChatColor.YELLOW + " this patch"
                );
                
                for(BaseComponent comp : extra) {
                    hoverText.addExtra(comp);
                }
                
                base.setClickEvent(createClickEvent(patch));
            }
            
            HoverEvent hover = new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] { hoverText });
            base.setHoverEvent(hover);
            player.sendMessage(base);
        }
    }
    
    private static BaseComponent createPatchHover(TxPatch patch) {
        ChatColor color = PatchManager.isEnabled(patch)? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
        Patch info = patch.getMetadata();
        BaseComponent base = new TextComponent(info.display_name() + "\n");
        base.setColor(color);
        
        TextComponent description = new TextComponent(info.description() + "\n");
        description.setColor(ChatColor.GRAY);
        description.setItalic(true);
        
        TextComponent author = new TextComponent("Written by " + info.author() + "\n\n");
        author.setColor(ChatColor.BLUE);
        
        TextComponent internal = new TextComponent("ID: " + info.internal_name());
        internal.setColor(ChatColor.DARK_GRAY);
        
        base.addExtra(description);
        base.addExtra(author);
        base.addExtra(internal);
        
        return base;
    }
    
    private static ClickEvent createClickEvent(TxPatch patch) {
        StringBuilder command = new StringBuilder();
        command.append("/patch ");
        if(patch.isEnabled()) command.append("disable ");
        else command.append("enable ");
        command.append(patch.getMetadata().internal_name());
        
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, command.toString());
    }
}
