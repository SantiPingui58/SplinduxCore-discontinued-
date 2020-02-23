package me.santipingui58.splindux.task;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.scoreboard.PinguiScoreboard;



public class ScoreboardTask {

	
	public ScoreboardTask() {
		task();
	}
	
	
	private void task() {
	Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
		    public void run() {
		    	
		    	for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {	   		    		
		    		PinguiScoreboard.getScoreboard().scoreboard(sp);				
		    		}
		    }
		    }, 0, 10L);
	
	
	}
}
