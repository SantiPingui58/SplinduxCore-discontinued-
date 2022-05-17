package me.santipingui58.splindux.sws;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;


import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.StatsManager;


public class SWSCountry {

	private String code;
	private LinkedHashMap<UUID,Integer> ranking = new LinkedHashMap<UUID,Integer>();
	
	private int SWCplayOffsAmount;
	private int continentalplayOffsAmount;
	private boolean hasNationalLeague;
	
	public SWSCountry(String code) {
		this.code = code;
		this.SWCplayOffsAmount=1;
	}

	public void setRanking(LinkedHashMap<UUID,Integer> ranking) {
		this.ranking = ranking;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public LinkedHashMap<UUID,Integer> getRanking() {
		return this.ranking;
	}
	
	public int getSWCPlayOffsAmount() {
		return this.SWCplayOffsAmount;
	}
	
	public void setSWCPlayOffsAmount(int i) {
		this.SWCplayOffsAmount = i;
	}
	
	public int getContinentalPlayOffsAmount() {
		return this.continentalplayOffsAmount;
	}
	
	public void setContinentalPlayOffsAmount(int i) {
		this.continentalplayOffsAmount = i;
	}
	
	public boolean hasNationalLeague() {
		return this.hasNationalLeague;
	}
	
	public void nationalLeague(boolean b) {
		this.hasNationalLeague = b;
	}

	public void updateRanking() {
	ranking = StatsManager.getManager().sortByValueLinked(ranking);
	}

	
	public int getPos(SpleefPlayer sp) {
		Iterator<Entry<UUID, Integer>> it = this.ranking.entrySet().iterator();
		  int i = 1;
		  while (it.hasNext()) {
		        Map.Entry<UUID,Integer> pair = (Map.Entry<UUID,Integer>)it.next();	        
		        if (pair.getKey().compareTo(sp.getUUID())==0) {
		        	return i;
		        }	        
		        i++;    
		  }
		  return i;
	}
	
}
