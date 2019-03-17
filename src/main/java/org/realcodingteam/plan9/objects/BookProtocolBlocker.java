package org.realcodingteam.plan9.objects;

import java.util.HashSet;
import java.util.Set;

import org.realcodingteam.plan9.NtxPlugin;
import org.realcodingteam.plan9.listeners.BookOverloadListener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;

public class BookProtocolBlocker {
    
    public static void start() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketAdapter.AdapterParameteters params = PacketAdapter.params();
        Set<PacketType> packets = new HashSet<PacketType>();
        
        packets.add(PacketType.Play.Client.CUSTOM_PAYLOAD);
        
        params.types(packets).plugin(NtxPlugin.instance).gamePhase(GamePhase.PLAYING).connectionSide(ConnectionSide.CLIENT_SIDE).listenerPriority(ListenerPriority.HIGHEST);
        
        manager.addPacketListener(new PacketAdapter(params) {        
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                try {
                    if(packet.getStrings().read(0).equals("MC|BSign")) {
                        if(BookOverloadListener.shouldBlock(event.getPlayer())) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage("§cCould not write book because you never right clicked it?!?");
                        }
                    }
                } catch(Exception ignored) {}
            }
        });
    }
    
}
