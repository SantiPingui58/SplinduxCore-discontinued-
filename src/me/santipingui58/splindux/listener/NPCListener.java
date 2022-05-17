package me.santipingui58.splindux.listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.VotesMenu;
import me.santipingui58.splindux.gui.game.parkour.ParkourMenu;
import me.santipingui58.splindux.gui.game.GameMenu;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.npc.NPCType;
import me.santipingui58.splindux.npc.SplinduxNPC;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
public class NPCListener implements Listener {

	
	@EventHandler
	public void onInteract(NPCRightClickEvent e) {
		new BukkitRunnable() {
			public void run() {
				NPC npc = e.getNPC();
				Player p = e.getClicker();
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);

				 SplinduxNPC splinduxnpc = NPCManager.getManager().getSplinduxNPCBy(npc.getId());
				if (splinduxnpc!=null) {
					NPCType type = splinduxnpc.getType();
					Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
						
						 if (type!=null && type.equals(NPCType.VOTES)) {
								new VotesMenu(sp).o(p);
							} else {
								
								if (DataManager.getManager().areQueuesClosed() && !p.hasPermission("splindux.staff")) {
									p.sendMessage("§cQueues are currently closed.");
									return;
								}
								
								Party party = PartyManager.getManager().getParty(p);
								if (party!=null && !party.isLeader(p)) {
									p.sendMessage("§cOnly the party leader can join a game.");
									return;
								}
								
								if (type!=null && type.equals(NPCType.PARKOUR)) {
									new ParkourMenu(sp).o(p);
								} else if (type!=null && type.equals(NPCType.THETOWERS)) {
									  ByteArrayOutputStream b = new ByteArrayOutputStream();
								        DataOutputStream out = new DataOutputStream(b);

								        try {
								            out.writeUTF("Connect");
								            out.writeUTF("thetowers");
								        } catch (Exception e) {
								            return;
								        }

								        p.sendPluginMessage(Main.get(), "BungeeCord", b.toByteArray());
								}  else if (splinduxnpc.getSpleefType()!=null) {
									new GameMenu(sp,splinduxnpc.getSpleefType()).o(p);
								}		 
								
					}
					},1L);
							
		} 
				
				if (npc.getId()==306) {
					p.teleport(new Location(p.getWorld(),155,179,136));
				} else if (npc.getId()==307) {
					p.performCommand("/lobby");
				} 
			}
		}.runTaskAsynchronously(Main.get());
		
	
}
}
					
					
					//if (!sp.isInQueue()) {
				//		GameManager.getManager().addDuelQueue(sp, splinduxnpc.getType().getAmount(), null, splinduxnpc.getType().getSpleefType(),splinduxnpc.getType().isRanked());
				//} else {
				//	p.sendMessage("§cYou are already in a queue.");
			//	}

