package me.santipingui58.splindux.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.santipingui58.splindux.Main;

public class QuestsManager {

	private static QuestsManager manager;	
	 public static QuestsManager getManager() {
	        if (manager == null)
	        	manager = new QuestsManager();
	        return manager;
	    }
	 
	 private List<SplinduxQuest> quests = new ArrayList<SplinduxQuest>();
	 private List<SplinduxQuestNPC> npcs = new ArrayList<SplinduxQuestNPC>();
	
	 
	 public List<SplinduxQuest> getQuests() {
		 return this.quests;
	 }
	 
	 public List<SplinduxQuestNPC> getNPCs() {
		 return this.npcs;
	 }
	 
	 
	 public void loadQuests() {
		 FileConfiguration config = Main.quests.getConfig();
		 if (config.contains("quests")) {
			 Set<String> quests = config.getConfigurationSection("quests").getKeys(false);	
			 for (String s : quests) {
				 Set<QuestDialogues> quest_dialogues = new HashSet<QuestDialogues>();
				 for (String d : config.getConfigurationSection("quests."+s+".messages").getKeys(false)) {
					 HashMap<Integer,List<String>> hashmap = new HashMap<Integer,List<String>>();
					 
					 List<String> start_message = config.getStringList("quests."+s+".messages."+d+".StartMessage");
					 List<String> finish_message = config.getStringList("quests."+s+".messages."+d+".FinishMessage");
					 int i = 1; 

					 for (String stages : config.getConfigurationSection("quests."+s+".messages."+d +".NotCompleted").getKeys(false)) {
						 List<String> stageMessage = config.getStringList("quests."+s+".messages."+d+".NotCompleted."+stages);
						 hashmap.put(i, stageMessage);
						 i++;
					 }
					 
					 QuestDialogues dialogues = new QuestDialogues(Integer.valueOf(d), start_message, hashmap, finish_message);
					 quest_dialogues.add(dialogues);
				 } 
				 SplinduxQuest splinduxQuest = new SplinduxQuest(s, quest_dialogues);
				 this.quests.add(splinduxQuest);
			 }
		 }
	 }
	 
	 public void loadNPCs() {
		 FileConfiguration config = Main.quests.getConfig();
		 if (config.contains("npcs")) {
			 Set<String> npcs = config.getConfigurationSection("npcs").getKeys(false);	
			 for (String s : npcs) {
				 List<String> quests = config.getStringList("npcs."+s+".quests");
				 SplinduxQuestNPC sqnpc = new SplinduxQuestNPC(Integer.valueOf(s), quests);
				 this.npcs.add(sqnpc);
				 
			 }
		 }
	 }

	public SplinduxQuestNPC getNPC(int id) {
		for (SplinduxQuestNPC sqn : this.npcs) {
			if (sqn.getID()==id) return sqn;
		} 
		return null;
	}

	public SplinduxQuest getQuest(int id) {
	
		for (SplinduxQuest quest : this.quests) {
			Bukkit.broadcastMessage("quest");
			for (QuestDialogues dialogues : quest.getDialogues()) {
				Bukkit.broadcastMessage("dialogue");
				Bukkit.broadcastMessage(dialogues.getNPCId() + " " + id);
				if (dialogues.getNPCId()==id) return quest;
			}
		}
		return null;
	}
	
	
	
	public QuestState getQuestState(Quester quester, Quest quest) {
		Bukkit.broadcastMessage(quester.toString());
		Bukkit.broadcastMessage(quest.toString());
		if (quester.getCompletedQuests().contains(quest.getName())) {
			return QuestState.COMPLETED;
		} else if (quester.getCurrentQuests().containsKey(quest)) {
			return QuestState.IN_PROGRESS;
		} else {
			return QuestState.CAN_START;
		}
	}
	 

	 
	 
	 
}
