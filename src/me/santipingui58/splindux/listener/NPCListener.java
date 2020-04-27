package me.santipingui58.splindux.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
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
		
		 if (ChatColor.getLastColors(npc.getName()).equalsIgnoreCase("§c")) {
			p.sendMessage("§cComing soon..."); 
			return;
		 }
		 
		 
		//FFA
		 SplinduxNPC splinduxnpc = NPCManager.getManager().getSplinduxNPCBy(npc.getId());
		if (splinduxnpc!=null) {
			if (splinduxnpc.getType().equals(NPCType.FFA)) {	
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getGameType().equals(GameType.FFA)) {
					arena.addFFAQueue(sp);		
					return;
					}
				
			}		
				} else if (splinduxnpc.getType().equals(NPCType.VOTES)) {
					
				} else {
					GameManager.getManager().addDuelQueue(sp, splinduxnpc.getType().getAmount(), null, splinduxnpc.getType().getSpleefType());
				}
	}
	}
}
