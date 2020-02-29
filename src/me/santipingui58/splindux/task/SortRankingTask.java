package me.santipingui58.splindux.task;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.stats.StatsManager;



public class SortRankingTask {

	
	public SortRankingTask() {
		task();
	}
	
	
	private void task() {
	Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
		    public void run() {
		    	StatsManager.getManager().updateRankings();
		    	
		    }
		    }, 0, 3*60*20L);
	
	
	}
}
