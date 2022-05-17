package me.santipingui58.splindux.quest;

import java.util.HashMap;
import java.util.List;

public class QuestDialogues {

	private int npc_id;
	private List<String> startMessage;
	private HashMap<Integer,List<String>> stageStartMessages;
	private HashMap<Integer,List<String>> currentStageMessages;
	private List<String> endMessage;
	
	
	public QuestDialogues(int id,List<String> startMessage, HashMap<Integer,List<String>> stageStartMessages, List<String> endMessage) {
		this.npc_id = id;
		this.startMessage = startMessage;
		this.stageStartMessages = stageStartMessages;
		this.endMessage = endMessage;
	}
	
	public List<String> getStartMessage() {
		return this.startMessage;
	}
	
	public List<String> getStageStartMessage(int stage) {
		return this.stageStartMessages.get(stage-1);
	}
	
	public int getNPCId() {
		return this.npc_id;
	}
	
	
	public List<String> getCurrentStageMessage(int stage) {
		return this.currentStageMessages.get(stage-1);
	}
	
	public List<String> getEndMessage() {
		return this.endMessage;
	}
	
}
