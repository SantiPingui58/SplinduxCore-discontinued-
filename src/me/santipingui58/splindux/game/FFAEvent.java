package me.santipingui58.splindux.game;

import java.util.HashMap;

public class FFAEvent {

	
	private int currentRound;
	private int maxRounds;
	private HashMap<SpleefPlayer,Integer> points;
	
	
	public FFAEvent(int maxRounds) {
		this.maxRounds = maxRounds;
		this.currentRound = 1;
		this.points = new HashMap<SpleefPlayer,Integer>();
	}
	
	public int getCurrentRound() {
		return this.currentRound;
	}
	
	public void nextRound() {
		if (this.currentRound<this.maxRounds)
		this.currentRound++;
	}
	
	public int getMaxRounds() {
		return this.maxRounds;
	}
	
	public HashMap<SpleefPlayer,Integer> getPoints() {
		return this.points;
	}
	
}
