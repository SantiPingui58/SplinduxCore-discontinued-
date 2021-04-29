package me.santipingui58.splindux.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.VotesMenu;
import me.santipingui58.splindux.gui.game.parkour.ParkourMenu;
import me.santipingui58.splindux.gui.game.FFAMenu;
import me.santipingui58.splindux.gui.game.RankedMenu;
import me.santipingui58.splindux.gui.game.TeamsMenu;
import me.santipingui58.splindux.gui.game.VersusMenu;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.npc.NPCType;
import me.santipingui58.splindux.npc.SplinduxNPC;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
public class NPCListener implements Listener {

	
	@EventHandler
	public void onInteract(NPCRightClickEvent e) {
		NPC npc = e.getNPC();
		Player p = e.getClicker();
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);

		 SplinduxNPC splinduxnpc = NPCManager.getManager().getSplinduxNPCBy(npc.getId());
		if (splinduxnpc!=null) {
			NPCType type = splinduxnpc.getType();
			 if (type.equals(NPCType.VOTES)) {
					new VotesMenu(sp).o(p);
				} else if (type.equals(NPCType.FISHING)) {
				} else {
					
					if (DataManager.getManager().areQueuesClosed() && !p.hasPermission("splindux.staff")) {
						p.sendMessage("§cQueues are currently closed.");
						return;
					}
					
				if (type.equals(NPCType.RANKED)) {
					new RankedMenu(sp).o(p);
				} else if (type.equals(NPCType.FFA)) {
					new FFAMenu(sp,null, null).o(sp.getPlayer());
				}  else if (type.equals(NPCType.TEAMS)) {
					new TeamsMenu(sp).o(sp.getPlayer());
				}  else if (type.equals(NPCType.VERSUS)) {
					new VersusMenu(sp).o(sp.getPlayer());
				} else if (type.equals(NPCType.PARKOUR)) {
					new ParkourMenu(sp).o(p);
				}
		}
			
} 
}
}
					
					
					//if (!sp.isInQueue()) {
				//		GameManager.getManager().addDuelQueue(sp, splinduxnpc.getType().getAmount(), null, splinduxnpc.getType().getSpleefType(),splinduxnpc.getType().isRanked());
				//} else {
				//	p.sendMessage("§cYou are already in a queue.");
			//	}

