package me.santipingui58.splindux.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.events.quester.QuesterPostCompleteQuestEvent;
import me.blackvein.quests.events.quester.QuesterPreChangeStageEvent;
import me.blackvein.quests.events.quester.QuesterPreCompleteQuestEvent;
import me.blackvein.quests.events.quester.QuesterPreStartQuestEvent;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.quest.QuestDialogues;
import me.santipingui58.splindux.quest.QuestState;
import me.santipingui58.splindux.quest.QuestsManager;
import me.santipingui58.splindux.quest.SplinduxQuest;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

public class QuestListener implements Listener {

	@EventHandler
	public void onPreQuestStart(QuesterPreStartQuestEvent e) {
		Bukkit.broadcastMessage("a");
		SplinduxQuest quest = QuestsManager.getManager().getQuest(e.getQuest().getNpcStart().getId());
		if (quest==null) return;
		
			Player p = e.getQuester().getPlayer();
			e.setCancelled(true);
			int max = 1;
			new BukkitRunnable() {
				int i = 0;
				public void run() {
					p.sendMessage("[" + i+ "/"+max +"]");
					i++;
				}
			}.runTaskTimer(Main.get(), 0L, 50L);
			
			e.getQuester().takeQuest(e.getQuest(), false);
		
	}
	
	
	@EventHandler
	public void onPreQuestChangeStage(QuesterPreChangeStageEvent e) {
		Bukkit.broadcastMessage("b");
		e.getQuest();
	}
	
	
	@EventHandler
	public void onPreQuestFinish(QuesterPreCompleteQuestEvent e) {
		Bukkit.broadcastMessage("c");
	}
	
	@EventHandler
	public void onCompleteQuest(QuesterPostCompleteQuestEvent e) {
		Bukkit.broadcastMessage("d");
	}
	
	
	@EventHandler(priority= EventPriority.HIGHEST)
	public void onInteract(NPCRightClickEvent e) {
		boolean b = true;
		if (b) return;
		NPC npc = e.getNPC();
		Player p = e.getClicker();
	    SplinduxQuest splinduxQuest= QuestsManager.getManager().getQuest(npc.getId());
	    Bukkit.broadcastMessage("d");
	    if (splinduxQuest==null) return;
	    Bukkit.broadcastMessage("c");
		  Quester quester = Main.questsAPI.getQuester(p.getUniqueId());
		    Quest quest = Main.questsAPI.getQuest(splinduxQuest.getName());
		    QuestsManager qm = QuestsManager.getManager();
		   
		    if (qm.getQuestState(quester, quest).equals(QuestState.CAN_START)) {
		    	e.setCancelled(true);
		    	Bukkit.broadcastMessage("a");
		    	QuestDialogues dialogues = splinduxQuest.getDialogue(npc.getId());
		    	
		    	new BukkitRunnable() {
		    		int i = 0;
		    		public void run() {
		    			if (i>=dialogues.getStartMessage().size()) {
		    				cancel();
		    			}
		    			
		    			p.sendMessage(dialogues.getStartMessage().get(i));
		    			i++;
		    		}
		    	}.runTaskTimer(Main.get(), 0L, 40L);
		    	
		    	quester.takeQuest(quest, false);
		    }				
} 
	
}
	
	
	


