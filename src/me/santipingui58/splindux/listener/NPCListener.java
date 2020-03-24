package me.santipingui58.splindux.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefType;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
public class NPCListener implements Listener {

	
	@EventHandler
	public void onInteract(NPCRightClickEvent e) {
		NPC npc = e.getNPC();
		Player p = e.getClicker();
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		
		//FFA
		if (npc.getId()==0) {
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getType().equals(SpleefType.SPLEEFFFA)) {
					GameManager.getManager().addQueue(sp, arena);
					
					}
			}
					return;
				} 
	}
}
