package me.santipingui58.splindux.task;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.scoreboard.PinguiScoreboard;


public class TabTask {

	

	public TabTask() {
		task();

	}
	
	
	private void task() {
		
		 Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
		          
               @Override
               public void run() {
            	   
            	   PinguiScoreboard.getScoreboard().setTags();
            	   
            	   
               }
		 }, 10, 60L);
		
	}
}
