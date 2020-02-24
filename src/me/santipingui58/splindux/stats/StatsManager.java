package me.santipingui58.splindux.stats;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class StatsManager {

	
	private HashMap<String,Integer> ffawinsranking = new HashMap<String,Integer>();
	private HashMap<String,Integer> ffakillsranking = new HashMap<String,Integer>();
	
	private static StatsManager manager;	
	 public static StatsManager getManager() {
	        if (manager == null)
	        	manager = new StatsManager();
	        return manager;
	    }
	
	public int getRankingPosition(RankingType type) {
		return 0;
	}
	
	public void sendRanking(SpleefPlayer sp,int page,RankingType type) {
		HashMap<String,Integer> hashmap = getHashMapByType(type);
		
		if (hashmap.isEmpty()) {
			updateRankings();
		}
		  Iterator<Entry<String, Integer>> it = hashmap.entrySet().iterator();
		  int inicio = (page*10)+1;
		  int fin = (page+1)*10;
		  int i = 1;
		    while (it.hasNext()) {
		        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
		        String name = pair.getKey();
		        int wins = pair.getValue();
		        if (i<=fin) {
		        	if (i>=inicio) {
		        sp.getPlayer().sendMessage("§6"+i +". §a"+name+"§7: §b" + wins + " " + getAmountByType(type));
		        	}
		        i++;
		    }  else {
		    	break;
		    }
		    }
	
	}
	
	public void updateRankings() {
		for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			if (sp.getFFAWins()>0) {
			ffawinsranking.put(sp.getOfflinePlayer().getName(), sp.getFFAWins());
		}
			
			if (sp.getFFAKills()>0) {
				ffakillsranking.put(sp.getOfflinePlayer().getName(), sp.getFFAKills());
			}
		}
		
		ffawinsranking = sortByValue(ffawinsranking);
		ffakillsranking = sortByValue(ffakillsranking);
	}
	
	
	
	public HashMap<String,Integer> getRanking(RankingType type) {
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
		if (type.equals(RankingType.SPLEEFFFA_WINS)) {
			hashmap = ffawinsranking;
		} else if (type.equals(RankingType.SPLEEFFFA_KILLS)) {
			hashmap = ffakillsranking;
		}
		if (hashmap.isEmpty()) {
			updateRankings();
		}
		
		return hashmap;
	}
	
	private HashMap<String,Integer> getHashMapByType(RankingType type) {
		if (type.equals(RankingType.SPLEEFFFA_WINS)) {
			return ffawinsranking;
		} else if (type.equals(RankingType.SPLEEFFFA_KILLS)) {
			return ffakillsranking;
		}
		return null;
	}
	
	private String getAmountByType(RankingType type) {
		if (type.equals(RankingType.SPLEEFFFA_WINS)) {
			return "Wins";
		} else if (type.equals(RankingType.SPLEEFFFA_KILLS)) {
			return "Kills";
		}
		return null;
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