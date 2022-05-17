package me.santipingui58.splindux.quest;

import java.util.Set;

public class SplinduxQuest {

	private String name;
	private Set<QuestDialogues> dialogues;
	

	
	
	public SplinduxQuest(String name, Set<QuestDialogues> dialogues) {
		this.name = name;
		this.dialogues = dialogues;
	}
	
	
	public String getName() {
		return this.name;
	}
	 
	public Set<QuestDialogues> getDialogues() {
		return this.dialogues;
	}
	
	public QuestDialogues getDialogue(int npc) {
		for (QuestDialogues dialogues : this.dialogues) {
			if (dialogues.getNPCId()==npc) return dialogues;
		}
		return null;
	}



}
