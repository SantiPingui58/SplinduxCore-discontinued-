package me.santipingui58.splindux.quest;

import java.util.List;

public class SplinduxQuestNPC {

	private int id;
	private List<String> quests;
	
	public SplinduxQuestNPC(int id, List<String> quests) {
		this.id = id;
		this.quests = quests;
	}
	
	public int getID() {
		return this.id;
	}

	public List<String> getQuests() {
		return this.quests;
	}
	
}
