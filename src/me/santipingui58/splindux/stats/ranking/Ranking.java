package me.santipingui58.splindux.stats.ranking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;


public class Ranking {

	private LinkedHashMap<UUID, Integer> ranking;
	
	private HashMap<UUID, Integer> positionCache;
	
	public Ranking(LinkedHashMap<UUID,Integer> ranking) {
		this.ranking = ranking;
		this.positionCache = new HashMap<UUID,Integer>();
	}
	
	public Ranking() {
		this.ranking = new LinkedHashMap<UUID,Integer>();
		this.positionCache = new HashMap<UUID,Integer>();
	}
	
	public LinkedHashMap<UUID,Integer> getRanking() {
		return this.ranking;
	}
	
	public int getPosition(UUID uuid) {
		
		if (ranking.containsKey(uuid)) {
			if (positionCache.containsKey(uuid)) {
				return positionCache.get(uuid);
			} else {
				Set<UUID> keys = this.ranking.keySet();
				  List<UUID> listKeys = new ArrayList<UUID>(keys);				  
				  int index = listKeys.indexOf(uuid);
		positionCache.put(uuid, index+1);
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
