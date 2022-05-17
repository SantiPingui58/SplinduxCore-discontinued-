package me.santipingui58.splindux.listener;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.hologram.HologramViewer;
import me.santipingui58.splindux.hologram.RankingHologram;
import net.minecraft.server.v1_12_R1.EntityArmorStand;


public class CustomPacketListener {

	
	public CustomPacketListener() {
		packet();
	}
	
	private void packet() {
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(new PacketAdapter(Main.get(),
                ListenerPriority.NORMAL,
                PacketType.Play.Client.USE_ENTITY) {
			@Override
            public void onPacketReceiving(PacketEvent event) {
				if (event.getPacket()==null) return;
				Player player = event.getPlayer();
                if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {                
                	PacketContainer packet = event.getPacket();
                	packet.getBlocks();
                	int id = packet.getIntegers().read(0);
                	for (RankingHologram h : HologramManager.getManager().getHolograms()) {
                		try {
                		HologramViewer viewer = HologramViewer.getHologramViewer(player.getUniqueId(), h.getType());
                		if (viewer==null) continue;
                		EntityArmorStand armorStand =   viewer.getArmorStand(id, h.getType());
                		if (armorStand==null) continue;   		
                	
                		
                		new BukkitRunnable() {
            				public void run() {
                		if (viewer.isPrimaryButton(id, h.getType())) {
                			HologramManager.getManager().primaryButton(viewer, h);
                			return;
                				}
                		if (viewer.isSecondaryButton(id, h.getType())) {
                			HologramManager.getManager().secondaryButton(viewer, h);
                			return;
                				}
                			}
                		}.runTask(Main.get());


                		} catch(Exception ex2) {}
                	}
                	
                }
                
                
                
			}
            
        }
		);
		
		/*
		protocolManager.addPacketListener(new PacketAdapter(Main.get(),
                ListenerPriority.NORMAL,
                new PacketType[] { PacketType.Play.Client.TAB_COMPLETE }) {
			@Override
			 public void onPacketReceiving(PacketEvent event) {
				 if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE)
	                    try {
	                        PacketContainer packet = event.getPacket();
	                        String message = (packet.getSpecificModifier(String.class).read(0)).toLowerCase();
	                        // The following is a boolean function that returns true if the command should be cancelled
	                        if(CommonScripts.commandIsNotInThree(event.getPlayer(), message)){
	                            // Cancel the event
	                            event.setCancelled(true);
	                        }
	                    } catch (Exception e) {
	                        Main.get().getLogger().info(ChatColor.RED + "Error in Packet Listener");
	                    }
			}
            
        }
		);
	*/
		
	}
}
