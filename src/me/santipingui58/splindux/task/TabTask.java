package me.santipingui58.splindux.task;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.scoreboard.PinguiScoreboard;
import me.santipingui58.splindux.scoreboard.hologram.HologramManager;


public class TabTask {

	private int holograms;

	public TabTask() {
		task();

	}
	
	
	private void task() {
		
		 Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
		          
               @Override
               public void run() {
            	   
            	   PinguiScoreboard.getScoreboard().setTags();
            	   NPCManager.getManager().updateNPCs();
            	   
            	   holograms++;
            	   if (holograms>=40) {
            		   holograms =0;
            		   HologramManager.getManager().updateHolograms(false);
            	   } else {
            		   HologramManager.getManager().updateHolograms(true);
            		   }
            	   
            	 
            	   
               }
		 }, 10, 60L);
		
	}
}
