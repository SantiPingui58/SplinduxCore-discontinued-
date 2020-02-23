package me.santipingui58.splindux.stats;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class StatsManager {

	
	private HashMap<String,Integer> ffawinsranking = new HashMap<String,Integer>();
	
	private static StatsManager manager;	
	 public static StatsManager getManager() {
	        if (manager == null)
	        	manager = new StatsManager();
	        return manager;
	    }
	
	public int getRankingPosition(RankingType type) {
		return 0;
	}
	
	
	
	public void updateRankings() {
		for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			ffawinsranking.put(sp.getPlayer().getName(), sp.getFFAWins());
		}
		
		ffawinsranking = sortByValue(ffawinsranking);
	}
	
	
	
	public HashMap<String,Integer> getFFAWinsRanking() {
		if (ffawinsranking.isEmpty()) {
			for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
				ffawinsranking.put(sp.getPlayer().getName(), sp.getFFAWins());
			}
			ffawinsranking = sortByValue(ffawinsranking);
		}
		
		return ffawinsranking;
	}
	
	
	  public  HashMap<String, Integer> sortByValue(HashMap<String,Integer> hm)     { 
	        // Create a list from elements of HashMap 
	        List<Map.Entry<String, Integer> > list = 
	               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 
	  
	        // Sort the list 
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
	            public int compare(Map.Entry<String, Integer> o2,  
	                               Map.Entry<String, Integer> o1) 
	            { 
	                return (o1.getValue()).compareTo(o2.getValue()); 
	            } 
	        }); 
	
	        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
	        for (Map.Entry<String, Integer> aa : list) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        } 
	        return temp; 
}
}