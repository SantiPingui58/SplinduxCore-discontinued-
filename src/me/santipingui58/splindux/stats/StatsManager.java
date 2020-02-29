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

	
	private HashMap<String,Integer> spleefffawinsranking = new HashMap<String,Integer>();
	private HashMap<String,Integer> spleefffakillsranking = new HashMap<String,Integer>();
	private HashMap<String,Integer> spleefffagamesranking = new HashMap<String,Integer>();
	private HashMap<String,Double> spleefffawgranking = new HashMap<String,Double>();
	private HashMap<String,Double> spleefffakgranking = new HashMap<String,Double>();
	private HashMap<String,Integer> spleefffawinsranking_monthly = new HashMap<String,Integer>();
	private HashMap<String,Integer> spleefffakillsranking_monthly = new HashMap<String,Integer>();
	private HashMap<String,Integer> spleefffagamesranking_monthly = new HashMap<String,Integer>();
	private HashMap<String,Integer> spleefffawinsranking_weekly = new HashMap<String,Integer>();
	private HashMap<String,Integer> spleefffakillsranking_weekly = new HashMap<String,Integer>();
	private HashMap<String,Integer> spleefffagamesranking_weekly = new HashMap<String,Integer>();
	private HashMap<String,Integer> spleef1vs1winsranking = new HashMap<String,Integer>();
	private HashMap<String,Integer> spleef1vs1gamesranking = new HashMap<String,Integer>();
	private HashMap<String,Integer> spleef1vs1ELOranking = new HashMap<String,Integer>();
	private static StatsManager manager;	
	 public static StatsManager getManager() {
	        if (manager == null)
	        	manager = new StatsManager();
	        return manager;
	    }
	
	public int getRankingPosition(RankingType type,SpleefPlayer sp) {
		HashMap<String,Integer> ranking = getRanking(type);
		  Iterator<Entry<String, Integer>> it = ranking.entrySet().iterator();
		  int i = 1;
		  while (it.hasNext()) {
		        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
		        String name = pair.getKey();
		        if (name.equalsIgnoreCase(sp.getOfflinePlayer().getName())) {
		        	return i;
		        }
		        i++;
		        
		  }
		  return i;
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
	
public void sendRankingDouble(SpleefPlayer sp,int page,RankingType type) {
		
		HashMap<String,Double> hashmap = getHashMapByTypeDouble(type);
		
		if (hashmap.isEmpty()) {
			updateRankings();
		}
		  Iterator<Entry<String, Double>> it = hashmap.entrySet().iterator();
		  int inicio = (page*10)+1;
		  int fin = (page+1)*10;
		  int i = 1;
		    while (it.hasNext()) {
		        Map.Entry<String,Double> pair = (Map.Entry<String,Double>)it.next();
		        String name = pair.getKey();
		        Double wins = pair.getValue();
		        
		        if (i<=fin) {
		        	if (i>=inicio) {
		        sp.getPlayer().sendMessage("§6"+i +". §a"+name+"§7: §b" + String.format("%.00f", wins) + " " + getAmountByType(type));
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
			spleefffawinsranking.put(sp.getOfflinePlayer().getName(), sp.getFFAWins());
		}
			
			if (sp.getFFAKills()>0) {
				spleefffakillsranking.put(sp.getOfflinePlayer().getName(), sp.getFFAKills());
			}
			
			if (sp.getFFAGames()>0) {
				spleefffagamesranking.put(sp.getOfflinePlayer().getName(), sp.getFFAGames());
			}
			if (sp.getWinGameRatio()>0) {
				spleefffawgranking.put(sp.getOfflinePlayer().getName(), sp.getWinGameRatio());
			}
			
			if (sp.getKillGameRatio()>0) {
				spleefffakgranking.put(sp.getOfflinePlayer().getName(), sp.getKillGameRatio());
			}
			
			if (sp.getMonthlyFFAWins()>0) {
				spleefffawinsranking_monthly.put(sp.getOfflinePlayer().getName(), sp.getMonthlyFFAWins());
			}
				
				if (sp.getMonthlyFFAKills()>0) {
					spleefffakillsranking_monthly.put(sp.getOfflinePlayer().getName(), sp.getMonthlyFFAKills());
				}
				
				if (sp.getMonthlyFFAGames()>0) {
					spleefffagamesranking_monthly.put(sp.getOfflinePlayer().getName(), sp.getMonthlyFFAGames());
				}
				
				if (sp.getWeeklyFFAWins()>0) {
					spleefffawinsranking_weekly.put(sp.getOfflinePlayer().getName(), sp.getMonthlyFFAWins());
				}
					
					if (sp.getWeeklyFFAKills()>0) {
						spleefffakillsranking_weekly.put(sp.getOfflinePlayer().getName(), sp.getMonthlyFFAKills());
					}
					
					if (sp.getWeeklyFFAGames()>0) {
						spleefffagamesranking_weekly.put(sp.getOfflinePlayer().getName(), sp.getMonthlyFFAGames());
					}
					
					if (sp.get1vs1Wins()>0) {
						spleef1vs1winsranking.put(sp.getOfflinePlayer().getName(), sp.get1vs1Wins());
					}
					if (sp.get1vs1Wins()>0) {
						spleef1vs1gamesranking.put(sp.getOfflinePlayer().getName(), sp.get1vs1Games());
					}
					if (sp.getELO()>1000) {
						spleef1vs1ELOranking.put(sp.getOfflinePlayer().getName(), sp.getELO());
					}
		}
		
		spleefffawinsranking = sortByValue(spleefffawinsranking);
		spleefffakillsranking = sortByValue(spleefffakillsranking);
		spleefffagamesranking = sortByValue(spleefffagamesranking);
		
		
		spleefffawinsranking_monthly = sortByValue(spleefffawinsranking_monthly);
		spleefffakillsranking_monthly = sortByValue(spleefffakillsranking_monthly);
		spleefffagamesranking_monthly = sortByValue(spleefffagamesranking_monthly);
		
		spleefffawinsranking_weekly = sortByValue(spleefffawinsranking_weekly);
		spleefffakillsranking_weekly = sortByValue(spleefffakillsranking_weekly);
		spleefffagamesranking_weekly = sortByValue(spleefffagamesranking_weekly);
		
		spleefffawgranking = sortByValueDouble(spleefffawgranking);
		spleefffakgranking = sortByValueDouble(spleefffakgranking);
		
		spleef1vs1winsranking = sortByValue(spleef1vs1winsranking);
		spleef1vs1gamesranking = sortByValue(spleef1vs1gamesranking);
		spleef1vs1ELOranking = sortByValue(spleef1vs1ELOranking);
		
	}
	
	
	
	public HashMap<String,Integer> getRanking(RankingType type) {
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
		if (type.equals(RankingType.SPLEEFFFA_WINS)) {
			hashmap = spleefffawinsranking;
		} else if (type.equals(RankingType.SPLEEFFFA_KILLS)) {
			hashmap = spleefffakillsranking;
		} else if (type.equals(RankingType.SPLEEFFFA_GAMES)) {
			hashmap = spleefffagamesranking;
		} else if (type.equals(RankingType.SPLEEF1VS1_ELO)) {
			hashmap = spleef1vs1ELOranking;
		} else if (type.equals(RankingType.SPLEEF1VS1_GAMES)) {
			hashmap = spleef1vs1gamesranking;
		} else if (type.equals(RankingType.SPLEEF1VS1_WINS)) {
			hashmap = spleef1vs1winsranking;
		} 
		
		if (hashmap.isEmpty()) {
			updateRankings();
		}
		
		return hashmap;
	}
	
	public HashMap<String,Double> getRankingDouble(RankingType type) {
		HashMap<String,Double> hashmap = new HashMap<String,Double>();
		if (type.equals(RankingType.SPLEEFFFA_KG)) {
			hashmap = spleefffakgranking;
		} else {
			hashmap = spleefffawgranking;
		}
		
		if (hashmap.isEmpty()) {
			updateRankings();
		}
		
		return hashmap;
	}
	
	private HashMap<String,Integer> getHashMapByType(RankingType type) {
		if (type.equals(RankingType.SPLEEFFFA_WINS)) {
			return spleefffawinsranking;
		} else if (type.equals(RankingType.SPLEEFFFA_KILLS)) {
			return spleefffakillsranking;
		} else if (type.equals(RankingType.SPLEEFFFA_GAMES)) {
			return spleefffagamesranking;
		} 
		return null;
	}
	
	private HashMap<String,Double> getHashMapByTypeDouble(RankingType type) {
		if (type.equals(RankingType.SPLEEFFFA_KG)) {
			return spleefffakgranking;
		} else if (type.equals(RankingType.SPLEEFFFA_WG)) {
			return spleefffawgranking;
		}
		return null;
	}
	
	public String getAmountByType(RankingType type) {
		if (type.equals(RankingType.SPLEEFFFA_WINS)) {
			return "Wins";
		} else if (type.equals(RankingType.SPLEEFFFA_KILLS)) {
			return "Kills";
		} else if (type.equals(RankingType.SPLEEFFFA_GAMES)) {
			return "Games";
		}else if (type.equals(RankingType.SPLEEFFFA_WG)) {
			return "W/G";
		}else if (type.equals(RankingType.SPLEEFFFA_KG)) {
			return "K/G";
		}
		return null;
	}
	
	public String getTitleByType(RankingType type) {
		if (type.equals(RankingType.SPLEEFFFA_WINS)) {
			return "Spleef FFA Wins";
		} else if (type.equals(RankingType.SPLEEFFFA_KILLS)) {
			return "Spleef FFA Kills";
		} else if (type.equals(RankingType.SPLEEFFFA_GAMES)) {
			return "Spleef FFA Games";
		}
		return null;
	}
	  public  HashMap<String, Integer> sortByValue(HashMap<String,Integer> hm) { 
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
	  
	  public  HashMap<String, Double> sortByValueDouble(HashMap<String,Double> hm)     { 
	        // Create a list from elements of HashMap 
	        List<Map.Entry<String, Double> > list = 
	               new LinkedList<Map.Entry<String, Double> >(hm.entrySet()); 
	  
	        // Sort the list 
	        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() { 
	            public int compare(Map.Entry<String, Double> o2,  
	                               Map.Entry<String, Double> o1) 
	            { 
	                return (o1.getValue()).compareTo(o2.getValue()); 
	            } 
	        }); 
	
	        HashMap<String, Double> temp = new LinkedHashMap<String, Double>(); 
	        for (Map.Entry<String, Double> aa : list) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        } 
	        return temp; 
}
}