package me.santipingui58.splindux.game.ranked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.stats.StatsManager;

public class RankedQueue {

	private SpleefType type;
	private HashMap<Integer,List<UUID>> queue = new HashMap<Integer,List<UUID>>();
	private UUID lastJoiner;
	
	public RankedQueue(SpleefType type) {
		this.type = type;
		queue.put(1, new ArrayList<UUID>());
		queue.put(2, new ArrayList<UUID>());
		queue.put(3, new ArrayList<UUID>());
	}
	
	public SpleefType getSpleefType() {
		return this.type;
	}
	
	
	public List<UUID> getQueue(int i) {
		return this.queue.get(i);
	}
	
	
	 public void addDuelQueue(SpleefPlayer sp, int teamSize) {
			sp.leave(false, false);	
			 sp.giveQueueItems(false,false,false);		
			 sp.getPlayer().sendMessage("§aYou have been added to the queue for " + type.toString()+ " " + teamSize + "V" + teamSize);
			 getQueue(teamSize).add(sp.getUUID());		
			 lastJoiner = sp.getUUID();
	 }

	 
	 public void sendDuels() {
		 for (Entry<Integer, List<UUID>> list : queue.entrySet()) {
			 int teamSize = list.getKey();
			if (list.getValue().size()< teamSize*2) continue;
			 
			 if (list.getValue().size()%2!=0) {
				 list.getValue().remove(lastJoiner);
			 }
			 
			 LinkedHashMap<UUID, Integer> elo = new LinkedHashMap<UUID,Integer>();
			 
			 for (UUID uuid : list.getValue()) {
				 if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
					 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(uuid);
					 elo.put(sp.getUUID(), sp.getPlayerStats().getELO(type));
				 }

			 }
			 
			 elo = StatsManager.getManager().sortByValueLinked(elo); 
			 elo = sort(elo);
			 for (UUID s : elo.keySet()) {
				 new BukkitRunnable() {
					 public void run() {
				 GameManager.getManager().addDuelQueue(SpleefPlayer.getSpleefPlayer(s), teamSize, null, type, true, null);
			 }
				 }.runTask(Main.get());
			 }
			 list.getValue().clear();
			 list.getValue().add(lastJoiner);
		 } 
		 
	 }
	 
	 
		public static LinkedHashMap<UUID,Integer> sort(LinkedHashMap<UUID,Integer> arrayToSort) {
		    int n = arrayToSort.size();
		    
		    int iterations = 0;
		    List<UUID> uuids = new ArrayList<UUID>();
		    uuids.addAll(arrayToSort.keySet());
		    for (int gap = n / 2; gap > 0 && iterations<=1; gap /= 2) {
		        for (int i = gap; i < n; i++) {
		        	
		            UUID key = uuids.get(i);
		            int j = i;
		            while (j >= gap && arrayToSort.get(uuids.get(j-gap)) > arrayToSort.get(key)) {
		                uuids.set(j, uuids.get(j - gap));
		                j -= gap;
		            }
		            uuids.set(j, key);
		        }
		        iterations++;
	}
		    
		    LinkedHashMap<UUID,Integer> map = new LinkedHashMap<UUID,Integer>();
		    
		    for (UUID u : uuids) {
		    	map.put(u, arrayToSort.get(u));
		    }
		    

	    return map;
		}
	
	
}
