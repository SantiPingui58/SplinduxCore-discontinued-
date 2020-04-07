package me.santipingui58.splindux.task;

import java.util.Map;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.stats.RankingType;
import me.santipingui58.splindux.stats.StatsManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;



public class SortRankingTask {

	
	public SortRankingTask() {
		task();
	}
	
	
	private void task() {
	Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
		    public void run() {
		    	StatsManager.getManager().updateRankings();
		    	DataManager.getManager().savePlayers();
		    	boolean b = false;
		    	String name = "";
		    	 for (Map.Entry<String, Integer> entry : StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_WINS_WEEKLY).entrySet()) {
		    		 if (!b) {
		    			 b = true;
		    			 name = entry.getKey();
		    		 } else {
		    			 break;
		    		 }
		    	 }
		    	 
		    	 try {
		    	NPC npc = CitizensAPI.getNPCRegistry().getById(0);
		    	SkinnableEntity entity = (SkinnableEntity) npc.getEntity();
		    	if (!name.isEmpty() && !name.equalsIgnoreCase("")) {
		    	entity.setSkinName(name);
		    } else {
		    	entity.setSkinName("SantiPingui58");
		    }
		    	}catch(Exception e) {}
		    }
		    }, 0, 2*60*20L);
	
	
	}
}
