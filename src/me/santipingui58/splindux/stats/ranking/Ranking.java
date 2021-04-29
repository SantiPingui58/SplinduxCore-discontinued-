package me.santipingui58.splindux.stats.ranking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class Ranking {

	private LinkedHashMap<UUID, Integer> ranking;
	
	private HashMap<SpleefPlayer, Integer> positionCache;
	
	public Ranking(LinkedHashMap<UUID,Integer> ranking) {
		this.ranking = ranking;
		this.positionCache = new HashMap<SpleefPlayer,Integer>();
	}
	
	public Ranking() {
		this.ranking = new LinkedHashMap<UUID,Integer>();
		this.positionCache = new HashMap<SpleefPlayer,Integer>();
	}
	
	public LinkedHashMap<UUID,Integer> getRanking() {
		return this.ranking;
	}
	
	public int getPosition(SpleefPlayer sp) {
		UUID uuid = sp.getUUID();
		
		if (ranking.containsKey(uuid)) {
			if (positionCache.containsKey(sp)) {
				return positionCache.get(sp);
			} else {
				Set<UUID> keys = this.ranking.keySet();
				  List<UUID> listKeys = new ArrayList<UUID>(keys);				  
				  int index = listKeys.indexOf(uuid);
		positionCache.put(sp, index);
		 return index+1;
			}
	} else {
		return -1;
	}
	}
 
	public void setRanking(LinkedHashMap<UUID, Integer> sortByValueUUID) {
		this.ranking = sortByValueUUID;
		
	}
	
	
	public void remove0() {
		LinkedHashMap<UUID,Integer> newRanking = new LinkedHashMap<UUID,Integer>();
		for (Entry<UUID, Integer> entry : ranking.entrySet()) {
	        UUID key = entry.getKey();
	        int value = entry.getValue();
	       if (value>0) newRanking.put(key, value);
		}
		
		this.ranking = newRanking;
	}
	
}
