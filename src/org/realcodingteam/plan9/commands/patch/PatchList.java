package org.realcodingteam.plan9.commands.patch;

import static org.realcodingteam.plan9.commands.TxCommand.Result.NO_PERMISSION;
import static org.realcodingteam.plan9.commands.TxCommand.Result.SUCCESS;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.realcodingteam.plan9.commands.TxCommand;
import org.realcodingteam.plan9.patches.Patch;
import org.realcodingteam.plan9.patches.PatchManager;
import org.realcodingteam.plan9.patches.TxPatch;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
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
            return NO_PERMISSION;
        }
        
        TxPatch[] patches = PatchManager.getPatches();
        if(!(executor instanceof Player) || args.length >= 1) {
            sendBoring(executor, patches);
        } else {
            sendFancy((Player) executor, patches);
        }
        return SUCCESS;
    }
    
    private static void sendBoring(CommandSender executor, TxPatch[] patches) {
        executor.sendMessage(ChatColor.BLUE + "Patches:");
        String format = "%s%s %s(%s by %s) %s- %s";
        for(TxPatch patch : patches) {
            Patch info = PatchManager.getPatchMetadata(patch);
            ChatColor color = PatchManager.isEnabled(patch)? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
            executor.sendMessage(color + String.format(format, color, info.display_name(), ChatColor.WHITE, info.internal_name(), info.author(), ChatColor.GRAY, info.description()));
        }
        executor.sendMessage("");
    }
    
    private static void sendFancy(Player player, TxPatch[] patches) {
        player.sendMessage(ChatColor.BLUE + "Patches: (hover over for info)");
        for(TxPatch patch : patches) {
            Patch info = PatchManager.getPatchMetadata(patch);
            ChatColor color = PatchManager.isEnabled(patch)? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
            BaseComponent base = new TextComponent(info.display_name());
            base.setColor(color);
            
            HoverEvent hover = new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] { createPatchHover(patch) });
            base.setHoverEvent(hover);
            player.sendMessage(base);
        }
        player.sendMessage("");
    }
    
    private static BaseComponent createPatchHover(TxPatch patch) {
        ChatColor color = PatchManager.isEnabled(patch)? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
        Patch info = PatchManager.getPatchMetadata(patch);
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
}
