package me.santipingui58.splindux.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.scoreboard.PinguiScoreboard;



public class ScoreboardTask {

	
	public ScoreboardTask() {
		task();
	}
	
	
	private void task() {
	Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
		    public void run() {
		    	
		    	for (Player p : Bukkit.getOnlinePlayers()) {	   		    		
		    		PinguiScoreboard.getScoreboard().scoreboard(p);	
		    		
		    		}
		    }
		    }, 0, 10L);
	
	
	}
}
