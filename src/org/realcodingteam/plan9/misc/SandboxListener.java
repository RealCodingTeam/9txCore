package org.realcodingteam.plan9.misc;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.realcodingteam.plan9.NtxPlugin;

public final class SandboxListener implements Listener {

    public SandboxListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, NtxPlugin.getInstance());
    }

}
