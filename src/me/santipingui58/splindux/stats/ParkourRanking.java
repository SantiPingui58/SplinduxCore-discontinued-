package me.santipingui58.splindux.stats;

import java.util.HashMap;
import java.util.UUID;

import me.santipingui58.splindux.game.parkour.Level;

public class ParkourRanking {

	private Level level;
	private HashMap<UUID,Integer> ranking;
	
	public ParkourRanking(Level level) {
		this.level = level;
		this.ranking = new HashMap<UUID,Integer>();
	}
	
	public Level getLevel() {
		return this.level;
	}
	public HashMap<UUID,Integer> getRanking() {
		return this.ranking;
	}
	
	public void setRanking(HashMap<UUID,Integer> ranking) {
		this.ranking = ranking;
	}
	
}
